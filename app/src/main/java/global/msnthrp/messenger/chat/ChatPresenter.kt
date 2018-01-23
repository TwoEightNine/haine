package global.msnthrp.messenger.chat

import global.msnthrp.messenger.base.BasePresenter
import global.msnthrp.messenger.dialogs.Message
import global.msnthrp.messenger.extensions.subscribeSmart
import global.msnthrp.messenger.network.ApiService
import global.msnthrp.messenger.profile.User
import global.msnthrp.messenger.utils.time
import java.util.concurrent.TimeUnit

/**
 * Created by msnthrp on 22/01/18.
 */
class ChatPresenter(view: ChatView,
                    api: ApiService,
                    private val user: User) : BasePresenter<ChatView>(view, api) {

    fun sendMessage(text: String) {
        api.sendMessage(text, user.id)
                .subscribeSmart({
                    view.onMessageSent(Message(it, text, time(), true, user.id))
                }, defaultError {
                    view.onMessageSent(Message(0, text, 0, false, 0))
                })
    }

    fun loadDialogs() {
        view.onShowLoading()
        api.getMessages(user.id)
                .delay(5L, TimeUnit.SECONDS)
                .repeat()
                .subscribeSmart({
                    view.onHideLoading()
                    view.onMessagesLoaded(it.reversed() as ArrayList<Message>)
                }, defaultError())
    }
}