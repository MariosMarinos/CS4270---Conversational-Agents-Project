package furhatos.app.mathtutor.nlu

import furhatos.nlu.*
import furhatos.util.Language

class ShowInterest : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "I would like to learn more about percentages",
                "Can you teach me how to calculate percentages?")
    }
}

class Confused : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "Who are you?",
                "What can you do?",
                "What is this?")
    }
}

class Yes : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "Yes",
                "Sure",
                "Yeah",
                "Yes, please")
    }
}

class No : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "No",
                "No thanks",
                "Nope",
                "I'm good")
    }
}

class DontUnderstand : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "Huh?",
                "What?",
                "I don't understand",
                "I'm lost")
    }
}