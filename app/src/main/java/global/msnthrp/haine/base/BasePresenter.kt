package global.msnthrp.haine.base

import global.msnthrp.haine.network.ApiService
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by msnthrp on 22/01/18.
 */
open class BasePresenter<V : BaseView>(protected open val view: V,
                                       protected open val api: ApiService) {

    protected val compositeDisposable = CompositeDisposable()

    protected fun defaultError(block: () -> Unit = {}) = { error: String ->
        view.onHideLoading()
        view.onShowError(error)
        block.invoke()
    }

    fun destroy() {
        compositeDisposable.dispose()
    }
}