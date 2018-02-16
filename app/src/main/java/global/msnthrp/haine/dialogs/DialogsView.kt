package global.msnthrp.haine.dialogs

import global.msnthrp.haine.base.BaseView
import global.msnthrp.haine.model.Message

/**
 * Created by msnthrp on 22/01/18.
 */
interface DialogsView : BaseView {
    fun onDialogLoaded(dialogs: ArrayList<Message>)
}