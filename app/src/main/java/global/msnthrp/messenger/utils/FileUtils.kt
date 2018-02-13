package global.msnthrp.messenger.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import okhttp3.ResponseBody
import java.io.*

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

fun getPath(context: Context, uri: Uri): String? {

    if (DocumentsContract.isDocumentUri(context, uri)) {
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]

            if ("primary".equals(type, ignoreCase = true)) {
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }

            // TODO handle non-primary volumes
        } else if (isDownloadsDocument(uri)) {

            val id = DocumentsContract.getDocumentId(uri)
            val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)!!)

            return getDataColumn(context, contentUri, null, null)
        } else if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]

            var contentUri: Uri? = null
            if ("image" == type) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if ("video" == type) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else if ("audio" == type) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

            val selection = "_id=?"
            val selectionArgs = arrayOf(split[1])
            if (contentUri == null) return null

            return getDataColumn(context, contentUri, selection, selectionArgs)
        }
    } else if ("content".equals(uri.scheme, ignoreCase = true)) {

        return getDataColumn(context, uri, null, null)
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        return uri.path
    }

    return null
}

private fun getDataColumn(context: Context, uri: Uri, selection: String?,
                          selectionArgs: Array<String>?): String? {

    var cursor: Cursor? = null
    try {
        val projection = arrayOf(MediaStore.Images.Media.DATA)

        cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val res = cursor.getString(columnIndex)
            cursor.close()
            return res
        }
    } finally {
        if (cursor != null)
            cursor.close()
    }
    return null
}

private fun isExternalStorageDocument(uri: Uri) = "com.android.externalstorage.documents" == uri.authority

private fun isDownloadsDocument(uri: Uri) = "com.android.providers.downloads.documents" == uri.authority

private fun isMediaDocument(uri: Uri) = "com.android.providers.media.documents" == uri.authority