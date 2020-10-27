package furhatos.app.mathtutor.nlu

import furhatos.nlu.*
import furhatos.util.Language

class ShowInterest : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "I would like to learn more about percentages",
                "Can you teach me how to calculate percentages?",
                "percentages")
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
                "Okay",
                "Yes, please")
    }
}

class No : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "No",
                "No thanks",
                "Nope",
                "I'm good",
                "I want to continue",
                "Continue")
    }
}

class DontUnderstand : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "Huh?",
                "What?",
                "I don't understand",
                "I'm confused",
                "I'm lost")
    }
}

class Help : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "I need some help",
                "I want some help",
                "Can I get some help",
                "Help",
                "Can you explain it to me?",
                "Can I get an explanation?",
                "Explanation")
    }
}

// This intent is used when users have enough of exercising,
class Stop : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "I'm done",
                "I want to quit",
                "I've had enough",
                "I've practised enough",
                "Let's take a break",
                "I need a break",
                "I want a break",
                "Stop")
    }
}

class Repeat : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
                "Could you repeat that?",
                "Repeat",
                "Explain again")
    }
}
