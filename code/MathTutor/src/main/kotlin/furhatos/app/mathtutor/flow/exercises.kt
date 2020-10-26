package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.*
import furhatos.app.mathtutor.mathskill
import furhatos.app.mathtutor.nlu.DontUnderstand
import furhatos.app.mathtutor.nlu.*
import furhatos.app.mathtutor.score
import furhatos.flow.kotlin.*
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.onResponse
import furhatos.flow.kotlin.state
import furhatos.nlu.common.DontKnow
import furhatos.nlu.common.Number
import java.lang.NumberFormatException
import java.math.BigDecimal
import java.math.RoundingMode

// Custom data class consisting of a question and answer, since Kotlin can't return a tuple.
data class ExerciseTuple(val question: String, val percentage : Int, val Value : Int, val answer: Int)

fun getRandomExercise(): ExerciseTuple {
    // TODO: if difficulty is easy, only give answers that results in nice integers
    val randomPercentage = (1..19).random() // random number between 1 and 19
    val percentage: Int = randomPercentage * 5 // scale random percentage from 5 to 95
    val randomValue: Int = (1..10).random()
    val value: Int = randomValue * 10 // scale to value between 10 and 100
    val answerTemp: BigDecimal = (percentage.toBigDecimal().divide(BigDecimal(100)).multiply(value.toBigDecimal())) // calculate the answer
    val answer: Int = answerTemp.setScale(0, RoundingMode.UP).toInt()
    val question = "What is $percentage% of $value?"
    println("$percentage% of $value is $answer")

    // test if answer results in an integer, replace question if it doesn't
    return try {
        println("Exact Integer Value of " +
                answerTemp + " is " + answerTemp.intValueExact())
        return ExerciseTuple(question, percentage, value, answer)
    } catch (e: ArithmeticException) {
        println("answer does not end with a 0, replace question")
        getRandomExercise()
    }
}


val AskExercise: State = state(Interaction) {
    var failedAttempts = 0
    var (question, percentage, value, answer) = getRandomExercise()
    onEntry {
        var score = users.current.score
        if (score > 5) { goto(exerciseSummary(score)) }


        failedAttempts = 0
        users.current.isAnsweringExercise = true
        val response = furhat.askFor<Number>(question) {
            onResponse<DontKnow> {
                call(Encouragement)
                reentry()
            }
            onResponse<Help> {
                println("user requested help, starting explanation ")
                call(exerciseExplanation(percentage, value))
                reentry()
            }
        }

        // Try to catch responses where numbers are interpreted as text or where parser is confused
        // ex: "That's a tough one, I think it's 32" (exception on 'one')
        try {
            response?.toText()?.toInt()
        }
        catch (e: NumberFormatException) {
            println("couldn't parse number out of response")
            furhat.say("Could you please repeat your answer?")
            reentry()
        }

        // to check with answer(Int) we need to cask our response(Number) to response(Int).
        val parsedResponse = response?.toText()?.toInt()
        if (parsedResponse == answer) {
            println("answer correct")
            users.current.score++
            score = users.current.score
            println("updated score is $score")
            furhat.say {
                random {
                    +"Correct!"
                    +"Good job!"
                    +"Well done!"
                }
            }
            users.current.isAnsweringExercise = false

            // get new question and re-enter state
            val (newQ, newP, newV, newA) = getRandomExercise()
            question = newQ
            percentage = newP
            value = newV
            answer = newA
            reentry()

        }
        else {
            //TODO: Give user chance to try again?
            println("user gave wrong answer")
            furhat.say("Unfortunately this answer is wrong. The correct answer was $answer")
            users.current.isAnsweringExercise = false

            // get new question and re-enter state
            val (newQ, newP, newV, newA) = getRandomExercise()
            question = newQ
            percentage = newP
            value = newV
            answer = newA
        }
    }


    /* If we get a response that doesn't map to any alternative or any of the above handlers,
        we track how many times this has happened in a row and give them two more attempts and
        finally moving on if we still don't get it.
     */
    onResponse {
        failedAttempts++
        when (failedAttempts) {
            1 -> furhat.ask("I didn't get that, sorry. Try again!")
            2 -> {
                furhat.say("Sorry, I still didn't get that")
            }
            else -> {
                furhat.say("Still couldn't get that. Let's try a new question")
                reentry()
            }
        }
    }


}


fun exerciseExplanation(percentage : Int, value: Int)  = state {
    onEntry {
        val percentageRemainder : Int = percentage % 10
        val percentageDividedByTen : Int = percentage / 10
        furhat.say("Let's calculate the answer together.")
        furhat.say("10% of $value is the same as $value divided by 10. In our case this results in ${value/10}.")
        // TODO: adjust for other remainders than 5. Not necessary if we do not create these percentages anyway
        if (percentageRemainder > 0) {furhat.say("5% is half of 10%. 5% of $value will give a value of ${value/20}")}
        furhat.say("To get to ${percentageDividedByTen * 10}%, " +
                "we would need to multiply ${value / 10} by $percentageDividedByTen.")
        if (percentageRemainder > 0) {furhat.say("Now we can add the remaining 5% we calculated earlier " +
                "to come to our final answer")}
        furhat.ask("Do you know the answer to the question now?")
    }
    onResponse<Yes> { terminate() /* go back to the question */ }
    onResponse<No> { reentry() }
    // TODO: add response if asked for repeat
    onNoResponse { terminate() }
}


fun exerciseSummary(score : Int) : State = state {
    println("give a summary")
    onEntry {
        furhat.say("You answered $score questions correct")
    }
}
