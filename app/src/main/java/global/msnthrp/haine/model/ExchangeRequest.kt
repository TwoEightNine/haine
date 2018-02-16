package global.msnthrp.haine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by twoeightnine on 2/6/18.
 */
data class ExchangeRequest(

        @SerializedName("p")
        @Expose
        val p: String,

        @SerializedName("g")
        @Expose
        val g: String,

        @SerializedName("from_id")
        @Expose
        val fromId: Int = 0,

        @SerializedName("to_id")
        @Expose
        val toId: Int = 0,

        @SerializedName("public_from")
        @Expose
        val publicFrom: String,

        @SerializedName("public_to")
        @Expose
        val publicTo: String? = "",

        @SerializedName("last_upd")
        @Expose
        val lastUpd: Long = 0L,

        @SerializedName("last_editor")
        @Expose
        val lastEditor: Int = 0
) {
        fun isDebut() = publicTo.isNullOrEmpty()

        fun toParams(myId: Int) = ExchangeParams(
                if (myId == fromId) toId else fromId,
                p, g, "",
                if (myId == fromId) publicFrom else publicTo ?: "",
                if (myId == fromId) publicTo ?: "" else publicFrom,
                initByUs = if (myId == fromId) 1 else 0
        )
}