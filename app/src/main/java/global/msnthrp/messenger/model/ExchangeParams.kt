package global.msnthrp.messenger.model

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
        val privateOwn: String,

        @DatabaseField
        @SerializedName("pub_own")
        @Expose
        val publicOwn: String,

        @DatabaseField
        @SerializedName("pub_oth")
        @Expose
        val publicOther: String = "",

        @DatabaseField
        @SerializedName("shared")
        @Expose
        val shared: String = ""
) {
    constructor() : this(0, "", "", "", "", "", "") // for ormlite
}