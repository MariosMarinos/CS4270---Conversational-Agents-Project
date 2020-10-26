package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.nlu.*
import furhatos.flow.kotlin.*


val Start = state(Interaction) {
    onEntry {
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
        goto(Idle)
    }
}


val SkillIntro = state(Interaction) {
    init {
        furhat.say("Great, Let's get started then!")
        goto(Explanation1)
    }
}

val Explanation1: State = state(Interaction) {
    onEntry {
        furhat.ask({
            + "Percentage means 'per hundred'. "
            + delay(100)
            + "You can express any value as broken up into 100 different parts, "
            + "each being 1 percent. We use percentages to make calculations and comparisons between values easier."
            + "Are you with me so far?"
        })
    }

    onResponse<Yes> { goto(ExerciseIntro) }
    onResponse<No> { goto(Explanation2) }
    onResponse<DontUnderstand> { goto(Explanation2) }
}


val Explanation2 = state(Interaction) {
    onEntry {
        furhat.ask("A percentage is nothing more than a ${furhat.voice.emphasis("ratio")} " +
                "expressed as a fraction of 100. Do you understand this so far?")
    }

    onResponse<Yes> { goto(ExplanationCont) }
    onResponse<No> { goto(Encouragement) }
    onResponse<DontUnderstand> { goto(Encouragement) }
}


val Encouragement = state(Interaction) {
    onEntry {
        furhat.say {
            random {
                + "Try to focus hard on the next part. I know you can do it!"
                + "Let's back up a little bit, I'm sure you will see the light."
                + "Alright, don't be frustrated if you don't get it the first time, let's try that again."
            }
        }
        terminate()
    }
}


val ExplanationCont: State = state(Interaction) {
    onEntry {
        furhat.ask("Great! Let's try to compute 20% of 500. " +
                "One method to calculate the percentage is by dividing the value by 100. " +
                "This will give you 1% of the value. Once you have worked out the value of 1%, " +
                "we simply multiply the value by the percentage you're looking for. " +
                "In our example we need to divide our value of 500 by 100. " +
                "This will give 5 as our answer, meaning that 5 is 1% of 500. " +
                "To calculate 20% we multiply 5 by 20 to get 100. " +
                "This means that 100 is the same as 20% of 500. Do you understand this?")
    }

    onReentry {
        furhat.ask("Another method to calculate the percentage is to shift the comma of the percentage two places to the left. For example, 50% will become 0.5 and 1% will become 0.01. We can then multiply our value by this decimal. In our example, we can multiply 500 by 0.20. This will give 100, which is thus 20% of 500. Is this clear?")
    }

    onResponse<Yes> { goto(ExerciseIntro) }
    onResponse<No> { goto(Loop) }
    onResponse<DontUnderstand> { goto(Loop) }
}


// There should be a prettier way to do this...?
val Loop: State = state(Interaction) {
    onEntry {
        goto(ExplanationCont)
    }
}


val ExerciseIntro: State = state(Interaction) {
    onEntry {
        furhat.say("Great! Let's try some exercises to practice your newly acquired skill.")
        goto(AskExercise)
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
    onEntry {
        furhat.say {
            random {
                + "See you around"
                + "Until next time"
                + "Goodbye"
            }
        }
    }
}
