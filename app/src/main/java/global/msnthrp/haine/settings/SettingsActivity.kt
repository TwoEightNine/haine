package global.msnthrp.haine.settings

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.*
import global.msnthrp.haine.App
import global.msnthrp.haine.BuildConfig
import global.msnthrp.haine.R
import global.msnthrp.haine.base.BaseActivity
import global.msnthrp.haine.db.DbHelper
import global.msnthrp.haine.extensions.setVisible
import global.msnthrp.haine.extensions.view
import global.msnthrp.haine.network.ApiService
import global.msnthrp.haine.model.User
import global.msnthrp.haine.storage.Lg
import global.msnthrp.haine.storage.Prefs
import global.msnthrp.haine.storage.Session
import global.msnthrp.haine.utils.*
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

    private val swShowNotifications: Switch by view(R.id.swShowNotifications)
    private val swVibrate: Switch by view(R.id.swVibrate)
    private val swPlayRingtone: Switch by view(R.id.swPlayRingtone)

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
        initSwitches()
        ivClearPhoto.setOnClickListener { etPhoto.setText("") }
        btnLogOut.setOnClickListener { showLogOutConfirm() }
        presenter.loadUser(session.userId)
        handler.postDelayed(::showWhatIs, HINT_DELAY)
        ivHaine.setOnClickListener { showLogs() }
        tvVersion.text = getString(R.string.version, BuildConfig.VERSION_NAME, BuildConfig.BUILD_TIME)
    }

    override fun onShowLoading() {
        progressBar.setVisible(true)
    }

    override fun onHideLoading() {
        progressBar.setVisible(false)
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
        Snackbar.make(contentView, "What is haine?", Snackbar.LENGTH_INDEFINITE)
                .setAction("Find out") {
                    startActivity(Intent(this, WhatIsHaineActivity::class.java))
                }
                .show()
    }

    private fun showLogs() {
        showAlert(this, Lg.logs.joinToString(separator = "\n"))
    }

    private fun showLogOutConfirm() {
        showConfirm(this, getString(R.string.wanna_log_out)) {
            if (it) presenter.terminate()
        }
    }

    private fun initSwitches() {
        swShowNotifications.isChecked = prefs.showNotifications
        swVibrate.isChecked = prefs.vibrate
        swPlayRingtone.isChecked = prefs.soundNotifications
    }

    private fun saveSettings() {
        prefs.showNotifications = swShowNotifications.isChecked
        prefs.vibrate = swVibrate.isChecked
        prefs.soundNotifications = swPlayRingtone.isChecked
    }

    companion object {
        const val HINT_DELAY = 1000L * 90
    }
}