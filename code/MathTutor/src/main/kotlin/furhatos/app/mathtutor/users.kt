package furhatos.app.mathtutor
import furhatos.flow.kotlin.Furhat
import furhatos.flow.kotlin.NullSafeUserDataDelegate
import furhatos.records.Record
import furhatos.records.User


var User.isAnsweringExercise by NullSafeUserDataDelegate { false }
var User.score by NullSafeUserDataDelegate { 0 }
var User.questionsAsked by NullSafeUserDataDelegate { 0 }
