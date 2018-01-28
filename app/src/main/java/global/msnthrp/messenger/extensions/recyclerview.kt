package global.msnthrp.messenger.extensions

import android.support.annotation.IdRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by msnthrp on 22/01/18.
 */

/** Binds views by its [viewId] */
fun <T : View> RecyclerView.ViewHolder.view(@IdRes viewId: Int) = lazy { itemView.findViewById<T>(viewId) }

/** Returns true if [RecyclerView] is positioned at the bottom (last view is visible) */
fun RecyclerView.isAtEnd(totalCount: Int)
        = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition() == totalCount - 1