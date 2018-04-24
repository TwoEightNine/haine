package global.msnthrp.haine.settings

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import android.widget.*
import de.hdodenhof.circleimageview.CircleImageView
import global.msnthrp.haine.App
import global.msnthrp.haine.BuildConfig
import global.msnthrp.haine.R
import global.msnthrp.haine.base.BaseActivity
import global.msnthrp.haine.db.DbHelper
import global.msnthrp.haine.extensions.loadUrl
import global.msnthrp.haine.extensions.loadUrlForce
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
    lateinit var apiUtils: ApiUtils
    @Inject
    lateinit var prefs: Prefs
    @Inject
    lateinit var dbHelper: DbHelper

    private val toolbar: Toolbar by view(R.id.toolbar)
    private val progressBar: ProgressBar by view(R.id.progressBar)
    private val ivLogOut: ImageView by view(R.id.ivLogOut)
    private val civAvatar: CircleImageView by view(R.id.civAvatar)
    private val tvName: TextView by view(R.id.tvName)

    private val ivHaine: ImageView by view(R.id.ivHaine)
    private val tvVersion: TextView by view(R.id.tvVersion)

    private val swShowNotifications: Switch by view(R.id.swShowNotifications)
    private val swVibrate: Switch by view(R.id.swVibrate)
    private val swPlayRingtone: Switch by view(R.id.swPlayRingtone)

    private val handler = Handler()
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
        ivLogOut.setOnClickListener { showLogOutConfirm() }
        presenter.loadUser(session.userId)
        handler.postDelayed(::showWhatIs, HINT_DELAY)
        ivHaine.setOnClickListener { showLogs() }
        tvVersion.text = getString(R.string.version, BuildConfig.VERSION_NAME, BuildConfig.BUILD_TIME)
        civAvatar.setOnClickListener {
            if (hasPermissions(this)) {
                chooseFile()
            } else {
                requestPermissions(this, PERMISSIONS_REQUEST_CODE)
            }
        }
        apiUtils.updateStickers()
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
        tvName.text = user.name
        civAvatar.loadUrl(this, user.photoUrl())
    }

    override fun onPhotoUpdated(user: User) {
        civAvatar.loadUrlForce(this, user.photoUrl())
    }

    private fun chooseFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICKFILE_REQUEST_CODE)
    }

    override fun onTerminated() {
        prefs.reset()
        session.reset()
        dbHelper.db.dropAll()
        finishAffinity()
        restartApp(this, getString(R.string.loggingOut))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICKFILE_REQUEST_CODE
                && resultCode == Activity.RESULT_OK && data != null) {
            val path = getPath(this, data.data) ?: return
            presenter.updatePhoto(path)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE &&
                grantResults.filter { it != PackageManager.PERMISSION_GRANTED }.isEmpty()) {
            chooseFile()
        }
    }

    override fun onBackPressed() {
        saveSettings()
        super.onBackPressed()
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
        const val PICKFILE_REQUEST_CODE = 17 * 5 * 3
        const val PERMISSIONS_REQUEST_CODE = 175 + 3
    }
}