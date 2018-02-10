package global.msnthrp.messenger.settings

import global.msnthrp.messenger.base.BasePresenter
import global.msnthrp.messenger.extensions.subscribeSmart
import global.msnthrp.messenger.network.ApiService
import global.msnthrp.messenger.storage.Lg

/**
 * Created by msnthrp on 22/01/18.
 */
class SettingsPresenter(view: SettingsView,
                        api: ApiService) : BasePresenter<SettingsView>(view, api) {

    fun loadUser(userId: Int) {
        view.onShowLoading()
        api.getUser(userId)
                .subscribeSmart({
                    view.onHideLoading()
                    view.onUserLoaded(it)
                }, defaultError())
    }

    fun updatePhoto(photo: String) {
        view.onShowLoading()
        api.updatePhoto(photo)
                .subscribeSmart({
                    view.onHideLoading()
                    view.onPhotoUpdated()
                }, defaultError())
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