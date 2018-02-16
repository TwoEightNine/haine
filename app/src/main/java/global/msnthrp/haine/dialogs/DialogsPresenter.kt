package global.msnthrp.haine.dialogs

import global.msnthrp.haine.base.BasePresenter
import global.msnthrp.haine.extensions.subscribeSmart
import global.msnthrp.haine.model.Message
import global.msnthrp.haine.network.ApiService
import global.msnthrp.haine.model.User
import java.util.*

/**
 * Created by msnthrp on 22/01/18.
 */
class DialogsPresenter(api: ApiService,
                       view: DialogsView) : BasePresenter<DialogsView>(view, api) {

    fun loadDialogs() {
        view.onShowLoading()
        api.getDialogs()
                .subscribeSmart({ response ->
                    view.onHideLoading()
                    val messages = response.messages
                    val dict = hashMapOf<Int, User>()
                    response.users.forEach { dict.put(it.id, it) }
                    messages.forEach { it.user = dict[it.peerId] }
                    view.onDialogLoaded(messages as ArrayList<Message>)
                }, defaultError())
    }

}