package global.msnthrp.haine.chat

import global.msnthrp.haine.base.BasePresenter
import global.msnthrp.haine.db.DbHelper
import global.msnthrp.haine.model.Message
import global.msnthrp.haine.extensions.subscribeSmart
import global.msnthrp.haine.model.ExchangeParams
import global.msnthrp.haine.model.Sticker
import global.msnthrp.haine.network.ApiService
import global.msnthrp.haine.model.User
import global.msnthrp.haine.storage.Lg
import global.msnthrp.haine.storage.Session
import global.msnthrp.haine.utils.*
import java.io.File

/**
 * Created by msnthrp on 22/01/18.
 */
class ChatPresenter(view: ChatView,
                    api: ApiService,
                    private val session: Session,
                    private val apiUtils: ApiUtils,
                    private val dbHelper: DbHelper,
                    private val user: User) : BasePresenter<ChatView>(view, api) {

    private var crypto: Cryptool? = null

    var isFingerprintChecked = false
        set(value) {
            field = value
            view.onFingerPrintUpdated(value)
        }

    init {
        compositeDisposable.add(ChatBus.subscribeMessage(::onMessagesAdded))
        compositeDisposable.add(ChatBus.subscribeExchange { checkForExchanges() })
        compositeDisposable.addAll(ChatBus.subscribeSticker(::sendSticker))
        checkForExchanges()
    }

    fun sendMessage(text: String) {
        if (text.isBlank() || crypto == null) return

        if (isUrl(text)) {
            api.sendAttachments(text, user.id)
                    .subscribeSmart({
                        view.onMessageSent(Message.getSentAttachment(it, text, user.id))
                    }, defaultError {
                        view.onMessageSent(Message.getNotSent(text))
                    })
        } else {
            val encrypted = crypto!!.encrypt(text)
            api.sendMessage(encrypted, user.id)
                    .subscribeSmart({
                        view.onMessageSent(Message.getSentText(it, text, user.id))
                    }, defaultError {
                        view.onMessageSent(Message.getNotSent(text))
                    })
        }
    }

    fun sendFile(path: String) {
        if (path.isBlank() || !File(path).exists()
                || crypto == null) return

        view.onShowLoading()
        crypto!!.encryptFile(path) { encPath ->
            apiUtils.uploadFile(encPath, { link ->
                sendMessage(link)
                File(encPath).delete()
                view.onHideLoading()
            }, defaultError())
        }
    }

    fun sendSticker(sticker: Sticker) {
        api.sendSticker(sticker.id, user.id)
                .subscribeSmart({
                    view.onMessageSent(Message.getSentSticker(it, sticker.id, user.id))
                }, defaultError {
                    view.onMessageSent(Message.getNotSent("", sticker.id))
                })
    }

    fun addSticker(path: String) {
        if (path.isBlank() || !File(path).exists()) return

        view.onShowLoading()
        api.uploadSticker(toBase64(getBytesFromFile(path)))
                .subscribeSmart({
                    view.onHideLoading()
                    view.onStickerAdded(it)
                }, defaultError())
    }

    fun openFile(link: String) {
        if (crypto == null) return

        view.onShowLoading()
        apiUtils.downloadFile(link, { path ->
            crypto!!.decryptFile(path) { decPath ->
                File(path).delete()
                view.onHideLoading()
                view.onFileAvailable(decPath)
            }
        }, defaultError())
    }

    fun loadDialogs() {
        view.onShowLoading()
        api.getMessages(user.id)
                .subscribeSmart({ messages ->
                    view.onHideLoading()
                    with(messages) {
                        reverse()
                        decrypt()
                    }
                    if (messages.isNotEmpty()) {
                        view.onMessagesLoaded(messages)
                    }
                }, defaultError())
    }

    fun getFingerPrint() = crypto?.getFingerPrint()

    fun createNewExchange() {
        view.onShowLoading()
        apiUtils.createExchange(user.id) {
            view.onHideLoading()
        }
    }

    private fun checkForExchanges() {
        val params = dbHelper.db.exchangeDao.queryForId(user.id)
        if (params == null) {
            if (session.userId == user.id) {
                val newParams = ExchangeParams(
                        session.userId, "0", "0", "0", "0", "0",
                        sha256(randomString())
                )
                dbHelper.db.exchangeDao.createOrUpdate(newParams)
                applyExchange(newParams)
            } else {
                apiUtils.createExchange(user.id) {}
            }
        } else if (!params.isDebut()) {
            applyExchange(params)
        }
    }

    private fun applyExchange(params: ExchangeParams) {
        view.onSendingAllowed()
        crypto = Cryptool(params.shared)
        Lg.i("fingerprint = ${crypto?.getFingerPrint()}")
        isFingerprintChecked = false
    }

    private fun onMessagesAdded(messages: List<Message>) {
        with(messages) {
            decrypt()
        }
        view.onMessagesAdded(messages)
    }

    private fun List<Message>.decrypt(): List<Message> {
        if (crypto == null) return this

        forEach {
            it.text = crypto!!.decrypt(it.text)
        }
        return this
    }
}