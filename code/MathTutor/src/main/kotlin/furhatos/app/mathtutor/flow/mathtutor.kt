package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.nlu.*
import furhatos.flow.kotlin.*
import furhatos.records.Location


val Start = state(Interaction) {
    onEntry {
        furhat.gesture(indefiniteSmile)

        furhat.ask {
            random {
                + "Hello, how can I help you?"
                + "Hi there! How can I be of service?"
            }
            + behavior {
                furhat.attend(users.current) }
        }
    }

    onResponse<ShowInterest> { goto(SkillIntro) }
    onResponse<Confused> { goto(RobotIntro) }
}


val RobotIntro = state(Interaction) {
    onEntry {
        furhat.ask("My name is Professor Euler and I can teach you everything you need to know about percentages. " +
                "Would you like to learn more about percentages?")
    }

    onResponse<Yes> { goto(SkillIntro) }
    onResponse<No> {
        furhat.say("That's too bad. I'm afraid I can't help you then. " +
                "Feel free to talk to me when you want to learn about it some other time")
        goto(Goodbye)
    }
}


val SkillIntro = state(Interaction) {
    onEntry {
        furhat.say("Great, Let's get started then!")
        goto(Explanation1)
    }
}



val Encouragement = state(Interaction) {
    onEntry {
        furhat.say {
            random {
                + "Try to focus hard on the next part. I know you can do it!"
                + "Let's back up a little bit, I'm sure you will see the light."
                + "Alright, don't be frustrated if you don't get it the first time, let's try that again."
                + "Don't give up, keep up the good work and you will bright!"
            }
        }
        terminate()
    }
}

val EmotionSeenEncouragment = state(Interaction) {
    onEntry {
        furhat.say {
            random {
                + "I can see in your face that you might be disappointed, but don't worry you will get it eventually!"
                + "Hey, don't be pessimistic, I can see that on your face. Let's move on."
            }
        }
        terminate()
    }
}


// Ask user if he wants to stop
val checkStop : State = state(Interaction) {
    onEntry {
        furhat.ask("Are you sure you want to stop?")
    }
    onResponse<Yes> { goto(ExerciseSummary) }
    onResponse<No> { terminate() }
}


val requestBreak : State = state(Interaction) {
    onEntry {
        furhat.ask("Why don't you take a break for today?")
    }
    onResponse<Yes> { goto(ExerciseSummary) }
    onResponse<Stop> { goto(ExerciseSummary) }
    onResponse<No> {
        furhat.say("Okay, just let me know if you want to stop.")
        terminate() }
}


val Goodbye : State = state(Interaction) {
    // TODO: wave user goodbye
    onEntry {
        furhat.gesture(indefiniteBigSmile)
        furhat.say {
            random {
                + "See you around"
                + "Until next time"
                + "Goodbye"
            }
        }
        delay(4000)
        furhat.gesture(stopSmile)
        goto(Idle)
    }
}
