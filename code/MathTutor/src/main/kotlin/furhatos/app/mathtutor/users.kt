package furhatos.app.mathtutor
import furhatos.flow.kotlin.Furhat
import furhatos.flow.kotlin.NullSafeUserDataDelegate
import furhatos.records.Record
import furhatos.records.User

// User variables
class UserData(
        var answeringExercise : Boolean = false,
        var correctCounter : Int = 0,
//        var difficulty : Int = 0, TODO: implement difficulty
        var score : Int = 0,
        var lastScore : Int = 0

) : Record()

var User.isAnsweringExercise by NullSafeUserDataDelegate { false }
var User.score by NullSafeUserDataDelegate { 0 }

val User.mathskill : UserData
    get() = data.getOrPut(UserData::class.qualifiedName, UserData())
