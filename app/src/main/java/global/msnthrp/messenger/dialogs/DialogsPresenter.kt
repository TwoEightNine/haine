package global.msnthrp.messenger.dialogs

import global.msnthrp.messenger.base.BasePresenter
import global.msnthrp.messenger.extensions.subscribeSmart
import global.msnthrp.messenger.network.ApiService
import global.msnthrp.messenger.profile.User

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