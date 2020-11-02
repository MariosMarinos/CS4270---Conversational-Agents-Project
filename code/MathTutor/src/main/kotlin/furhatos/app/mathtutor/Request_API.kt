package furhatos.app.mathtutor
import khttp.get

val URL = "http://localhost:5000"// API call

fun Request(): String {
    val query = "$URL"
    val emotion = get(query).text
    when(emotion) {
        "Happy", "Neutral" -> return "No act. needed"
        "Fearful", "Surprised", "Sad" -> return "Act. needed"
        else -> return "Unknown Emotion"
    }
}