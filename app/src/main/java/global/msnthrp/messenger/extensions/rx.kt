package global.msnthrp.messenger.extensions

import global.msnthrp.messenger.network.model.BaseResponse
import global.msnthrp.messenger.utils.applySchedulersFlowable
import global.msnthrp.messenger.utils.applySchedulersSingle
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by msnthrp on 22/01/18.
 */

fun <T> Single<BaseResponse<T>>.subscribeSmart(onSuccess: (T) -> Unit,
                                               onError: (String) -> Unit) {
    this.compose(applySchedulersSingle())
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

fun <T> Flowable<BaseResponse<T>>.subscribeSmart(onSuccess: (T) -> Unit,
                                               onError: (String) -> Unit) {
    this.compose(applySchedulersFlowable())
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