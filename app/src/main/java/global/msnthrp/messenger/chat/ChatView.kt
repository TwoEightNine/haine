package global.msnthrp.messenger.chat

import global.msnthrp.messenger.base.BaseView
import global.msnthrp.messenger.model.Message

/**
 * Created by msnthrp on 22/01/18.
 */
interface ChatView : BaseView {
    fun onMessagesLoaded(messages: ArrayList<Message>)
    fun onMessageSent(message: Message)
    fun onSendingAllowed()
    fun onMessagesAdded(messages: List<Message>)
}