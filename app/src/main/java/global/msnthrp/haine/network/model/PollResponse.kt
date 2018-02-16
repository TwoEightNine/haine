package global.msnthrp.haine.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import global.msnthrp.haine.model.ExchangeRequest
import global.msnthrp.haine.model.Message

/**
 * Created by twoeightnine on 2/5/18.
 */
data class PollResponse(
        @SerializedName("messages")
        @Expose
        val messages: List<Message>,

        @SerializedName("exchanges")
        @Expose
        val exchanges: List<ExchangeRequest>
)