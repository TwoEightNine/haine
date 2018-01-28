package global.msnthrp.messenger.search

import global.msnthrp.messenger.base.BaseView
import global.msnthrp.messenger.model.User

/**
 * Created by msnthrp on 22/01/18.
 */
interface SearchView : BaseView {
    fun onUsersLoaded(users: ArrayList<User>)
}