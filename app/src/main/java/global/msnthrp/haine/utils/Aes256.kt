package global.msnthrp.haine.utils

import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException

import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Aes256 {

    private val KEY_LENGTH = 32
    private val IV_LENGTH = 16
    private val CIPHER = "AES/CBC/PKCS5Padding"
    private val AES = "AES"
    private val KEY_LENGTH_EXCEPTION = "AES-256 requires $KEY_LENGTH byte of key"
    private val IV_LENGTH_EXCEPTION = "AES-256 requires $IV_LENGTH byte of IV"

    @Throws(java.io.UnsupportedEncodingException::class,
            NoSuchAlgorithmException::class,
            NoSuchPaddingException::class,
            InvalidKeyException::class,
            InvalidAlgorithmParameterException::class,
            IllegalBlockSizeException::class,
            BadPaddingException::class)
    fun encrypt(ivBytes: ByteArray, keyBytes: ByteArray, textBytes: ByteArray): ByteArray {

        if (keyBytes.size != KEY_LENGTH) {
            throw InvalidKeyException(KEY_LENGTH_EXCEPTION)
        }
        if (ivBytes.size != IV_LENGTH) {
            throw InvalidKeyException(IV_LENGTH_EXCEPTION)
        }
        val ivSpec = IvParameterSpec(ivBytes)
        val newKey = SecretKeySpec(keyBytes, AES)
        val cipher = Cipher.getInstance(CIPHER)
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec)
        return cipher.doFinal(textBytes)
    }

    @Throws(java.io.UnsupportedEncodingException::class,
            NoSuchAlgorithmException::class,
            NoSuchPaddingException::class,
            InvalidKeyException::class,
            InvalidAlgorithmParameterException::class,
            IllegalBlockSizeException::class,
            BadPaddingException::class)
    fun decrypt(ivBytes: ByteArray, keyBytes: ByteArray, textBytes: ByteArray): ByteArray {

        if (keyBytes.size != KEY_LENGTH) {
            throw InvalidKeyException(KEY_LENGTH_EXCEPTION)
        }
        if (ivBytes.size != IV_LENGTH) {
            throw InvalidKeyException(IV_LENGTH_EXCEPTION)
        }
        val ivSpec = IvParameterSpec(ivBytes)
        val newKey = SecretKeySpec(keyBytes, AES)
        val cipher = Cipher.getInstance(CIPHER)
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec)
        return cipher.doFinal(textBytes)
    }
}
