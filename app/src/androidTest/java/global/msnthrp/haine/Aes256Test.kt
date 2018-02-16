package global.msnthrp.haine

import android.support.test.runner.AndroidJUnit4
import global.msnthrp.haine.utils.Aes256
import org.junit.Test
import org.junit.runner.RunWith
import java.security.InvalidKeyException
import java.util.*

/**
 * Created by twoeightnine on 2/9/18.
 */

@RunWith(AndroidJUnit4::class)
class Aes256Test {

    @Test(expected = InvalidKeyException::class)
    fun encrypting_wrongIv() {
        Aes256.encrypt(IV_INVALID, KEY_VALID, TEXT)
    }

    @Test(expected = InvalidKeyException::class)
    fun encrypting_wrongKey() {
        Aes256.encrypt(IV_VALID, KEY_INVALID, TEXT)
    }

    @Test
    fun encrypting_isCorrect() {
        val enc = Aes256.encrypt(IV_VALID, KEY_VALID, TEXT)
        val dec = Aes256.decrypt(IV_VALID, KEY_VALID, enc)
        assert(Arrays.equals(dec, TEXT))
    }

    @Test(expected = InvalidKeyException::class)
    fun decrypting_wrongIv() {
        Aes256.decrypt(IV_INVALID, KEY_VALID, TEXT)
    }

    @Test(expected = InvalidKeyException::class)
    fun decrypting_wrongKey() {
        Aes256.decrypt(IV_VALID, KEY_INVALID, TEXT)
    }

    companion object {
        val KEY_VALID = "12345678901234567890123456789012".toByteArray()
        val KEY_INVALID = "1234567890123456".toByteArray()

        val IV_VALID = "1234567890123456".toByteArray()
        val IV_INVALID = "123456789012345".toByteArray()

        val TEXT = "urinucercqw wuwpcrvw vw9r4vc4wrtcvw 4tvw4uctvj 4cv5u 34et-34eut3w-t".toByteArray()
    }

}