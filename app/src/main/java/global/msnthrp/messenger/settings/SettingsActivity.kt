package global.msnthrp.messenger.settings

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.*
import global.msnthrp.messenger.App
import global.msnthrp.messenger.BuildConfig
import global.msnthrp.messenger.R
import global.msnthrp.messenger.base.BaseActivity
import global.msnthrp.messenger.db.DbHelper
import global.msnthrp.messenger.extensions.view
import global.msnthrp.messenger.network.ApiService
import global.msnthrp.messenger.model.User
import global.msnthrp.messenger.storage.Lg
import global.msnthrp.messenger.storage.Prefs
import global.msnthrp.messenger.storage.Session
import global.msnthrp.messenger.utils.*
import javax.inject.Inject

/**
 * Created by msnthrp on 22/01/18.
 */
class SettingsActivity : BaseActivity(), SettingsView {

    @Inject
    lateinit var session: Session
    @Inject
    lateinit var api: ApiService
    @Inject
    lateinit var prefs: Prefs
    @Inject
    lateinit var dbHelper: DbHelper

    private val toolbar: Toolbar by view(R.id.toolbar)
    private val progressBar: ProgressBar by view(R.id.progressBar)
    private val etPhoto: EditText by view(R.id.etPhoto)
    private val ivClearPhoto: ImageView by view(R.id.ivClearPhoto)
    private val btnLogOut: Button by view(R.id.btnLogOut)

    private val ivHaine: ImageView by view(R.id.ivHaine)
    private val tvVersion: TextView by view(R.id.tvVersion)

    private val handler = Handler()
    private var oldPhoto = ""
    private val presenter: SettingsPresenter by lazy { SettingsPresenter(this, api) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        App.appComponent.inject(this)
        initToolbar(toolbar) {
            it.setDisplayHomeAsUpEnabled(true)
            it.setTitle(R.string.settings)
        }
        ivClearPhoto.setOnClickListener { etPhoto.setText("") }
        btnLogOut.setOnClickListener { presenter.terminate() }
        presenter.loadUser(session.userId)
        handler.postDelayed(::showWhatIs, HINT_DELAY)
        ivHaine.setOnClickListener { showLogs() }
        tvVersion.text = getString(R.string.version, BuildConfig.VERSION_NAME, BuildConfig.BUILD_TIME)
    }

    override fun onShowLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onHideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun onShowError(error: String) {
        showToast(this, error)
    }

    override fun onUserLoaded(user: User) {
        etPhoto.setText(user.photo)
        user.photo?.let { oldPhoto = it }
    }

    override fun onPhotoUpdated() {
        saveSettings()
        finish()
    }

    override fun onTerminated() {
        prefs.reset()
        session.reset()
        dbHelper.db.dropAll()
        finishAffinity()
        restartApp(this, getString(R.string.loggingOut))
    }

    override fun onBackPressed() {
        val newPhoto = etPhoto.text.toString()
        if (newPhoto != oldPhoto) {
            presenter.updatePhoto(newPhoto)
        } else {
            saveSettings()
            super.onBackPressed()
        }
    }

    private fun showWhatIs() {
        Snackbar.make(findViewById<View>(android.R.id.content), "What is haine?", Snackbar.LENGTH_INDEFINITE)
                .setAction("Find out") {
                    startActivity(Intent(this, WhatIsHaineActivity::class.java))
                }
                .show()
    }

    private fun showLogs() {
        showAlert(this, Lg.logs.joinToString(separator = "\n"))
    }

    private fun saveSettings() {

    }

    companion object {
        const val HINT_DELAY = 1000L * 90
    }
}