package global.msnthrp.haine.search

import global.msnthrp.haine.base.BaseView
import global.msnthrp.haine.model.User

/**
 * Created by msnthrp on 22/01/18.
 */
interface SearchView : BaseView {
    fun onUsersLoaded(users: ArrayList<User>)
}