package global.msnthrp.messenger.storage

import android.content.Context
import android.content.SharedPreferences
import global.msnthrp.messenger.utils.time
import javax.inject.Inject

/**
 * Created by msnthrp on 27/01/18.
 */
class Prefs @Inject constructor(private val context: Context) {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    var stickerUpdTimer
        get() = prefs.getInt(STICKERS_UPD, 0)
        set(value) = prefs.edit().putInt(STICKERS_UPD, value).apply()

    fun needToUpdateStickers() = time() - stickerUpdTimer > STICKERS_UPD_DELAY

    companion object {
        const val NAME = "prefs"
        const val STICKERS_UPD_DELAY = 60 * 60 * 24

        const val STICKERS_UPD = "stickersUpd"
    }
}