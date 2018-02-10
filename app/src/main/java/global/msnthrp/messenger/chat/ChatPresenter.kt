package global.msnthrp.messenger.chat

import global.msnthrp.messenger.base.BasePresenter
import global.msnthrp.messenger.db.DbHelper
import global.msnthrp.messenger.model.Message
import global.msnthrp.messenger.extensions.subscribeSmart
import global.msnthrp.messenger.model.Sticker
import global.msnthrp.messenger.network.ApiService
import global.msnthrp.messenger.model.User
import global.msnthrp.messenger.storage.Lg
import global.msnthrp.messenger.utils.ApiUtils
import global.msnthrp.messenger.utils.Cryptool
import global.msnthrp.messenger.utils.time

/**
 * Created by msnthrp on 22/01/18.
 */
class ChatPresenter(view: ChatView,
                    api: ApiService,
                    private val apiUtils: ApiUtils,
                    private val dbHelper: DbHelper,
                    private val user: User) : BasePresenter<ChatView>(view, api) {

    init {
        compositeDisposable.add(ChatBus.subscribeMessage(::onMessagesAdded))
        compositeDisposable.add(ChatBus.subscribeExchange { checkForExchanges() })
        checkForExchanges()
    }

    private var crypto: Cryptool? = null

    fun sendMessage(text: String) {
        if (text.isBlank() || crypto == null) return

        val encrypted = crypto!!.encrypt(text)
        api.sendMessage(encrypted, user.id)
                .subscribeSmart({
                    view.onMessageSent(Message(it, text, time(), true, user.id, null))
                }, defaultError {
                    view.onMessageSent(Message(0, text, 0, false, 0, null))
                })
    }

    fun sendSticker(sticker: Sticker) {
        api.sendSticker(sticker.id, user.id)
                .subscribeSmart({
                    view.onMessageSent(Message(it, "", time(), true, user.id, sticker.id))
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

    private fun checkForExchanges() {
        val params = dbHelper.db.exchangeDao.queryForId(user.id)
        if (params == null) {
            apiUtils.createExchange(user.id) {}
        } else if (!params.isDebut()) {
            view.onSendingAllowed()
            crypto = Cryptool(params.shared)
            Lg.i("fingerprint = ${crypto?.getFingerPrint()}")
        }
    }

    private fun onMessagesAdded(messages: List<Message>) {
        with (messages) {
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