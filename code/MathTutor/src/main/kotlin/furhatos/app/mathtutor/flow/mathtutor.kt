package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.nlu.*
import furhatos.flow.kotlin.*

val Start = state(Interaction) {
    onEntry {
        random(
                { furhat.ask("Hello, how can I help you?") },
                { furhat.ask("Hi there! How can I be of service?") }
        )
    }

    onResponse<ShowInterest> {
        goto(SkillIntro)
    }

    onResponse<Confused> {
        goto(RobotIntro)
    }
}

val RobotIntro = state(Interaction) {
    onEntry {
        furhat.ask("My name is Professor Euler and I can teach you everything you need to know about percentages. Would you like to learn more about percentages?")
    }

    onResponse<Yes> {
        goto(SkillIntro)
    }

    onResponse<No> {
        furhat.say("That's too bad. I'm afraid I can't help you then. Feel free to talk to me when you want to learn about it some other time")
        goto(Idle)
    }
}

val SkillIntro = state(Interaction) {
    onEntry {
        furhat.say("Great, Let's get started then!")
        goto(Explanation1)
    }
}

val Explanation1 : State = state(Interaction) {
    onEntry {
        furhat.ask("Percentage means 'per hundred'. You can express any value as broken up into 100 different parts, each being 1 percent. We use percentages to make calculations and comparisons between values easier. Are you with me so far?")
    }

    onResponse<Yes> {
        //
    }

    onResponse<No> {
        goto(Explanation2)
    }

    onResponse<DontUnderstand> {
        goto(Explanation2)
    }
}

val Explanation2 = state(Interaction) {
    onEntry {
        furhat.ask("A percentage is a ratio expressed as a fraction of 100. Do you understand this so far?")
    }

    onResponse<Yes> {
        //
    }

    onResponse<No> {
        goto(Encouragement)
    }

    onResponse<DontUnderstand> {
        goto(Encouragement)
    }
}

val Encouragement = state(Interaction) {
    onEntry {
        random(
                furhat.say("Try to focus hard on the next part. I know you can do it!"),
                furhat.say("Let's back up a little bit, I'm sure you will see the light."),
                furhat.say("Alright, don't be frustrated if you don't get it the first time, let's try that again.")
                // etc...
        )
        
        goto(Explanation1)
    }
}