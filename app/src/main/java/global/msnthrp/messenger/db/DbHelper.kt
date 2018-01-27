package global.msnthrp.messenger.db

import android.content.Context
import com.j256.ormlite.android.apptools.OpenHelperManager
import javax.inject.Inject

/**
 * Created by msnthrp on 27/01/18.
 */
class DbHelper @Inject constructor(context: Context) {

    val db: DatabaseHelper by lazy {
        OpenHelperManager.getHelper(context, DatabaseHelper::class.java)
    }

    fun release() {
        OpenHelperManager.releaseHelper()
    }

}