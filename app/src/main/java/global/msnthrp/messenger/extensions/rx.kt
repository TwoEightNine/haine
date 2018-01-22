package global.msnthrp.messenger.extensions

import global.msnthrp.messenger.network.model.BaseResponse
import io.reactivex.Single

/**
 * Created by msnthrp on 22/01/18.
 */

fun <T> Single<BaseResponse<T>>.subscribeSmart(onSuccess: (T) -> Unit,
                                               onError: (String) -> Unit) {
    this.compose(global.msnthrp.messenger.utils.applySchedulers())
            .subscribe({ response ->
                if (response.result != null) {
                    onSuccess.invoke(response.result)
                } else if (response.errorCode != 0 && response.errorMessage != null) {
                    onError.invoke(response.errorMessage)
                }
            }, {
                onError.invoke(it.message ?: "Unknown error")
            })
}