package global.msnthrp.messenger.utils

import io.reactivex.Flowable


/**
 * Created by twoeightnine on 2/7/18.
 */
class Cryptool(private val shared: String) {

    private val aesIv = md5Raw(shared.toByteArray())
    private val aesKey = sha256Raw(shared.toByteArray())

    fun getFingerPrint() = sha256("${bytesToHex(aesIv)}${bytesToHex(aesKey)}")

    fun encrypt(plain: String) = toBase64(Aes256.encrypt(aesIv, aesKey, plain.toByteArray()))

    fun decrypt(plain: String)
        = try {
            String(Aes256.decrypt(aesIv, aesKey, fromBase64(plain)))
        } catch (e: Exception) {
            plain
        }

    fun isSharedOther(otherShared: String) = shared != otherShared

    fun encryptFile(path: String, callback: (String) -> Unit = {}) {
        val bytes = getBytesFromFile(path)
        val resultName = getNameFromUrl(path) + ENC_POSTFIX
        Flowable.fromCallable {
            writeBytesToFile(
                    Aes256.encrypt(aesIv, aesKey, bytes),
                    resultName
            )
        }
                .compose(applySchedulersFlowable())
                .subscribe(callback)
    }

    fun decryptFile(path: String, callback: (String) -> Unit = {}) {
        val bytes = getBytesFromFile(path)
        val resultName = getNameFromUrl(path) + DEC_POSTFIX
        Flowable.fromCallable {
            try {
                writeBytesToFile(
                        Aes256.encrypt(aesIv, aesKey, bytes),
                        resultName
                )
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
                .compose(applySchedulersFlowable())
                .subscribe(callback)
    }

    companion object {
        val ENC_POSTFIX = ".enc"
        val DEC_POSTFIX = ".dec"
    }

}