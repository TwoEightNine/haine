package global.msnthrp.haine.dialogs

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import global.msnthrp.haine.model.Message
import global.msnthrp.haine.model.User

/**
 * Created by msnthrp on 22/01/18.
 */
data class DialogResponse(
        @SerializedName("messages")
        @Expose
        val messages: List<Message>,

        @SerializedName("users")
        @Expose
        val users: List<User>
)