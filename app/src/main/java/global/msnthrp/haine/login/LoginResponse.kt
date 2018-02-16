package global.msnthrp.haine.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by msnthrp on 22/01/18.
 */
data class LoginResponse(
        @SerializedName("token")
        @Expose
        val token: String,

        @SerializedName("id")
        @Expose
        val userId: Int
)