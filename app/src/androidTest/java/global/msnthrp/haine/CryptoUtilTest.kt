package global.msnthrp.haine

import android.support.test.runner.AndroidJUnit4
import global.msnthrp.haine.utils.bytesToHex
import global.msnthrp.haine.utils.fromBase64
import global.msnthrp.haine.utils.getSmallPrime
import global.msnthrp.haine.utils.toBase64
import org.junit.Test
import org.junit.runner.RunWith
import java.math.BigInteger

/**
 * Created by twoeightnine on 2/9/18.
 */

@RunWith(AndroidJUnit4::class)
class CryptoUtilTest {

    @Test
    fun base64_isCorrect() {
        val encoded = toBase64(SAMPLE_TEXT.toByteArray())
        val decoded = fromBase64(encoded).toString()
        assert(SAMPLE_TEXT == decoded)
    }

    @Test
    fun bytesToHex_isCorrect() {
        assert(bytesToHex("12345678".toByteArray()) == "3132333435363738")
    }

    @Test
    fun smallPrime_isCorrect() {
        assert(getSmallPrime(BigInteger("5")) == BigInteger("2"))
        assert(getSmallPrime(BigInteger("47")) == BigInteger("23"))
    }

    companion object {
        val SAMPLE_TEXT = "@RunWith(AndroidJUnit4::class)@RunWith(AndroidJUnit4::class)@RunWith(AndroidJUnit4::class)"
    }

}