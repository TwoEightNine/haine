package global.msnthrp.messenger.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import global.msnthrp.messenger.model.ExchangeParams
import global.msnthrp.messenger.model.Message

/**
 * Created by twoeightnine on 2/5/18.
 */
data class PollResponse(
        @SerializedName("messages")
        @Expose
        val messages: List<Message>,

        @SerializedName("exchanges")
        @Expose
        val exchanges: List<ExchangeParams>
)