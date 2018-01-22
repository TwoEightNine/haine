package global.msnthrp.messenger.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

/**
 * Created by msnthrp on 22/01/18.
 */

fun showToast(context: Context?, text: String = "", duration: Int = Toast.LENGTH_SHORT) {
    if (context == null) return

    Toast.makeText(context, text, duration).show()
}

fun hideKeyboard(activity: Activity) {
    val view = activity.currentFocus
    if (view != null) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}