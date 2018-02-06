package global.msnthrp.messenger.utils

import global.msnthrp.messenger.db.DbHelper
import global.msnthrp.messenger.extensions.subscribeSmart
import global.msnthrp.messenger.model.ExchangeParams
import global.msnthrp.messenger.network.ApiService
import global.msnthrp.messenger.storage.Lg
import global.msnthrp.messenger.storage.Prefs
import java.math.BigInteger
import java.security.SecureRandom
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

    fun createExchange(userId: Int, callback: (Boolean) -> Unit) {
        val p = BigInteger(prefs.safePrime)
        val q = getSmallPrime(p)
        val g = getRandomPrimeRoot(p, q)

        val privateOwn = BigInteger(DH_BITS, SecureRandom())
        val publicOwn = g.modPow(privateOwn, p)

        val expParams = ExchangeParams(
                userId,
                p.toString(),
                g.toString(),
                privateOwn.toString(),
                publicOwn.toString()
        )

        api.commitExchange(
                expParams.p, expParams.g,
                expParams.publicOwn, userId
        )
                .subscribeSmart({
                    dbHelper.db.exchangeDao.createOrUpdate(expParams)
                    callback.invoke(true)
                }, {
                    Lg.wtf("error creating exchange: $it")
                    callback.invoke(false)
                })
    }

    fun getStickers() = dbHelper.db.stickerDao.getAll()

}