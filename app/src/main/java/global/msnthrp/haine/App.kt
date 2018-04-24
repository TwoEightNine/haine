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

        const val BASE_URL = "http://172.30.1.18:1753" //"http://185.227.111.96:1753"
    }
}