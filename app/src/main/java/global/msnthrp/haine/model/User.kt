package global.msnthrp.haine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import global.msnthrp.haine.App
import java.io.Serializable

/**
 * Created by msnthrp on 22/01/18.
 */

data class User(
        @SerializedName("id")
        @Expose
        val id: Int,

        @SerializedName("name")
        @Expose
        val name: String,

        @SerializedName("last_seen")
        @Expose
        val lastSeen: Int
) : Serializable {

    fun photoUrl() = App.BASE_URL + "/user.avatar/$id"

}