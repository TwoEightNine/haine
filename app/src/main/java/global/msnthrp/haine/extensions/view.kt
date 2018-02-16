package global.msnthrp.haine.extensions

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE

/**
 * Created by twoeightnine on 2/16/18.
 */

fun View.setVisible(visible: Boolean) {
    visibility = if (visible) VISIBLE else GONE
}

fun View.getVisible() = visibility == VISIBLE

fun View.toggleVisibility() {
    setVisible(!getVisible())
}