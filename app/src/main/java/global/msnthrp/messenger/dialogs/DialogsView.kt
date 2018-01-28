package global.msnthrp.messenger.dialogs

import global.msnthrp.messenger.base.BaseView
import global.msnthrp.messenger.model.Message

/**
 * Created by msnthrp on 22/01/18.
 */
interface DialogsView : BaseView {
    fun onDialogLoaded(dialogs: ArrayList<Message>)
}