package global.msnthrp.messenger.base

import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem

/**
 * Created by msnthrp on 22/01/18.
 */

open class BaseActivity() : AppCompatActivity() {

    fun initToolbar(toolbar: Toolbar?, postInit: (ActionBar) -> Unit = {}) {
        if (toolbar == null) return

        setSupportActionBar(toolbar)
        supportActionBar?.let {
            postInit.invoke(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?)
        = when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}