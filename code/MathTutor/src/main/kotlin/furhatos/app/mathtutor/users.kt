package furhatos.app.mathtutor
import furhatos.flow.kotlin.Furhat
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


val User.mathskill : UserData
    get() = data.getOrPut(UserData::class.qualifiedName, UserData())
