package global.msnthrp.haine.chat.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Handler
import android.os.SystemClock
import android.os.Vibrator
import android.support.v4.app.NotificationCompat
import android.text.Html
import global.msnthrp.haine.App
import global.msnthrp.haine.R
import global.msnthrp.haine.chat.ChatBus
import global.msnthrp.haine.db.DbHelper
import global.msnthrp.haine.extensions.subscribeSmart
import global.msnthrp.haine.login.LoginActivity
import global.msnthrp.haine.model.ExchangeRequest
import global.msnthrp.haine.model.Message
import global.msnthrp.haine.network.ApiService
import global.msnthrp.haine.storage.Lg
import global.msnthrp.haine.storage.Prefs
import global.msnthrp.haine.storage.Session
import global.msnthrp.haine.utils.DH_BITS
import global.msnthrp.haine.utils.getRestartIntent
import global.msnthrp.haine.utils.startService
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

    private val handler = Handler()
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
                    postPolling()
                }, { error ->
                    l("polling error: $error")
                    handler.postDelayed(::postPolling, INTERNET_DELAY)
                })
    }

    private fun sendResult(messages: List<Message>) {
        if (messages.isNotEmpty()) {
            session.lastMessage = messages.last().id
            ChatBus.publishMessage(messages)
        }
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

    private fun showNotification(content: String = getString(R.string.hidden_content),
                                 title: String = getString(R.string.appName),
                                 icon: Bitmap = BitmapFactory.decodeResource(resources, R.mipmap.haine_128)) {
        val intent = getRestartIntent(this) //Intent(this, LoginActivity::class.java)
        val pIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        val mBuilder = NotificationCompat.Builder(this)
                .setLargeIcon(icon)
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle(title)
                .setContentText(Html.fromHtml(content))
                .setContentIntent(pIntent)

        val mNotifyMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotifyMgr.notify(NOTIFICATION, mBuilder.build())
    }

    private fun closeNotification() {
        val mNotifyMgr = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotifyMgr.cancel(NOTIFICATION)
    }

    private fun vibrate() {
        val vi = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vi.vibrate(VIBRATE_DELAY)
    }

    private fun playRingtone() {
        RingtoneManager.getRingtone(
                applicationContext,
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        ).play()
    }

    private fun l(s: String) {
        Lg.i("[service] $s")
    }

    companion object {
        const val INTERNET_DELAY = 5000L
        const val NO_TOKEN_DELAY = 1000L

        const val VIBRATE_DELAY = 200L
        const val NOTIFICATION = 1753
    }
}