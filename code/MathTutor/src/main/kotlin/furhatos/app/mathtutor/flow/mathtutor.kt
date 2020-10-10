package furhatos.app.mathtutor.flow

import furhatos.app.mathtutor.nlu.ShowInterest
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
}

val SkillIntro = state(Interaction) {
    onEntry {
        furhat.say("Great, Let's get started then!")
        //goto(Explanation)
    }
}