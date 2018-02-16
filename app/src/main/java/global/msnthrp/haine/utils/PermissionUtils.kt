package global.msnthrp.haine.utils

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import global.msnthrp.haine.R

/**
 * Created by twoeightnine on 2/13/18.
 */

fun hasPermissions(activity: Activity) = Build.VERSION.SDK_INT < 23 ||
        ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

@TargetApi(23)
fun requestPermissions(activity: Activity, requestCode: Int) {
    val dialog = AlertDialog.Builder(activity)
            .setMessage(R.string.permissions_info)
            .setPositiveButton(android.R.string.ok, { _, _ ->
                activity.requestPermissions(arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO
                ), requestCode)
            })
            .create()
    dialog.show()
    paintDialog(activity, dialog)
}