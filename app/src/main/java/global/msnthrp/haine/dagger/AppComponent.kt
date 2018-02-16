package global.msnthrp.haine.dagger

import dagger.Component
import global.msnthrp.haine.chat.ChatActivity
import global.msnthrp.haine.chat.service.NotificationService
import global.msnthrp.haine.chat.stickers.StickersFragment
import global.msnthrp.haine.dagger.module.ContextModule
import global.msnthrp.haine.dagger.module.NetworkModule
import global.msnthrp.haine.dialogs.DialogsActivity
import global.msnthrp.haine.login.LoginActivity
import global.msnthrp.haine.search.SearchActivity
import global.msnthrp.haine.settings.SettingsActivity
import javax.inject.Singleton

/**
 * Created by msnthrp on 22/01/18.
 */

@Singleton
@Component(modules = arrayOf(ContextModule::class, NetworkModule::class))
interface AppComponent {

    fun inject(loginActivity: LoginActivity)
    fun inject(dialogsActivity: DialogsActivity)
    fun inject(searchActivity: SearchActivity)
    fun inject(settingsActivity: SettingsActivity)
    fun inject(chatActivity: ChatActivity)

    fun inject(stickersFragment: StickersFragment)

    fun inject(notificationService: NotificationService)
}