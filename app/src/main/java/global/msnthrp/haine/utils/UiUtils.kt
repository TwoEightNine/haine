package global.msnthrp.haine.utils

import android.app.Activity
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.annotation.StringRes
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DialogTitle
import android.text.Html
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import global.msnthrp.haine.R
import global.msnthrp.haine.chat.service.NotificationService

/**
 * Created by msnthrp on 22/01/18.
 */

const val NOTIFICATION = 1753

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

fun showNotification(context: Context?,
                             content: String? = context?.getString(R.string.hidden_content),
                             title: String? = context?.getString(R.string.appName),
                             icon: Bitmap = BitmapFactory.decodeResource(context?.resources, R.mipmap.haine128)) {
    if (context == null) return

    val intent = getRestartIntent(context)
    val pIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
    )
    val mBuilder = NotificationCompat.Builder(context)
            .setLargeIcon(icon)
            .setSmallIcon(R.drawable.ic_message)
            .setContentTitle(title)
            .setContentText(Html.fromHtml(content))
            .setContentIntent(pIntent)

    val mNotifyMgr = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    mNotifyMgr.notify(NOTIFICATION, mBuilder.build())
}

fun closeNotification(context: Context?) {
    if (context == null) return

    val mNotifyMgr = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    mNotifyMgr.cancel(NOTIFICATION)
}