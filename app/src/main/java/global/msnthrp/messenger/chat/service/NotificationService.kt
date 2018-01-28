package global.msnthrp.messenger.chat.service

import android.app.Service
import android.content.Intent
import android.os.SystemClock
import global.msnthrp.messenger.App
import global.msnthrp.messenger.chat.ChatBus
import global.msnthrp.messenger.extensions.subscribeSmart
import global.msnthrp.messenger.model.Message
import global.msnthrp.messenger.network.ApiService
import global.msnthrp.messenger.storage.Lg
import global.msnthrp.messenger.storage.Session
import global.msnthrp.messenger.utils.isOnline
import global.msnthrp.messenger.utils.startService
import javax.inject.Inject

/**
 * Created by msnthrp on 28/01/18.
 */
class NotificationService : Service() {

    @Inject
    lateinit var session: Session
    @Inject
    lateinit var api: ApiService

    private var lastMessage = 0
    private var isRunning = false

    override fun onBind(intent: Intent?) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!isRunning) {
            Thread {
                isRunning = true
                initPrefs()
                if (session.token.isNotEmpty()) {
                    poll()
                } else {
                    SystemClock.sleep(NO_TOKEN_DELAY)
                }
            }.start()
        }
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        App.appComponent.inject(this)
        l("onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        startService(this)
        l("onDestroy")
    }

    private fun initPrefs() {
        lastMessage = session.lastMessage
    }

    private fun poll() {
        api.poll(lastMessage)
                .subscribeSmart({ messages ->
                    l("updates: ${messages.size}")
                    sendResult(messages)
                }, { error ->
                    l("polling error: $error")
                    if (!isOnline(this) || "Auth" in error) {
                        SystemClock.sleep(INTERNET_DELAY)
                    }
                    postPolling()
                })
    }

    private fun sendResult(messages: List<Message>) {
        if (messages.isNotEmpty()) {
            session.lastMessage = messages[0].id
            ChatBus.publishMessage(messages)
        }
        postPolling()
    }

    private fun postPolling() {
        isRunning = false
        startService(this)
    }

    private fun l(s: String) {
        Lg.i("[service] $s")
    }

    companion object {
        const val INTERNET_DELAY = 5000L
        const val NO_TOKEN_DELAY = 1000L
    }
}