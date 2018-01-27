package global.msnthrp.messenger.utils

import global.msnthrp.messenger.db.DbHelper
import global.msnthrp.messenger.extensions.subscribeSmart
import global.msnthrp.messenger.network.ApiService
import global.msnthrp.messenger.storage.Lg
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by msnthrp on 27/01/18.
 */
class ApiUtils @Inject constructor(private val api: ApiService,
                                   private val dbHelper: DbHelper) {

    fun updateStickers() {
        api.getStickers()
                .delay(5L, TimeUnit.SECONDS)
                .subscribeSmart({
                    dbHelper.db.stickerDao.create(it)
                }, {
                    Lg.wtf("error obtaining stickers: $it")
                })
    }

    fun getStickers() = dbHelper.db.stickerDao.getAll()

}