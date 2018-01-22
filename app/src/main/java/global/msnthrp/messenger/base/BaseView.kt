package global.msnthrp.messenger.base

/**
 * Created by msnthrp on 22/01/18.
 */
interface BaseView {
    fun onShowLoading()
    fun onHideLoading()
    fun onShowError(error: String)
}