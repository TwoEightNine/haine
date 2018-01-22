package global.msnthrp.messenger.extensions

import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by msnthrp on 22/01/18.
 */

fun <T : View> RecyclerView.ViewHolder.view(@IdRes viewId: Int) = lazy { itemView.findViewById<T>(viewId) }