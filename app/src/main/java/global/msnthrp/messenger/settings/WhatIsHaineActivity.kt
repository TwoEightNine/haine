package global.msnthrp.messenger.settings

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import global.msnthrp.messenger.R
import global.msnthrp.messenger.base.BaseActivity
import global.msnthrp.messenger.extensions.view

/**
 * Created by twoeightnine on 2/11/18.
 */
class WhatIsHaineActivity : BaseActivity() {

    private val iv1753: ImageView by view(R.id.iv1753)

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_what_is)
        handler.postDelayed({ iv1753.visibility = View.VISIBLE }, SHOW_DELAY)
    }

    companion object {
        const val SHOW_DELAY = 1000L * 120 // 2 mins
    }
}