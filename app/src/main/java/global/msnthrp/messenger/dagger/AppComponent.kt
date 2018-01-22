package global.msnthrp.messenger.dagger

import dagger.Component
import global.msnthrp.messenger.chat.ChatActivity
import global.msnthrp.messenger.dagger.module.ContextModule
import global.msnthrp.messenger.dagger.module.NetworkModule
import global.msnthrp.messenger.dialogs.DialogsActivity
import global.msnthrp.messenger.login.LoginActivity
import global.msnthrp.messenger.search.SearchActivity
import global.msnthrp.messenger.settings.SettingsActivity
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
}