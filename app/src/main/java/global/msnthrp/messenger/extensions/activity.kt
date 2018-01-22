package global.msnthrp.messenger.extensions

import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.view.View

/**
 * Created by msnthrp on 22/01/18.
 */

fun <T: View> AppCompatActivity.view(@IdRes viewId: Int) = lazy { findViewById<T>(viewId) }

//fun <T: View> AppCompatActivity