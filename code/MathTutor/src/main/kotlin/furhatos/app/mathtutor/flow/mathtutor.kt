package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.nlu.*
import furhatos.flow.kotlin.*
import furhatos.records.Location


val Start = state(Interaction) {
    onEntry {
        furhat.gesture(indefiniteSmile)
        goto(Explanation1)
        furhat.ask {
            random {
                + "Hello, how can I help you?"
                + "Hi there! How can I be of service?"
            }
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

fun EmotionSeenEncouragment(emotion: String)  = state {
    onEntry {
        when (emotion){
            "Angry" -> furhat.say{random {
                    + "Don't waste your time in anger. Life is too short to be angry. Let's keep up the good work!"
                    + "You don't need to be mad at me, we will work through this together and you will understand everything about percentages!"
            }}
            "Disgusted" -> furhat.say("I can see that you don't like percentages but you will see that it's very exciting eventually!")
            "Fearful" -> furhat.say{random{
                + "I can see the fear in your face, but we will work this out together."
                + "There is no need to be feared, with hard work you are will make it!"
                + "Alright, don't be frustrated if you don't get it the first time, let's try that again."
                + "Let's back up a little bit, I'm sure you will see the light."
            }}
            "Happy" -> furhat.say{random {
                +"Great, I can see that you are happy with what you've learned so far!"
                +"Nice! You are doing pretty well. Keep it up!"
            }}
            "Sad" -> furhat.say{random{
                + "Hey, don't be sad, try to focus hard on the next part. I know you can do it!"
                + "Don't take that personally if you can't understand the maths within first time, I know you can bright!"
                + "I can see that you are sad with this. Many people struggle with maths so don't worry about it, keep up the good work and you got this!"
                + "Let's back up a little bit, I'm sure you will see the light."
            }}
            "Surprised" -> furhat.say{random{
                + "I know that you are surprised about percentages,it's rational as you see it for first time."
                + "Oh, I can see you are surprised. Be patient for my explanation and you will get it."
            }}
            // TODO : If Neutral say what? Also, neutral going to exist a lot of times because
            // TODO : when we don't recognize faces we put neutral.
            "Neutral" -> {terminate()}
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
