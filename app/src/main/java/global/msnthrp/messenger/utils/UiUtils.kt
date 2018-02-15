package global.msnthrp.messenger.utils

import android.app.Activity
import android.content.Context
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DialogTitle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import global.msnthrp.messenger.R

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
    if (context == null) return

    val dialog = AlertDialog.Builder(context)
            .setMessage(text)
            .setPositiveButton(android.R.string.ok, null)
            .create()
    dialog.show()
    paintDialog(context, dialog)
}

fun showConfirm(context: Context?, text: String, callback: (Boolean) -> Unit) {
    if (context == null) return

    val dialog = AlertDialog.Builder(context)
            .setMessage(text)
            .setPositiveButton(android.R.string.ok, { _, _ -> callback.invoke(true) })
            .setNegativeButton(android.R.string.cancel, { _, _ -> callback.invoke(false) })
            .create()
    dialog.show()
    paintDialog(context, dialog)
}

fun paintDialog(context: Context?, dialog: AlertDialog?) {
    if (dialog == null || context == null) return
    with(dialog) {
        findViewById<View>(R.id.contentPanel)?.setBackgroundColor(ContextCompat.getColor(context, R.color.backgroundPopup))
        findViewById<View>(R.id.buttonPanel)?.setBackgroundColor(ContextCompat.getColor(context, R.color.backgroundPopup))
        findViewById<View>(R.id.topPanel)?.setBackgroundColor(ContextCompat.getColor(context, R.color.backgroundPopup))
        findViewById<DialogTitle>(R.id.alertTitle)?.setTextColor(ContextCompat.getColor(context, R.color.textLight))
        findViewById<TextView>(android.R.id.message)?.setTextColor(ContextCompat.getColor(context, R.color.textLight))
        findViewById<Button>(android.R.id.button1)?.setTextColor(ContextCompat.getColor(context, R.color.textLightSecondary))
        findViewById<Button>(android.R.id.button2)?.setTextColor(ContextCompat.getColor(context, R.color.textLightSecondary))
    }
}