package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.Request
import furhatos.app.mathtutor.nlu.DontUnderstand
import furhatos.app.mathtutor.nlu.No
import furhatos.app.mathtutor.nlu.Repeat
import furhatos.app.mathtutor.nlu.Yes
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.DontKnow
import furhatos.records.Location


// function to generate a random location for the robot to look to when "A-gazing" (not looking at the user).
fun randomLookAway() : Location {
    var r1 = 1.0
    var r2 = 1.0
    if ((0..1).random() == 1) {
        r1 = -1.0
    }
    if ((0..1).random() == 1) {
        r2 = -1.0
    }
    return Location(r1, r2, 1.5)
}

fun callEmotion(): String {
    val emotion = Request()
    EmotionSeenEncouragment(emotion)
    return emotion
}

val Explanation1: State = state(Interaction) {
    onEntry {
        furhat.attend(randomLookAway())
        furhat.voice.rate = 0.7
        delay(1000)
        val emotion = Request()
        if (emotion == "Surprised") EmotionSeenEncouragment(emotion)
        furhat.say {
            +"Percentage means 'per hundred'."
            +behavior {
                furhat.attend(Location(0.0, -0.3, 1.5))
            }
            +"We use percentages to make calculations and comparisons between values easier."
            +delay(100)
            +"You can express any value as broken up into 100 different parts, each part being 1%."
            +behavior {
                furhat.gesture(Gestures.GazeAway)
            }
            +delay(250)
            +"Once you have worked out the value of 1%, "
            +"we simply multiply this value by the percentage you're looking for to get the final answer. "
            +behavior {
                furhat.attend(users.current)
            }
        }
        furhat.voice.rate = 0.9
        furhat.ask("Are you with me so far?")
    }
    onResponse<Yes> {
        furhat.say("Nice, you are on the right track! Let's work through an example together.")
        goto(Explanation1Example) }
    onResponse<Repeat> {
        callEmotion()
        furhat.say("Okay now, I will repeat my explanation again.")
        reentry() }
    onResponse<No> {
        // TODO : on No, and Don't understand, call something that is for sure encouraging or
        // TODO : CallEmotion which risk to find happy or neutral and don't encourage eventually?
        call(Encouragement)
        reentry() }
    onResponse<DontKnow> {
        val emotion = callEmotion()
        // if he answers Don't know but his emotion are good go to explanation and give him a good comment.
        if (emotion == "Happy" || emotion == "Neutral")  {
            EmotionSeenEncouragment(emotion)
            goto(Explanation1Example)
        }
        reentry()
    }
    onResponse<DontUnderstand> {
        val emotion = Request()
        if (emotion == "Happy"){
            EmotionSeenEncouragment(emotion)
            goto(Explanation1Example)
        }
        furhat.say("Let's try another explanation.")
        goto(Explanation2) }
}


val Explanation2 = state(Interaction) {
    onEntry {
        furhat.attend(randomLookAway())
        // if before explanation user is surprised, tell him to wait to see.
        val emotion = Request()
        if (emotion == "Surprised") EmotionSeenEncouragment(emotion)
        furhat.voice.rate = 0.7
        delay(1000)
        furhat.say{
                +"A percentage is nothing more than a ratio"
                + behavior {
                    furhat.attend(Location(0.0, -0.3, 1.5)) }
                + "expressed as a fraction of 100. So we could also write 100% as 100/100, 50% as 50/100, 1% as 1/100, "
                +"et cetera."
                + behavior{furhat.attend(users.current)}
        }
        furhat.voice.rate = 0.9
        furhat.ask("Do you understand this so far?")
    }

    onResponse<Yes> {
        furhat.say("Well done so far! Let's move on and show you an example.")
        goto(Explanation2Example) }
    onResponse<Repeat> {
        callEmotion()
        furhat.say("Okay, I will repeat my explanation again.")
        reentry() }
    onResponse<No> {
        call(Encouragement)
        reentry()}
    onResponse<DontUnderstand> {
        // TODO: Explanation loop must be handled more gracefully
        val emotion = Request()
        if (emotion == "Happy"){
            EmotionSeenEncouragment(emotion)
            goto(Explanation2Example)
        }
        furhat.say("Let's try the first explanation again.")
        goto(Explanation1) }
}

val Explanation1Example: State = state(Interaction) {
    onEntry {
        furhat.attend(randomLookAway())
        delay(2000)
        furhat.say {
            + "Let's try to compute 20% of 500."
            + behavior {
                furhat.attend(Location(0.0, -0.3, 1.5))
                furhat.voice.rate = 0.7 }
            + "We will start by calculating 1% of 500."
            + "In our example we need to divide 500 by 100 to get 1%. "
            + "This will give us 5. "
            + behavior { furhat.gesture(Gestures.GazeAway) }
            + delay(500)
            + "Remember that this is only 1%, and we would like to know 20% of 500."
            + "So we multiply 5 by 20 to reach our final answer, which is 100."
            + behavior { furhat.attend(users.current) }
        }
        furhat.voice.rate = 0.9
        furhat.ask("Do you understand this?")
    }
    onResponse<Yes> { goto(ExerciseIntro) }
    onResponse<Repeat> {
        furhat.say("Sure!")
        reentry() }
    onResponse<No> {
        call(Encouragement)
        reentry() }
    onResponse<DontUnderstand> {
        val emotion = Request()
        if (emotion == "Happy"){
            EmotionSeenEncouragment(emotion)
            goto(Explanation2Example)
        }
        furhat.say("Let's try a different explanation.")
        goto(Explanation2) }
}

val Explanation2Example : State = state(Interaction) {
    onEntry {
        furhat.attend(randomLookAway())
        delay(2000)
        furhat.say{
            + "Let's try to compute 20% of 500."
            + behavior {
                furhat.attend(Location(0.0, -0.3, 1.5))
                furhat.voice.rate = 0.7
            }
            + "We start by writing 20% as 20/100. "
            + behavior { furhat.gesture(Gestures.GazeAway) }
            + delay(500)
            + "We can then multiply our value by this fraction to reach our final answer. "
            + "In our example, we can multiply 500 by 20/100. This will give 100, which is thus our final answer."
            + behavior { furhat.attend(users.current) }
        }
        furhat.voice.rate = 0.9
        furhat.ask("Is this clear?")
    }

    onResponse<Yes> { goto(ExerciseIntro) }
    onResponse<Repeat> {
        furhat.say("Sure!")
        reentry() }
    onResponse<No> {
        call(Encouragement)
        reentry() }
    onResponse<DontUnderstand> {
        val emotion = Request()
        if (emotion == "Happy"){
            EmotionSeenEncouragment(emotion)
            goto(ExerciseIntro)
        }
        furhat.say("Let's try a different explanation.")
        goto(Explanation1)
    }
}
