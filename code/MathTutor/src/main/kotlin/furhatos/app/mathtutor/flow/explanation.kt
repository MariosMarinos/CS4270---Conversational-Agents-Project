package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.nlu.DontUnderstand
import furhatos.app.mathtutor.nlu.No
import furhatos.app.mathtutor.nlu.Repeat
import furhatos.app.mathtutor.nlu.Yes
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.onResponse
import furhatos.flow.kotlin.state
import furhatos.nlu.common.DontKnow

// TODO: add conditional for division / percentage explanation to each state in this file
// TODO: add a user counter for how many times things has been repeated. Switch to different explanation if repeated too much
val Explanation1: State = state(Interaction) {
    onEntry {
        furhat.say {
            +"Percentage means 'per hundred'. "
            +"We use percentages to make calculations and comparisons between values easier."
            +delay(100)
            +"You can express any value as broken up into 100 different parts, each part being 1%."
            +delay(250)
            +"Once you have worked out the value of 1%, "
            +"we simply multiply this value by the percentage you're looking for to get the final answer. "
        }
        furhat.ask("Are you with me so far?")
    }

    onResponse<Yes> {
        furhat.say("Let's work through an example together")
        goto(Explanation1Example) }
    onResponse<Repeat> {
        furhat.say("Okay, I will repeat my explanation again.")
        reentry() }
    onResponse<No> {
        call(Encouragement)
        reentry() }
    onResponse<DontKnow> {
        call(Encouragement)
        reentry() }
    onResponse<DontUnderstand> {
        furhat.say("Let's try another explanation.")
        goto(Explanation2) }
}


val Explanation2 = state(Interaction) {
    onEntry {
        furhat.ask("A percentage is nothing more than a ratio" +
                "expressed as a fraction of 100. So we could also write 100% as 100/100, 50% as 50/100, 1% as 1/100, " +
                "et cetera. Do you understand this so far?")
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
        furhat.say {
            + "Let's try to compute 20% of 500."
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
        furhat.say("Let's try to compute 20% of 500. We start by writing 20% as 20/100. " +
                "We can then multiply our value by this fraction to reach our final answer. " +
                "In our example, we can multiply 500 by 20/100. This will give 100, which is thus our final answer.")
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
