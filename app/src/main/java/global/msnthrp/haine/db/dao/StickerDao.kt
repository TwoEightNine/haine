package global.msnthrp.haine.db.dao

import com.j256.ormlite.dao.BaseDaoImpl
import com.j256.ormlite.support.ConnectionSource
import global.msnthrp.haine.model.Sticker

/**
 * Created by msnthrp on 27/01/18.
 */
class StickerDao(connectionSource: ConnectionSource,
                 dataClass: Class<Sticker>) : BaseDaoImpl<Sticker, Int>(connectionSource, dataClass) {

    fun getAll() = queryForAll().sortedByDescending { it.id }

    fun createOrUpdate(stickers: List<Sticker>) {
        stickers.forEach { createOrUpdate(it) }
    }

}