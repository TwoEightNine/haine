package global.msnthrp.messenger.storage

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

    companion object {
        val PREF_NAME = "sessionPrefs"

        val TOKEN = "token"
        val UID = "userId"
    }

}