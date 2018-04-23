package global.msnthrp.haine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import global.msnthrp.haine.utils.time

/**
 * Created by msnthrp on 22/01/18.
 */

data class Message(
        @SerializedName("id")
        @Expose
        val id: Int,

        @SerializedName("text")
        @Expose
        var text: String,

        @SerializedName("time")
        @Expose
        val time: Int,

        @SerializedName("out")
        @Expose
        val out: Boolean,

        @SerializedName("peer_id")
        @Expose
        val peerId: Int,

        @SerializedName("attachment")
        @Expose
        val attachment: String?,

        @SerializedName("sticker_id")
        @Expose
        val stickerId: Int = 0
) {
        var user: User? = null

    fun hasAttachments() = attachment != null

    fun hasSticker() = stickerId != 0

        companion object {
            fun getNotSent(text: String, stickerId: Int = 0) = Message(
                    0, text, 0, false, 0, null, stickerId
            )

            fun getSentText(id: Int, text: String, userId: Int) = Message(
                    id, text, time(), true, userId, null
            )

            fun getSentAttachment(id: Int, link: String, userId: Int) = Message(
                    id, "", time(), true, userId, link
            )

            fun getSentSticker(id: Int, stickerId: Int, userId: Int) = Message(
                    id, "", time(), true, userId, null, stickerId
            )
        }
}