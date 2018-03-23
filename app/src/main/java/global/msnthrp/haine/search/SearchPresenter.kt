package global.msnthrp.haine.search

import global.msnthrp.haine.base.BasePresenter
import global.msnthrp.haine.extensions.subscribeSmart
import global.msnthrp.haine.network.ApiService
import global.msnthrp.haine.model.User

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