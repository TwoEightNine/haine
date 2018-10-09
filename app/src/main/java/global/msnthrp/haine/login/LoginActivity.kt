package global.msnthrp.haine.login

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import global.msnthrp.haine.App
import global.msnthrp.haine.R
import global.msnthrp.haine.base.BaseActivity
import global.msnthrp.haine.dialogs.DialogsActivity
import global.msnthrp.haine.extensions.getAsString
import global.msnthrp.haine.extensions.setVisible
import global.msnthrp.haine.extensions.view
import global.msnthrp.haine.network.ApiService
import global.msnthrp.haine.settings.SettingsActivity
import global.msnthrp.haine.storage.Session
import global.msnthrp.haine.utils.hideKeyboard
import global.msnthrp.haine.utils.showAlert
import global.msnthrp.haine.utils.showToast
import javax.inject.Inject

class LoginActivity : BaseActivity(), LoginView {

    @Inject
    lateinit var api: ApiService

    @Inject
    lateinit var session: Session

    private val etName: EditText by view(R.id.etName)
    private val etPassword: EditText by view(R.id.etPassword)
    private val etPasswordRepeat: EditText by view(R.id.etPasswordRepeat)
    private val etEmail: EditText by view(R.id.etEmail)

    private val tilName: TextInputLayout by view(R.id.tilName)
    private val tilPassword: TextInputLayout by view(R.id.tilPassword)
    private val tilPasswordRepeat: TextInputLayout by view(R.id.tilPasswordRepeat)
    private val tilEmail: TextInputLayout by view(R.id.tilEmail)

    private val tvLogInHint: TextView by view(R.id.tvLogInHint)
    private val tvRegisterHint: TextView by view(R.id.tvRegisterHint)
    private val tvRestoreHint: TextView by view(R.id.tvRestoreHint)

    private val btnLogin: Button by view(R.id.btnLogin)
    private val progressBar: ProgressBar by view(R.id.progressBar)

    private val presenter: LoginPresenter by lazy { LoginPresenter(this, api, session) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        checkToken()

        setContentView(R.layout.activity_login)
        btnLogin.setOnClickListener {
            hideKeyboard(this)
            val name = etName.text.toString()
            val password = etPassword.text.toString()
            val email = etEmail.text.toString()
            when (state) {
                STATE_SIGN_UP -> presenter.register(name, password, email)
                STATE_LOG_IN -> presenter.login(name, password)
                STATE_RESTORE -> presenter.restore(email)
            }
        }
        tvLogInHint.setOnClickListener { switchState(STATE_LOG_IN) }
        tvRegisterHint.setOnClickListener { switchState(STATE_SIGN_UP) }
        tvRestoreHint.setOnClickListener { switchState(STATE_RESTORE) }

        val watcher = PasswordMatchWatcher()
        etName.addTextChangedListener(watcher)
        etPassword.addTextChangedListener(watcher)
        etPasswordRepeat.addTextChangedListener(watcher)
        etEmail.addTextChangedListener(watcher)
        switchState(STATE_LOG_IN)
        invalidateEditTexts()
    }

    private fun checkToken() {
        if (session.token.isNotEmpty()) {
            onLoggedIn()
        }
    }

    private fun switchState(newState: Int) {
        state = newState
        invalidateState()
    }

    private fun invalidateState() {
        tilName.setVisible(state != STATE_RESTORE)
        tilPassword.setVisible(state != STATE_RESTORE)
        tilPasswordRepeat.setVisible(state == STATE_SIGN_UP)
        tilEmail.setVisible(state != STATE_LOG_IN)
        tvLogInHint.setVisible(state != STATE_LOG_IN)
        tvRegisterHint.setVisible(state != STATE_SIGN_UP)
        tvRestoreHint.setVisible(state != STATE_RESTORE)
        btnLogin.text = getString(
                when (state) {
                    STATE_LOG_IN -> R.string.logIn
                    STATE_SIGN_UP -> R.string.register
                    else -> R.string.restore
                }
        )
    }

    private fun invalidateEditTexts() {
        var enableButton = false
        val password = etPassword.text.toString()
        val passwordRepeat = etPasswordRepeat.text.toString()
        val email = etEmail.text.toString()
        val name = etName.text.toString()
        when (state) {
            STATE_SIGN_UP -> {
                val confirmed = password == passwordRepeat
                        && password.length >= PASSWORD_LENGTH
                val icon = if (confirmed) R.drawable.ic_check else 0
                etPasswordRepeat.setCompoundDrawablesWithIntrinsicBounds(0, 0, icon, 0)
                enableButton = name.length >= NAME_LENGTH && confirmed && email.length >= EMAIL_LENGTH
            }
            STATE_LOG_IN -> {
                enableButton = name.length >= NAME_LENGTH && password.length >= PASSWORD_LENGTH
            }
            STATE_RESTORE -> {
                enableButton = email.length >= EMAIL_LENGTH
            }
        }
        btnLogin.isEnabled = enableButton
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

    override fun onRestored() {
        showAlert(this, getString(R.string.restored))
        switchState(STATE_LOG_IN)
    }

    override fun onLoggedIn() {
        startActivity(Intent(this, DialogsActivity::class.java))
        finish()
    }

    private inner class PasswordMatchWatcher : TextWatcher {

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            invalidateEditTexts()
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

    companion object {

        const val PASSWORD_LENGTH = 8
        const val NAME_LENGTH = 4
        const val EMAIL_LENGTH = 8

        const val STATE_LOG_IN = 1
        const val STATE_SIGN_UP = 2
        const val STATE_RESTORE = 3

        var state = STATE_LOG_IN
    }
}
