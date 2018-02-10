package global.msnthrp.messenger.chat.service

import android.app.Service
import android.content.Intent
import android.os.SystemClock
import global.msnthrp.messenger.App
import global.msnthrp.messenger.chat.ChatBus
import global.msnthrp.messenger.db.DbHelper
import global.msnthrp.messenger.extensions.subscribeSmart
import global.msnthrp.messenger.model.ExchangeParams
import global.msnthrp.messenger.model.ExchangeRequest
import global.msnthrp.messenger.model.Message
import global.msnthrp.messenger.network.ApiService
import global.msnthrp.messenger.storage.Lg
import global.msnthrp.messenger.storage.Prefs
import global.msnthrp.messenger.storage.Session
import global.msnthrp.messenger.utils.DH_BITS
import global.msnthrp.messenger.utils.isOnline
import global.msnthrp.messenger.utils.startService
import java.math.BigInteger
import java.security.SecureRandom
import javax.inject.Inject

/**
 * Created by msnthrp on 28/01/18.
 */
class NotificationService : Service() {

    @Inject
    lateinit var session: Session
    @Inject
    lateinit var prefs: Prefs
    @Inject
    lateinit var api: ApiService
    @Inject
    lateinit var dbHelper: DbHelper

    private var myPrime = ""
    private var myId = 0
    private var lastMessage = 0
    private var lastXchg = 0L
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
        lastXchg = session.lastXchg
        myId = session.userId
        myPrime = prefs.safePrime
    }

    private fun poll() {
        api.poll(lastMessage, lastXchg)
                .subscribeSmart({ pollResponse ->

                    l("updates: ${pollResponse.messages.size} ${pollResponse.exchanges.size}")
                    processExchange(pollResponse.exchanges)
                    sendResult(pollResponse.messages)
                }, { error ->
                    l("polling error: $error")
                    SystemClock.sleep(INTERNET_DELAY)
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

    private fun processExchange(exchanges: List<ExchangeRequest>) {
        if (exchanges.isNotEmpty()) {
            session.lastXchg = exchanges[0].lastUpd
        }
        exchanges
                .filter { it.lastEditor != myId }
                .forEach { exchange ->
            if (exchange.isDebut()) {
                supportExchange(exchange)
            } else {
                finishExchange(exchange)
            }
        }
    }

    /** This method confirms that someone supported our params and sent us its own */
    private fun finishExchange(exchangeRequest: ExchangeRequest) {
        val exchangeParams = exchangeRequest.toParams(myId)
        val storedParams = dbHelper.db.exchangeDao.queryForId(exchangeParams.id) ?: return

        val p = BigInteger(storedParams.p)

        val privateOwn = BigInteger(storedParams.privateOwn)
        val publicOther = BigInteger(exchangeParams.publicOther)
        val shared = publicOther.modPow(privateOwn, p)

        storedParams.publicOther = exchangeParams.publicOther
        storedParams.shared = shared.toString()

        dbHelper.db.exchangeDao.createOrUpdate(storedParams)
        ChatBus.publishExchange(shared.toString())
    }

    /** This method is being run when someone sent us params at first time */
    private fun supportExchange(exchangeRequest: ExchangeRequest) {
        val exchangeParams = exchangeRequest.toParams(myId)
        val p = BigInteger(exchangeParams.p)
        val g = BigInteger(exchangeParams.g)

        val privateOwn = BigInteger(DH_BITS, SecureRandom())
        val publicOwn = g.modPow(privateOwn, p)
        val publicOther = BigInteger(exchangeParams.publicOther)
        val shared = publicOther.modPow(privateOwn, p)

        api.commitExchange(p.toString(), g.toString(), publicOwn.toString(), exchangeParams.id)
                .subscribeSmart({
                    exchangeParams.privateOwn = privateOwn.toString()
                    exchangeParams.publicOwn = publicOwn.toString()
                    exchangeParams.shared = shared.toString()

                    dbHelper.db.exchangeDao.createOrUpdate(exchangeParams)
                    ChatBus.publishExchange(shared.toString())
                }, {
                    Lg.wtf("error supporting: $it")
                })
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