package global.msnthrp.messenger.base

import global.msnthrp.messenger.network.ApiService

/**
 * Created by msnthrp on 22/01/18.
 */
open class BasePresenter<V : BaseView>(open protected val view: V,
                                       open protected val api: ApiService) {

    protected fun defaultError(block: () -> Unit = {}) = { error: String ->
        view.onHideLoading()
        view.onShowError(error)
        block.invoke()
    }
}