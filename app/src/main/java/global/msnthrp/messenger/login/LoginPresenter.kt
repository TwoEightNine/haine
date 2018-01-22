package global.msnthrp.messenger.login

import global.msnthrp.messenger.base.BasePresenter
import global.msnthrp.messenger.extensions.subscribeSmart
import global.msnthrp.messenger.network.ApiService
import global.msnthrp.messenger.storage.Lg
import global.msnthrp.messenger.storage.Session

/**
 * Created by msnthrp on 22/01/18.
 */

class LoginPresenter(view: LoginView,
                     api: ApiService,
                     private val session: Session) : BasePresenter<LoginView>(view, api) {

    fun register(name: String, password: String) {
        view.onShowLoading()
        api.register(name, password)
                .subscribeSmart({
                    Lg.i("registered successfully $it")
                    login(name, password)
                }, defaultError())
    }

    fun login(name: String, password: String) {
        view.onShowLoading()
        api.login(name, password)
                .subscribeSmart({ response ->
                    session.token = response.token
                    session.userId = response.userId
                    view.onHideLoading()
                    view.onLoggedIn()
                }, defaultError())
    }

}