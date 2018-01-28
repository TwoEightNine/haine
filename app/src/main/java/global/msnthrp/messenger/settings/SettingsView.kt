package global.msnthrp.messenger.settings

import global.msnthrp.messenger.base.BaseView
import global.msnthrp.messenger.model.User

/**
 * Created by msnthrp on 22/01/18.
 */
interface SettingsView : BaseView {
    fun onUserLoaded(user: User)
    fun onPhotoUpdated()
}