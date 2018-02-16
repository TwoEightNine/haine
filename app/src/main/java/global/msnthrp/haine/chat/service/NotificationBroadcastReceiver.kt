package global.msnthrp.haine.chat.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by msnthrp on 28/01/18.
 */
class NotificationBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return

        if (intent?.action == ACTION_NAME || intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            context.startService(Intent(context, NotificationService::class.java))
        }
    }

    companion object {
        const val ACTION_NAME = "notificationBroadcastReceiver"
    }
}