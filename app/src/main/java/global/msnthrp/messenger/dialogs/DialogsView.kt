package global.msnthrp.messenger.dialogs

import global.msnthrp.messenger.base.BaseView

/**
 * Created by msnthrp on 22/01/18.
 */
interface DialogsView : BaseView {
    fun onDialogLoaded(dialogs: ArrayList<Message>)
}