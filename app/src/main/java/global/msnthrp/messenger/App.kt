package global.msnthrp.messenger

import android.app.Application
import global.msnthrp.messenger.dagger.AppComponent
import global.msnthrp.messenger.dagger.DaggerAppComponent
import global.msnthrp.messenger.dagger.module.ContextModule

/**
 * Created by msnthrp on 22/01/18.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
                .contextModule(ContextModule(this))
                .build()
    }

    companion object {
        lateinit var appComponent: AppComponent

        const val BASE_URL = "http://82.179.49.30:1753"
    }
}