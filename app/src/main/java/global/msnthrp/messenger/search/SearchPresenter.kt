package global.msnthrp.messenger.search

import global.msnthrp.messenger.base.BasePresenter
import global.msnthrp.messenger.extensions.subscribeSmart
import global.msnthrp.messenger.network.ApiService
import global.msnthrp.messenger.profile.User

/**
 * Created by msnthrp on 22/01/18.
 */
class SearchPresenter(view: SearchView,
                      api: ApiService) : BasePresenter<SearchView>(view, api) {

    fun search(q: String) {
        view.onShowLoading()
        api.search(q)
                .subscribeSmart({ response ->
                    view.onHideLoading()
                    view.onUsersLoaded(response as ArrayList<User>)

                }, defaultError())
    }

}