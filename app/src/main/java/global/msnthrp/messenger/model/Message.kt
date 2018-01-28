package global.msnthrp.messenger.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by msnthrp on 22/01/18.
 */

data class Message(
        @SerializedName("id")
        @Expose
        val id: Int,

        @SerializedName("body")
        @Expose
        val body: String,

        @SerializedName("date")
        @Expose
        val date: Int,

        @SerializedName("out")
        @Expose
        val out: Boolean,

        @SerializedName("peer_id")
        @Expose
        val peerId: Int,

        @SerializedName("sticker_id")
        @Expose
        val stickerId: Int?
) {
        var user: User? = null
}