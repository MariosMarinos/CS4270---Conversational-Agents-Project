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
    onEntry {
        users.current.mathskill.answeringExercise.apply {  }
        val (question, answer) = getRandomExercise()
        // can't ask for non integers..
        val response = furhat.askFor<Number>(question) {
            onResponse<DontKnow> {
                call(Encouragement)
                reentry()
            }
        }

        // to check with answer(Float) we need to cask our response (Number) to (Float).
        if (response?.toText()?.toFloat() == answer) {
            println("answer correct")
            var newScore = users.current.mathskill.correctCounter
            println("current score is $newScore")
            users.current.mathskill.correctCounter++
            newScore = users.current.mathskill.correctCounter
            println("updated score is $newScore")

            furhat.say {
                random {
                    +"Correct!"
                    +"Good job!"
                    +"Well done!"
                }
            }
        }
        else {
            furhat.say("Unfortunately this answer is wrong. The correct answer is $answer")
            goto(ExerciseIntro)
        }

        // check response
    }

    onResponse<Yes> {
        furhat.say("Good job")
    }

    onResponse<No> {
        goto(Encouragement)
    }

    onResponse<DontUnderstand> {
        goto(Encouragement)
    }
}
