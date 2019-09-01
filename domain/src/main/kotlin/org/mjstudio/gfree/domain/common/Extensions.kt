package org.mjstudio.gfree.domain.common

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.mjstudio.gfree.domain.BuildConfig

fun CoroutineScope.simpleTimer(milliseconds: Long, completion: (t: Long) -> Unit) = launch{
    delay(milliseconds)
    completion(milliseconds)
}


//endregion
fun debugE(tag: String, message: Any?) {
    if (BuildConfig.DEBUG)
        Log.e(tag, "\uD83C\uDF40" + message.toString())
}
fun debugE(message: Any?) {

    debugE("DEBUG", message)
}