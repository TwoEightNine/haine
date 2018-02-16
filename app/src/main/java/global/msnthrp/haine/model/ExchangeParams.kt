package global.msnthrp.haine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

/**
 * Created by twoeightnine on 2/5/18.
 */
@DatabaseTable(tableName = "exchange")
data class ExchangeParams(
        @DatabaseField(id = true)
        @SerializedName("id")
        @Expose
        val id: Int,

        @DatabaseField
        @SerializedName("p")
        @Expose
        val p: String,

        @DatabaseField
        @SerializedName("g")
        @Expose
        val g: String,

        @DatabaseField
        @SerializedName("priv_own")
        @Expose
        var privateOwn: String,

        @DatabaseField
        @SerializedName("pub_own")
        @Expose
        var publicOwn: String,

        @DatabaseField
        @SerializedName("pub_oth")
        @Expose
        var publicOther: String = "",

        @DatabaseField
        @SerializedName("shared")
        @Expose
        var shared: String = "",

        @DatabaseField
        @SerializedName("init")
        @Expose
        val initByUs: Int = 1
) {
        constructor() : this(0, "", "", "", "", "", "") // for ormlite

        fun toRequest(myId: Int) = ExchangeRequest(
                p, g,
                if (initByUs == 1) myId else id,
                if (initByUs == 1) id else myId,
                if (initByUs == 1) publicOwn else publicOther,
                if (initByUs == 1) publicOther else publicOwn
        )

        fun isDebut() = publicOther == "" && shared == ""
}