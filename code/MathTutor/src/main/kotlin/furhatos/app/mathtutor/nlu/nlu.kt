package furhatos.app.mathtutor.nlu

import furhatos.nlu.*
import furhatos.util.Language

class ShowInterest: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "I would like to learn more about fractions",
                "Can you teach me how to fraction?")
    }
}