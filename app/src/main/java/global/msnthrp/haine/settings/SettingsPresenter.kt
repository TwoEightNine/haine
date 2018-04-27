package global.msnthrp.haine.settings

import global.msnthrp.haine.base.BasePresenter
import global.msnthrp.haine.extensions.subscribeSmart
import global.msnthrp.haine.model.User
import global.msnthrp.haine.network.ApiService
import global.msnthrp.haine.storage.Lg
import global.msnthrp.haine.utils.getBytesFromFile
import global.msnthrp.haine.utils.toBase64

/**
 * Created by msnthrp on 22/01/18.
 */
class SettingsPresenter(view: SettingsView,
                        api: ApiService) : BasePresenter<SettingsView>(view, api) {

    private lateinit var user: User

    fun loadUser(userId: Int) {
        view.onShowLoading()
        api.getUser(userId)
                .subscribeSmart({
                    view.onHideLoading()
                    view.onUserLoaded(it)
                    user = it
                }, defaultError())
    }

    fun updatePhoto(photo: String) {
        view.onShowLoading()
        val encoded = toBase64(getBytesFromFile(photo))
        api.uploadPhoto(encoded)
                .subscribeSmart({
                    view.onHideLoading()
                    view.onPhotoUpdated(user)
                }, defaultError())
    }

    fun changePassword(oldPassword: String, newPassword: String) {

    }

    fun terminate() {
        api.terminateSessions()
                .subscribeSmart({
                    view.onTerminated()
                }, {
                    Lg.wtf("error terminating: $it")
                    view.onTerminated()
                })
    }

}