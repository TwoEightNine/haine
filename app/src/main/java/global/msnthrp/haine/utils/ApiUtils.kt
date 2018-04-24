package global.msnthrp.haine.utils

import android.os.Environment
import global.msnthrp.haine.db.DbHelper
import global.msnthrp.haine.extensions.subscribeSmart
import global.msnthrp.haine.model.ExchangeParams
import global.msnthrp.haine.network.ApiService
import global.msnthrp.haine.storage.Lg
import global.msnthrp.haine.storage.Prefs
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
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
        api.getStickers()
                .subscribeSmart({
                    prefs.stickersQuantity = it
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

    fun uploadFile(path: String,
                   onSuccess: (String) -> Unit = {},
                   onError: (String) -> Unit = {}) {
        val file = File(path)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        api.uploadFile(body)
                .compose(applySchedulersSingle())
                .subscribe({ response ->
                    if (response != null) {
                        onSuccess.invoke(response.link)
                    } else {
                        onError.invoke("null")
                    }
                }, { error ->
                    onError.invoke(error.message ?: "")
                })
    }

    fun downloadFile(link: String,
                     onSuccess: (String) -> Unit = {},
                     onError: (String) -> Unit = {}) {
        val dir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                DOWNLOADS_DIRECTORY
        )
        dir.mkdir()
        val file = File(dir, getNameFromUrl(link)).absolutePath
        api.downloadFile(link)
                .compose(applySchedulersSingle())
                .subscribe({ response ->
                    val written = writeResponseBodyToDisk(response, file)
                    if (written) {
                        onSuccess.invoke(file)
                    } else {
                        onError.invoke("Downloaded but not written")
                    }
                }, { error ->
                    onError.invoke(error?.message ?: "null")
                })
    }

    companion object {
        const val DOWNLOADS_DIRECTORY = "/haine"
    }

}