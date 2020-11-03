package furhatos.app.mathtutor
import khttp.get


val URL = "http://localhost:5000"// API call

fun Request(): String {
    val query = "$URL"
    return get(query).text
}