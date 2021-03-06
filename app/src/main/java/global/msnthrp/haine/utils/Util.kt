package global.msnthrp.haine.utils

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.Handler
import global.msnthrp.haine.chat.service.NotificationBroadcastReceiver
import global.msnthrp.haine.chat.service.NotificationService
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Created by msnthrp on 22/01/18.
 */

const val URL_REGEX = """^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"""
const val FILE_SHARE_URL = "file.io"
const val STICKER_SIZE_MAX = 512

fun isOnline(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm.activeNetworkInfo?.isConnectedOrConnecting ?: false
}

fun <T> applySchedulersSingle() = { single: Single<T> ->
    single
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> applySchedulersFlowable() = { flowable: Flowable<T> ->
    flowable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun restartApp(context: Context, title: String) {
    showToast(context, title)
    Handler().postDelayed({ restartApp(context) }, 800L)
}

fun restartApp(context: Context) {
    val mStartActivity = getRestartIntent(context)
    val mPendingIntentId = 123456
    val mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT)
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 500, mPendingIntent)

    val notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancelAll()
    System.exit(0)
}

fun getRestartIntent(context: Context): Intent {
    val defaultIntent = Intent(ACTION_MAIN, null)
    defaultIntent.addFlags(FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
    defaultIntent.addCategory(CATEGORY_LAUNCHER)

    val packageName = context.packageName
    val packageManager = context.packageManager
    for (resolveInfo in packageManager.queryIntentActivities(defaultIntent, 0)) {
        val activityInfo = resolveInfo.activityInfo
        if (activityInfo.packageName == packageName) {
            defaultIntent.component = ComponentName(packageName, activityInfo.name)
            return defaultIntent
        }
    }
    throw IllegalStateException("Unable to determine default activity for "
            + packageName
            + ". Does an activity specify the DEFAULT category in its intent filter?")
}

fun startService(context: Context) {
    context.sendBroadcast(Intent(NotificationBroadcastReceiver.ACTION_NAME))
}

fun stopService(context: Context) {
    context.stopService(Intent(context, NotificationService::class.java))
}

fun getNameFromUrl(link: String) = link.split("/")
        .last().split("?").first()

fun isUrl(text: String) = text.matches(Regex(URL_REGEX))

fun needToDecryptAttachment(link: String) = FILE_SHARE_URL in link

fun isAllowedToBeSticker(path: String): Boolean {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(path, options)
    val imageHeight = options.outHeight.toFloat()
    val imageWidth = options.outWidth.toFloat()
    return imageHeight <= STICKER_SIZE_MAX && imageWidth <= STICKER_SIZE_MAX
}
