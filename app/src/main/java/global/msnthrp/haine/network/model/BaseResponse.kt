package global.msnthrp.haine.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by msnthrp on 22/01/18.
 */
data class BaseResponse<T>(
        @SerializedName("result")
        @Expose
        val result: T?,

        @SerializedName("error")
        @Expose
        val errorCode: Int? = 0,

        @SerializedName("message")
        @Expose
        val errorMessage: String?
)