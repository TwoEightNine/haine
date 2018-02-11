package global.msnthrp.messenger.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

/**
 * Created by msnthrp on 22/01/18.
 */

fun showToast(context: Context?, text: String = "", duration: Int = Toast.LENGTH_SHORT) {
    if (context == null) return

    Toast.makeText(context, text, duration).show()
}

fun showToast(context: Context?, @StringRes textId: Int, duration: Int = Toast.LENGTH_SHORT) {
    showToast(context, context?.getString(textId) ?: "", duration)
}

fun hideKeyboard(activity: Activity) {
    val view = activity.currentFocus
    if (view != null) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun showAlert(context: Context?, text: String) {
    AlertDialog.Builder(context)
            .setMessage(text)
            .setPositiveButton(android.R.string.ok, null)
            .show()
}

fun showConfirm(context: Context?, text: String, callback: (Boolean) -> Unit) {
    AlertDialog.Builder(context)
            .setMessage(text)
            .setPositiveButton(android.R.string.ok, { _, _ -> callback.invoke(true) })
            .setNegativeButton(android.R.string.cancel, { _, _ -> callback.invoke(false) })
            .show()
}