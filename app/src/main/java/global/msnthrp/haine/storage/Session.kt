package global.msnthrp.haine.storage

import android.content.Context
import javax.inject.Inject

/**
 * Created by msnthrp on 22/01/18.
 */
class Session @Inject constructor(private val context: Context) {

    private val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var token
        get() = prefs.getString(TOKEN, "")
        set(value) = prefs.edit().putString(TOKEN, value).apply()

    var userId
        get() = prefs.getInt(UID, 0)
        set(value) = prefs.edit().putInt(UID, value).apply()

    var lastMessage
        get() = prefs.getInt(LAST_MESSAGE, 0)
        set(value) = prefs.edit().putInt(LAST_MESSAGE, value).apply()

    var lastRead
        get() = prefs.getInt(LAST_READ, 0)
        set(value) = prefs.edit().putInt(LAST_READ, value).apply()

    var lastXchg
        get() = prefs.getLong(LAST_XCHG, 0)
        set(value) = prefs.edit().putLong(LAST_XCHG, value).apply()

    fun reset() {
        prefs.edit().clear().apply()
    }

    companion object {
        const val PREF_NAME = "sessionPrefs"

        const val TOKEN = "token"
        const val UID = "userId"
        const val LAST_MESSAGE = "lastMessage"
        const val LAST_READ = "lastRead"
        const val LAST_XCHG = "lastExchange"
    }

}