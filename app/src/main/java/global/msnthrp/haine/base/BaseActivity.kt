package global.msnthrp.haine.base

import android.annotation.SuppressLint
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by msnthrp on 22/01/18.
 */

@SuppressLint("Registered")
open class BaseActivity() : AppCompatActivity() {

    protected val compositeDisposable = CompositeDisposable()

    protected var isShown = false

    fun initToolbar(toolbar: Toolbar?, postInit: (ActionBar) -> Unit = {}) {
        if (toolbar == null) return

        setSupportActionBar(toolbar)
        supportActionBar?.let {
            postInit.invoke(it)
        }
    }

    override fun onResume() {
        super.onResume()
        isShown = true
    }

    override fun onPause() {
        super.onPause()
        isShown = false
    }

    override fun onOptionsItemSelected(item: MenuItem?)
        = when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}