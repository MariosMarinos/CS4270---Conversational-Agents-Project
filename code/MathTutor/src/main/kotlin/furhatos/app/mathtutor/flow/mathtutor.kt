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

val Explanation1: State = state(Interaction) {
    onEntry {
        furhat.ask("Percentage means 'per hundred'. You can express any value as broken up into 100 different parts, each being 1 percent. We use percentages to make calculations and comparisons between values easier. Are you with me so far?")
    }

    onResponse<Yes> {
        ExplanationCont
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
        goto(ExplanationCont)
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

val ExplanationCont : State = state(Interaction) {
    onEntry {
        furhat.ask("Great! Let's try to compute 20% of 500. One method to calculate the percentage is by dividing the value by 100. This will give you 1% of the value. Once you have worked out the value of 1%, we simply multiply the value by the percentage you're looking for. In our example we need to divide our value of 500 by 100. This will give 5 as our answer, meaning that 5 is 1% of 500. To calculate 20% we multiply 5 by 20 to get 100. This means that 100 is the same as 20% of 500. Do you understand this?")
    }

    onReentry {
        furhat.ask("Another method to calculate the percentage is to shift the comma of the percentage two places to the left. For example, 50% will become 0.5 and 1% will become 0.01. We can then multiply our value by this decimal. In our example, we can multiply 500 by 0.20. This will give 100, which is thus 20% of 500. Is this clear?")
    }

    onResponse<Yes> {
        goto(Exercise)
    }

    onResponse<No> {
        goto(Loop)
    }

    onResponse<DontUnderstand> {
        goto(Loop)
    }
}

// There should be a prettier way to do this...?
val Loop : State = state(Interaction) {
    onEntry{
        goto(ExplanationCont)
    }
}

val ExerciseIntro : State = state(Interaction) {
    onEntry{
        furhat.say("I'm proud of you! Let's try some exercises to practice your newly acquired skill.")
        //goto(Exercise)
    }
}