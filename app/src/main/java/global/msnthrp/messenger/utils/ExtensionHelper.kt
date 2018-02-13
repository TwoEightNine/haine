package global.msnthrp.messenger.utils

import java.util.*

/**
 * Created by twoeightnine on 2/13/18.
 */
object ExtensionHelper {

    private val models = listOf(
            ExtensionModel("jpg", intArrayOf(255, 216, 255)),
            ExtensionModel("png", intArrayOf(137, 80, 78, 71)),
            ExtensionModel("bmp", intArrayOf(66, 77)),
            ExtensionModel("mp3", intArrayOf(255, 251)),
            ExtensionModel("mp3", intArrayOf(73, 68, 67)),
            ExtensionModel("wav", intArrayOf(82, 73, 70, 70)),
            ExtensionModel("pdf", intArrayOf(37, 80, 48, 70)),
            ExtensionModel("gif", intArrayOf(71, 73, 70, 56))
    )

    fun getExtension(path: String) = getExtension(getBytesFromFile(path))

    fun getExtension(bytes: ByteArray): String? {
        for (model in models) {
            val len = model.sign.size
            if (Arrays.equals(bytes.copyOf(len), model.sign.toByteArray())) {
                return model.ext
            }
        }
        return null
    }

    private fun IntArray.toByteArray() = map { it.toByte() }.toByteArray()
}

data class ExtensionModel(val ext: String, val sign: IntArray)
