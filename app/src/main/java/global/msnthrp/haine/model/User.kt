package global.msnthrp.haine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
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

        @SerializedName("photo")
        @Expose
        val photo: String?,

        @SerializedName("last_seen")
        @Expose
        val lastSeen: Int
) : Serializable