package global.msnthrp.messenger.chat

import global.msnthrp.messenger.base.BasePresenter
import global.msnthrp.messenger.db.DbHelper
import global.msnthrp.messenger.model.Message
import global.msnthrp.messenger.extensions.subscribeSmart
import global.msnthrp.messenger.model.Sticker
import global.msnthrp.messenger.network.ApiService
import global.msnthrp.messenger.model.User
import global.msnthrp.messenger.storage.Lg
import global.msnthrp.messenger.utils.*
import java.io.File

/**
 * Created by msnthrp on 22/01/18.
 */
class ChatPresenter(view: ChatView,
                    api: ApiService,
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
        checkForExchanges()
    }

    fun sendMessage(text: String) {
        if (text.isBlank() || crypto == null) return

        if (isUrl(text)) {
            api.sendAttachments(text, user.id)
                    .subscribeSmart({
                        view.onMessageSent(Message.getSendedAttachment(it, text, user.id))
                    }, defaultError {
                        view.onMessageSent(Message.getNotSended(text))
                    })
        } else {
            val encrypted = crypto!!.encrypt(text)
            api.sendMessage(encrypted, user.id)
                    .subscribeSmart({
                        view.onMessageSent(Message.getSendedText(it, text, user.id))
                    }, defaultError {
                        view.onMessageSent(Message.getNotSended(text))
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
            apiUtils.createExchange(user.id) {}
        } else if (!params.isDebut()) {
            view.onSendingAllowed()
            crypto = Cryptool(params.shared)
            Lg.i("fingerprint = ${crypto?.getFingerPrint()}")
            isFingerprintChecked = false
        }
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