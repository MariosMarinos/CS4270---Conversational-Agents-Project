package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.Request
import furhatos.app.mathtutor.nlu.*
import furhatos.app.mathtutor.questionsAsked
import furhatos.app.mathtutor.score
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.Number
import furhatos.records.Location
import java.math.BigDecimal
import java.math.RoundingMode


// Custom data class consisting of a question and answer, since Kotlin can't return a tuple.
data class ExerciseTuple(val question: String, val percentage : Int, val Value : Int, val answer: Int)

val ExerciseIntro: State = state(Interaction) {
    onEntry {
        furhat.gesture(indefiniteBigSmile)
        furhat.say("Great! Let's try some exercises to practice your newly acquired skill.")
        furhat.gesture(indefiniteSmile)
        goto(AskExercise)
    }
}

val AskExercise: State = state(Interaction) {
    var (question, percentage, value, answer) = getRandomExercise()
    onEntry {
        furhat.gesture(indefiniteSmile)
        var score = users.current.score
        if (score != 0 && score % 5 == 0) { // ask for a break every 5 questions
            furhat.say("You've been practising for a while now.")
            call(requestBreak)
        }
        furhat.attend(randomLookAway())
        delay(1000)
        furhat.attend(users.current)
        furhat.voice.rate = 0.7
        val response = furhat.askFor<Number>(question) {

            onResponse<DontUnderstand> {
                furhat.voice.rate = 0.9
                call(Encouragement)
                reentry()
            }
            onResponse<Help> {
                callEmotion()
                //TODO nod at user? requires get location of user which i cant find
                furhat.voice.rate = 0.9
                furhat.gesture(Gestures.Nod)
                furhat.say {
                    random{
                        + "Let's calculate the answer together"
                        + "Let me guide you to the answer"
                    }
                }
                call(exerciseExplanation(percentage, value))
                reentry()
            }
            onResponse<Stop> { furhat.voice.rate = 0.9
                goto(ExerciseSummary) }
        }

        // Try to catch responses where numbers are interpreted as text or where parser is confused
        // ex: "That's a tough one, I think it's 32" (exception on 'one')
        furhat.voice.rate = 0.9
        var parsedResponse: Int?
        try {
            parsedResponse = response?.toText()?.toInt()
        }
        catch (e: Exception) {
            val temp = response?.toText().toString()
            when (temp) {
                "one" -> parsedResponse = 1
                "two" -> parsedResponse = 2
                "three" -> parsedResponse = 3
                "four" -> parsedResponse = 4
                "five" -> parsedResponse = 5
                "six" -> parsedResponse = 6
                "seven" -> parsedResponse = 7
                "eight" -> parsedResponse = 8
                "nine" -> parsedResponse = 9
                "ten" -> parsedResponse = 10
                else -> {
                    println("couldn't parse number out of response")
                    furhat.say("Could you please repeat your answer?")
                    reentry()
                }
            }
        }

        // if we make it this far, we know the question will be asked, update counter
        users.current.questionsAsked++
        // to check with answer(Int) we need to cask our response(Number) to response(Int).
        //val parsedResponse = response?.toText()?.toInt()
        if (parsedResponse == answer) {
            println("answer correct")
            val emotion = Request()
            // if the emotion is happy
            if (emotion == "Happy") EmotionSeenEncouragment(emotion)
            users.current.score++
            score = users.current.score
            println("updated score is $score")
            furhat.gesture(indefiniteBigSmile)
            furhat.say {
                + behavior {
                    furhat.gesture(Gestures.Nod)
                }
                random {
                    +"Correct!"
                    +"Good job!"
                    +"Well done!"
                }
            }
            // get new question and re-enter state
            val (newQ, newP, newV, newA) = getRandomExercise()
            question = newQ
            percentage = newP
            value = newV
            answer = newA
            reentry()
        }
        else {
            // TODO: Give user another try at the answer?
            println("user gave wrong answer")
            val emotion = Request()
            // if the user is sad, fearful or surprised encourage the student.

            if (emotion == "Sad" || emotion == "Fearful" || emotion == "Surprised") EmotionSeenEncouragment(emotion)
            furhat.gesture(stopSmile)
            furhat.say("Unfortunately this answer is wrong. The correct answer was $answer")
            // get new question and re-enter state
            val (newQ, newP, newV, newA) = getRandomExercise()
            question = newQ
            percentage = newP
            value = newV
            answer = newA
            reentry()
        }
    }
}


fun exerciseExplanation(percentage : Int, value: Int)  = state {
    onEntry {
        val percentageRemainder : Int = percentage % 10
        val percentageDividedByTen : Int = percentage / 10
        furhat.attend(Location(0.0, -0.3, 1.5))
        furhat.voice.rate = 0.7
        furhat.say("10% of $value is the same as $value divided by 10. In our case this results in ${value/10}.")
        if (percentageRemainder > 0) {furhat.say("5% is half of 10%. 5% of $value will give a value of ${value/20}")}
        furhat.say("To get to ${percentageDividedByTen * 10}%, " +
                "we would need to multiply ${value / 10} by $percentageDividedByTen.")
        furhat.gesture(Gestures.GazeAway)
        delay(400)
        furhat.attend(users.current)
        if (percentageRemainder > 0) {furhat.say("Now we can add the remaining 5% we calculated earlier " +
                "to come to our final answer")}
        furhat.voice.rate = 0.9
        // if the user is Surprised or sad after the explanation say the encouragment and ask if he want
        // to repeat the explanation.
        val emotion = Request()
        if (emotion == "Surprised" || emotion == "Sad") {
            furhat.say("I can see that you didn't understand my explanation, therefore I am going to repeat the explanation")
            delay(1000)
            reentry()
        }
        furhat.ask("Do you know the answer to the question now?")
    }
    onResponse<Yes> { terminate() /* go back to the question */ }
    onResponse<No> { reentry() }
    onResponse<Repeat> { reentry() }
    onNoResponse { terminate() }
}


val ExerciseSummary : State = state {
    onEntry {
        val score = users.current.score
        val questionsAsked = users.current.questionsAsked
        if (score > questionsAsked * 0.6){
            furhat.gesture(indefiniteBigSmile)
        }
        furhat.say("You worked hard today")
        furhat.say("You answered $score out of $questionsAsked correct")
        furhat.say("Well done!")
        delay (900)
        furhat.gesture(stopSmile)
        furhat.ask("Do you want to practice some more?")
    }
    onResponse<No> { goto(Goodbye) }
    onResponse<Yes> { goto(AskExercise) }

    // reset user stats when leaving summary
    onExit {
        users.current.score = 0
        users.current.questionsAsked = 0 }
}

// Generates a question using a random percentage between 5 and 95, and a value between 10 and 100
fun getRandomExercise(): ExerciseTuple {
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
        answerTemp.intValueExact()
        return ExerciseTuple(question, percentage, value, answer)
    } catch (e: ArithmeticException) {
        println("answer does not end with a 0, replace question")
        getRandomExercise()
    }
}
