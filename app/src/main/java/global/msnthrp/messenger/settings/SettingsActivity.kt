package global.msnthrp.messenger.settings

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import global.msnthrp.messenger.App
import global.msnthrp.messenger.R
import global.msnthrp.messenger.base.BaseActivity
import global.msnthrp.messenger.db.DbHelper
import global.msnthrp.messenger.extensions.view
import global.msnthrp.messenger.network.ApiService
import global.msnthrp.messenger.model.User
import global.msnthrp.messenger.storage.Prefs
import global.msnthrp.messenger.storage.Session
import global.msnthrp.messenger.utils.restartApp
import global.msnthrp.messenger.utils.showToast
import global.msnthrp.messenger.utils.stopService
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
        btnLogOut.setOnClickListener { logOut() }
        presenter.loadUser(session.userId)
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

    override fun onBackPressed() {
        val newPhoto = etPhoto.text.toString()
        if (newPhoto != oldPhoto) {
            presenter.updatePhoto(newPhoto)
        } else {
            saveSettings()
            super.onBackPressed()
        }
    }

    private fun logOut() {
        prefs.reset()
        session.reset()
        dbHelper.db.dropAll()
        stopService(this)
        finishAffinity()
        restartApp(this, getString(R.string.loggingOut))
    }

    private fun saveSettings() {

    }
}