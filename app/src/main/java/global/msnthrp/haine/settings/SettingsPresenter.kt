package global.msnthrp.haine.settings

import global.msnthrp.haine.base.BasePresenter
import global.msnthrp.haine.extensions.subscribeSmart
import global.msnthrp.haine.network.ApiService
import global.msnthrp.haine.storage.Lg

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