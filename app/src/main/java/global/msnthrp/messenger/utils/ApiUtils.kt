package global.msnthrp.messenger.utils

import global.msnthrp.messenger.db.DbHelper
import global.msnthrp.messenger.extensions.subscribeSmart
import global.msnthrp.messenger.network.ApiService
import global.msnthrp.messenger.storage.Lg
import global.msnthrp.messenger.storage.Prefs
import javax.inject.Inject

/**
 * Created by msnthrp on 27/01/18.
 */
class ApiUtils @Inject constructor(private val api: ApiService,
                                   private val dbHelper: DbHelper,
                                   private val prefs: Prefs) {

    fun updateStickers() {
        if (!prefs.needToUpdateStickers()) return

        api.getStickers()
                .subscribeSmart({
                    dbHelper.db.stickerDao.createOrUpdate(it)
                    prefs.stickerUpdTime = time()
                }, {
                    Lg.wtf("error obtaining stickers: $it")
                })
    }

    fun updatePrime() {
        if (!prefs.needToUpdatePrime()) return

        api.getPrime()
                .subscribeSmart({ prime ->
                    prefs.safePrime = prime
                    prefs.primeUpdTime = time()
                    Lg.i("got prime $prime")
                }, {
                    Lg.wtf("error obtaining prime: $it")
                })
    }

    fun getStickers() = dbHelper.db.stickerDao.getAll()

}