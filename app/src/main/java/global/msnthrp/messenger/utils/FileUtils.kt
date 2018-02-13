package global.msnthrp.messenger.utils

import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import okhttp3.ResponseBody
import java.io.*
import java.net.URISyntaxException

/**
 * Created by twoeightnine on 2/12/18.
 */

fun getBytesFromFile(fileName: String): ByteArray {
    val file = File(fileName)
    val size = file.length().toInt()
    val bytes = ByteArray(size)
    try {
        val buf = BufferedInputStream(FileInputStream(file))
        buf.read(bytes, 0, bytes.size)
        buf.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return bytes
}

fun writeBytesToFile(bytes: ByteArray, fileName: String): String {
    val file = File(fileName)
    try {
        val out = FileOutputStream(file.absolutePath)
        out.write(bytes)
        out.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return file.absolutePath
}

fun writeResponseBodyToDisk(body: ResponseBody, path: String): Boolean {
    try {
        val futureStudioIconFile = File(path)
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null

        try {
            val fileReader = ByteArray(4096)
            var fileSizeDownloaded: Long = 0

            inputStream = body.byteStream()
            outputStream = FileOutputStream(futureStudioIconFile)

            while (true) {
                val read = inputStream!!.read(fileReader)

                if (read == -1) {
                    break
                }

                outputStream.write(fileReader, 0, read)

                fileSizeDownloaded += read.toLong()
            }
            outputStream.flush()

            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    } catch (e: IOException) {
        e.printStackTrace()
        return false
    }

}

fun getPicMimeType(context: Context, path: String): String {
    val opt = BitmapFactory.Options()
    /* The doc says that if inJustDecodeBounds set to true, the decoder
     * will return null (no bitmap), but the out... fields will still be
     * set, allowing the caller to query the bitmap without having to
     * allocate the memory for its pixels. */
    opt.inJustDecodeBounds = true

    val istream = context.contentResolver.openInputStream(Uri.fromFile(File(path)))
    BitmapFactory.decodeStream(istream, null, opt)
    istream?.close()

    return opt.outMimeType
}

@Throws(URISyntaxException::class)
fun getPath(context: Context, uri: Uri): String? {
    if ("content".equals(uri.scheme, ignoreCase = true)) {
        val projection = arrayOf("_data")
        var cursor: Cursor? = null

        try {
            cursor = context.contentResolver.query(uri, projection, null, null, null)
            val columnIndex = cursor!!.getColumnIndexOrThrow("_data")
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }

    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        return uri.path
    }
    return null
}