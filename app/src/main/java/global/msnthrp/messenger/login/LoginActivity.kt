package global.msnthrp.messenger.login

import android.content.Intent
import android.media.MediaCas
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import global.msnthrp.messenger.App
import global.msnthrp.messenger.R
import global.msnthrp.messenger.base.BaseActivity
import global.msnthrp.messenger.dialogs.DialogsActivity
import global.msnthrp.messenger.extensions.view
import global.msnthrp.messenger.network.ApiService
import global.msnthrp.messenger.storage.Session
import global.msnthrp.messenger.utils.hideKeyboard
import global.msnthrp.messenger.utils.showToast
import javax.inject.Inject

class LoginActivity : BaseActivity(), LoginView {

    @Inject
    lateinit var api: ApiService

    @Inject
    lateinit var session: Session

    private val progressBar: ProgressBar by view(R.id.progressBar)
    private val etName: EditText by view(R.id.etName)
    private val etPassword: EditText by view(R.id.etPassword)
    private val btnLogin: Button by view(R.id.btnLogin)
    private val tvInputHint: TextView by view(R.id.tvInputHint)

    private var isLogin = true
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
            if (isLogin) {
                presenter.login(name, password)
            } else {
                presenter.register(name, password)
            }
        }
        tvInputHint.setOnClickListener {
            isLogin = !isLogin
            if (isLogin) {
                btnLogin.setText(R.string.logIn)
                tvInputHint.setText(R.string.registerHint)
            } else {
                btnLogin.setText(R.string.register)
                tvInputHint.setText(R.string.logInHint)
            }
        }
    }

    private fun checkToken() {
        if (session.token.isNotEmpty()) {
            onLoggedIn()
        }
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

    override fun onLoggedIn() {
        startActivity(Intent(this, DialogsActivity::class.java))
        finish()
    }
}
