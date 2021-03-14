package unique.damian.androidapp.Others

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.util.*

fun getJson(): Gson {
    return  GsonBuilder().create()
}
fun uniqueId():String = UUID.randomUUID().toString()