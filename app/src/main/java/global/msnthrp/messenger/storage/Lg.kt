package global.msnthrp.messenger.storage

import android.util.Log
import global.msnthrp.messenger.utils.getTime
import global.msnthrp.messenger.utils.time

/**
 * Created by msnthrp on 22/01/18.
 */
object Lg {

    private const val APP_TAG = "haine"
    private const val ALLOWED_LEN = 300
    val logs = arrayListOf<String>()

    fun i(s: String) {
        Log.i(APP_TAG, s)
        logs.add("[${getTime(time())}] $s")
        truncate()
    }

    fun wtf(s: String) {
        Log.wtf(APP_TAG, s)
        logs.add("!![${getTime(time())}]!! $s")
        truncate()
    }

    private fun truncate() {
        while (logs.size > ALLOWED_LEN) {
            logs.removeAt(0)
        }
    }

}