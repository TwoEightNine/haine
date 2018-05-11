package global.msnthrp.haine.settings

import global.msnthrp.haine.base.BaseView
import global.msnthrp.haine.model.User

/**
 * Created by msnthrp on 22/01/18.
 */
interface SettingsView : BaseView {
    fun onUserLoaded(user: User)
    fun onPhotoUpdated(user: User)
    fun onTerminated()
    fun onPasswordChanged()
}