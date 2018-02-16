package global.msnthrp.haine.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by twoeightnine on 2/12/18.
 */
data class UploadResponse(
        @SerializedName("success")
        @Expose
        val success: Boolean,

        @SerializedName("link")
        @Expose
        val link: String,

        @SerializedName("message")
        @Expose
        val message: String
)