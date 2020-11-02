package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.Request
import furhatos.app.mathtutor.nlu.DontUnderstand
import furhatos.app.mathtutor.nlu.No
import furhatos.app.mathtutor.nlu.Repeat
import furhatos.app.mathtutor.nlu.Yes
import furhatos.flow.kotlin.*
import furhatos.nlu.common.DontKnow
import furhatos.records.Location
import java.util.*

// TODO: add conditional for division / percentage explanation to each state in this file
// TODO: add a user counter for how many times things has been repeated. Switch to different explanation if repeated too much
// TODO: make the robot speak slower, (if i was the student it would be talking too fast imo, idk if this is something we can change in our code, or in the furhat sdk server)

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
//    val x = 6.0 * r1 - 3
//    val y = 3.5 * r2 - 1.7
//    val z = 1.5
    println(""+r1+ ", " +r2+ ", 1.5")
    return Location(r1, r2, 1.5)
}

val Explanation1: State = state(Interaction) {
    onEntry {
        furhat.attend(randomLookAway())
        delay(2000)
        val response = Request()
        furhat.say {
            +"Percentage means 'per hundred'."
            +behavior {
                furhat.attend(users.current)
            }
            +"We use percentages to make calculations and comparisons between values easier."
            +delay(100)
            +"You can express any value as broken up into 100 different parts, each part being 1%."
            +behavior {
                furhat.attend(Location(0.0, -0.5, 1.5))
            }
            +delay(250)
            +"Once you have worked out the value of 1%, "
            +"we simply multiply this value by the percentage you're looking for to get the final answer. "
            +behavior {
                furhat.attend(users.current)
            }
        }
        furhat.ask("Are you with me so far?")
    }
    onResponse<Yes> {
        val emotion = Request()
        if (emotion == "Act. needed") call(EmotionSeenEncouragment)
        furhat.say("Let's work through an example together")
        goto(Explanation1Example) }
    onResponse<Repeat> {
        val emotion = Request()
        if (emotion == "Act. needed") call(EmotionSeenEncouragment)
        furhat.say("Okay now, I will repeat my explanation again.")
        reentry() }
    onResponse<No> {
        call(Encouragement)
        reentry() }
    onResponse<DontKnow> {
        call(Encouragement)
        reentry() }
    onResponse<DontUnderstand> {
        val emotion = Request()
        if (emotion == "Act. needed") call(EmotionSeenEncouragment)
        furhat.say("Let's try another explanation.")
        goto(Explanation2) }
}


val Explanation2 = state(Interaction) {
    onEntry {
        furhat.attend(randomLookAway())
        delay(1000)
        furhat.say{
                +"A percentage is nothing more than a ratio"
                + behavior {
                    furhat.attend(users.current) }
                + "expressed as a fraction of 100. So we could also write 100% as 100/100, 50% as 50/100, 1% as 1/100, "
                +"et cetera."}
        furhat.ask("Do you understand this so far?")
    }

    onResponse<Yes> {
        furhat.say("Okay. Let me show you an example.")
        goto(Explanation2Example) }
    onResponse<Repeat> {
        furhat.say("Okay, I will repeat my explanation again.")
        reentry() }
    onResponse<No> {
        call(Encouragement)
        reentry()}
    onResponse<DontUnderstand> {
        // TODO: Explanation loop must be handled more gracefully
        furhat.say("Let's try the first explanation again.")
        goto(Explanation1Example) }
}

val Explanation1Example: State = state(Interaction) {
    onEntry {
        furhat.attend(randomLookAway())
        delay(2000)
        furhat.say {
            + "Let's try to compute 20% of 500."
            + behavior {
                furhat.attend(users.current) }
            + "We will start by calculating 1% of 500."
            + "In our example we need to divide 500 by 100 to get 1%. "
            + "This will give us 5. "
            + delay(250)
            + "Remember that this is only 1%, and we would like to know 20% of 500."
            + "So we multiply 5 by 20 to reach our final answer, which is 100."}
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
        furhat.say("Let's try a different explanation.")
        goto(Explanation2) }
}

val Explanation2Example : State = state(Interaction) {
    onEntry {
        furhat.attend(randomLookAway())
        delay(2000)
        furhat.say{"Let's try to compute 20% of 500."
                + behavior {
                    furhat.attend(users.current)
                }
                + "We start by writing 20% as 20/100. "
                + "We can then multiply our value by this fraction to reach our final answer. "
                + "In our example, we can multiply 500 by 20/100. This will give 100, which is thus our final answer."}
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
        furhat.say("Let's try a different explanation.")
        goto(Explanation1)
    }
}
