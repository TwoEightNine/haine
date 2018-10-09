package global.msnthrp.haine.login

import global.msnthrp.haine.base.BaseView

/**
 * Created by msnthrp on 22/01/18.
 */
interface LoginView : BaseView {
    fun onLoggedIn()
    fun onRestored()
}