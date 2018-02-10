package global.msnthrp.messenger.extensions

import android.app.AlertDialog
import android.support.annotation.IdRes
import android.view.View

/**
 * Created by twoeightnine on 2/10/18.
 */

fun <T : View> AlertDialog.view(@IdRes viewId: Int) = lazy { findViewById<T>(viewId) }