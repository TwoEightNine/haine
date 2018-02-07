package global.msnthrp.messenger.utils

import android.util.Base64
import java.math.BigInteger
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*

/**
 * Created by twoeightnine on 2/6/18.
 */

const val DH_BITS = 2048

fun getSmallPrime(bigPrime: BigInteger) = bigPrime.minus(BigInteger.ONE).divide(BigInteger("2"))

fun getRandomPrimeRoot(p: BigInteger, q: BigInteger): BigInteger {
    var g: BigInteger
    do {
        g = BigInteger(32, SecureRandom())
    } while (g == BigInteger.ZERO || !isPrimeRoot(g, q, p))
    return g
}

fun isPrimeRoot(g: BigInteger, q: BigInteger, p: BigInteger): Boolean {
    return g.modPow(BigInteger.ONE, p) != BigInteger.ONE &&
            g.modPow(BigInteger.valueOf(2), p) != BigInteger.ONE &&
            g.modPow(q, p) != BigInteger.ONE

}

fun md5Raw(plain: ByteArray) = MessageDigest
        .getInstance("MD5")
        .digest(plain)

fun md5(plain: String) = md5Raw(plain.toByteArray())
        .map { Integer.toHexString(it.toInt() and 0xff) }
        .map { if (it.length == 2) it else "0$it" }
        .joinToString(separator = "")

fun sha256Raw(plain: ByteArray) = MessageDigest
        .getInstance("SHA-256")
        .digest(plain)

fun sha256(plain: String) = sha256Raw(plain.toByteArray())
        .map { Integer.toHexString(it.toInt() and 0xff) }
        .map { if (it.length == 2) it else "0$it" }
        .joinToString(separator = "")

fun bytesToHex(bytes: ByteArray) = bytes
        .map { Integer.toHexString(it.toInt() and 0xff) }
        .map { if (it.length == 2) it else "0$it" }
        .joinToString(separator = "")

fun getUiFriendlyHash(hash: String) = hash
        .mapIndexed { index, c -> if (index % 2 == 0) c.toString() else "$c " } // spaces
        .mapIndexed { index, s -> if (index % 16 == 15) "$s\n" else s } // new-lines
        .map { it.toUpperCase() }
        .joinToString(separator = "")

fun toBase64(bytes: ByteArray): String = Base64.encodeToString(bytes, Base64.NO_WRAP)

fun fromBase64(text: String) = Base64.decode(text, Base64.NO_WRAP)