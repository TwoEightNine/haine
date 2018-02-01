package global.msnthrp.messenger.chat

import global.msnthrp.messenger.base.BasePresenter
import global.msnthrp.messenger.model.Message
import global.msnthrp.messenger.extensions.subscribeSmart
import global.msnthrp.messenger.model.Sticker
import global.msnthrp.messenger.network.ApiService
import global.msnthrp.messenger.model.User
import global.msnthrp.messenger.utils.time

/**
 * Created by msnthrp on 22/01/18.
 */
class ChatPresenter(view: ChatView,
                    api: ApiService,
                    private val user: User) : BasePresenter<ChatView>(view, api) {

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        api.sendMessage(text, user.id)
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
                    if (messages.isNotEmpty()) {
                        view.onMessagesLoaded((messages as ArrayList<Message>))
                    }
                }, defaultError())
    }
}