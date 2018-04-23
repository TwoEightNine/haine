package global.msnthrp.haine.storage

import android.content.Context
import android.content.SharedPreferences
import global.msnthrp.haine.utils.time
import javax.inject.Inject

/**
 * Created by msnthrp on 27/01/18.
 */
class Prefs @Inject constructor(private val context: Context) {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    var showNotifications
        get() = prefs.getBoolean(SHOW_NOTIFICATIONS, true)
        set(value) = prefs.edit().putBoolean(SHOW_NOTIFICATIONS, value).apply()

    var showName
        get() = prefs.getBoolean(SHOW_NAME, false)
        set(value) = prefs.edit().putBoolean(SHOW_NAME, value).apply()

    var vibrate
        get() = prefs.getBoolean(VIBRATE, true)
        set(value) = prefs.edit().putBoolean(VIBRATE, value).apply()

    var soundNotifications
        get() = prefs.getBoolean(SOUND_NOTIFICATIONS, false)
        set(value) = prefs.edit().putBoolean(SOUND_NOTIFICATIONS, value).apply()

    var stickerUpdTime
        get() = prefs.getInt(STICKERS_UPD, 0)
        set(value) = prefs.edit().putInt(STICKERS_UPD, value).apply()

    var stickersQuantity
        get() = prefs.getInt(STICKERS_QTY, 0)
        set(value) = prefs.edit().putInt(STICKERS_QTY, value).apply()

    var primeUpdTime
        get() = prefs.getInt(PRIMES_UPD, 0)
        set(value) = prefs.edit().putInt(PRIMES_UPD, value).apply()

    var safePrime
        get() = prefs.getString(SAFE_PRIME, DEFAULT_PRIME)
        set(value) = prefs.edit().putString(SAFE_PRIME, value).apply()

    fun needToUpdateStickers() = time() - stickerUpdTime > STICKERS_UPD_DELAY

    fun needToUpdatePrime() = time() - primeUpdTime > PRIMES_UPD_DELAY

    fun reset() {
        prefs.edit().clear().apply()
    }

    companion object {
        const val NAME = "prefs"
        const val STICKERS_UPD_DELAY = 60 * 60 * 24
        const val PRIMES_UPD_DELAY = 60 * 60 * 24 * 30

        const val SHOW_NOTIFICATIONS = "showNotifications"
        const val SHOW_NAME = "showName"
        const val VIBRATE = "vibrate"
        const val SOUND_NOTIFICATIONS = "soundNotifications"

        const val STICKERS_UPD = "stickersUpd"
        const val STICKERS_QTY = "stickersQty"
        const val PRIMES_UPD = "primesUpd"
        const val SAFE_PRIME = "safePrime"

        const val DEFAULT_PRIME = "4299608458730885365997381468493988901976562819787460522603026" +
                "4746629091236330599666549875318212612031829511024440396464342648691577991833890" +
                "5631403871028184935255084712703423613529366297760543627384218281702559557613931" +
                "2309810252147014362928433748825470504108987492740175613809618646419868229349740" +
                "9454662570337393410558180415100006913617169493379962859674636074408998785963274" +
                "4266134003621159571422862980144043377717793249604329463406248840895370685965329" +
                "7215129355127048155109988683685972850470229107316790480107562863961081285040109" +
                "7540434467292419182764310323486917373365274421751734483875743936086632531541480503"
    }
}