package global.msnthrp.messenger.utils


/**
 * Created by twoeightnine on 2/7/18.
 */
class Cryptool(private val shared: String) {

    private var aesIv = md5Raw(shared.toByteArray())
    private var aesKey = sha256Raw(shared.toByteArray())

    fun getFingerPrint() = getUiFriendlyHash(sha256("${bytesToHex(aesIv)}${bytesToHex(aesKey)}"))

    fun encrypt(plain: String) = toBase64(Aes256.encrypt(aesIv, aesKey, plain.toByteArray()))

    fun decrypt(plain: String) = Aes256.decrypt(aesIv, aesKey, fromBase64(plain))

    fun isSharedOther(otherShared: String) = shared != otherShared

}