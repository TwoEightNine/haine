package global.msnthrp.messenger.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import global.msnthrp.messenger.db.dao.StickerDao
import global.msnthrp.messenger.model.Sticker
import global.msnthrp.messenger.storage.Lg

/**
 * Created by msnthrp on 27/01/18.
 */
class DatabaseHelper(context: Context) : OrmLiteSqliteOpenHelper(context,
        DB_NAME, null, DB_VERSION) {

    val stickerDao: StickerDao by lazy { StickerDao(getConnectionSource(), Sticker::class.java) }

    override fun onCreate(db: SQLiteDatabase?, conn: ConnectionSource?) {
        try {
            TableUtils.createTable(conn, Sticker::class.java)
        } catch (e: Exception) {
            Lg.wtf("db onCreate: ${e.message}")
            e.printStackTrace()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, conn: ConnectionSource?, oldVersion: Int, newVersion: Int) {

    }

    companion object {
        val DB_NAME = "global.db"
        val DB_VERSION = 1
    }
}