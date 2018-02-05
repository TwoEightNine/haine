package global.msnthrp.messenger.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import global.msnthrp.messenger.db.dao.ExchangeParamsDao
import global.msnthrp.messenger.db.dao.StickerDao
import global.msnthrp.messenger.model.ExchangeParams
import global.msnthrp.messenger.model.Sticker
import global.msnthrp.messenger.storage.Lg

/**
 * Created by msnthrp on 27/01/18.
 */
class DatabaseHelper(context: Context) : OrmLiteSqliteOpenHelper(context,
        DB_NAME, null, DB_VERSION) {

    val stickerDao: StickerDao by lazy { StickerDao(getConnectionSource(), Sticker::class.java) }
    val exchangeDao: ExchangeParamsDao by lazy { ExchangeParamsDao(getConnectionSource(), ExchangeParams::class.java) }

    override fun onCreate(db: SQLiteDatabase?, conn: ConnectionSource?) {
        try {
            TableUtils.createTable(conn, Sticker::class.java)
            TableUtils.createTable(conn, ExchangeParams::class.java)
        } catch (e: Exception) {
            Lg.wtf("db onCreate: ${e.message}")
            e.printStackTrace()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, conn: ConnectionSource?, oldVersion: Int, newVersion: Int) {
        dropAll()
        // TODO realise migration
    }

    fun dropAll() {
        TableUtils.dropTable<Sticker, Int>(getConnectionSource(), Sticker::class.java, true)
        TableUtils.dropTable<ExchangeParams, Int>(getConnectionSource(), ExchangeParams::class.java, true)
        TableUtils.createTable(getConnectionSource(), Sticker::class.java)
        TableUtils.createTable(getConnectionSource(), ExchangeParams::class.java)
    }

    companion object {
        const val DB_NAME = "global.db"
        const val DB_VERSION = 2
    }
}