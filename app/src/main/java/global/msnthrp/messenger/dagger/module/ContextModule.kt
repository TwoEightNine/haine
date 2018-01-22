package global.msnthrp.messenger.dagger.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import global.msnthrp.messenger.storage.Session
import javax.inject.Singleton

/**
 * Created by msnthrp on 22/01/18.
 */

@Module
class ContextModule(private val app: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun provideSession(context: Context): Session = Session(context)

}