package global.msnthrp.messenger.extensions

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.view.View

/**
 * Created by msnthrp on 28/01/18.
 */

fun <V : View> Fragment.view(@IdRes viewId: Int) = lazy { view!!.findViewById<V>(viewId) }