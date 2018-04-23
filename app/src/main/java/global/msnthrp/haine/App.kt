package global.msnthrp.haine

import android.app.Application
import global.msnthrp.haine.dagger.AppComponent
import global.msnthrp.haine.dagger.DaggerAppComponent
import global.msnthrp.haine.dagger.module.ContextModule

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

        const val BASE_URL = "http://192.168.1.50:1753"
    }
}