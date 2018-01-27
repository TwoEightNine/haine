package global.msnthrp.messenger.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

/**
 * Created by msnthrp on 27/01/18.
 */
@DatabaseTable(tableName = "sticker")
data class Sticker(
        @DatabaseField(id = true)
        @SerializedName("id")
        @Expose
        val id: Int,

        @DatabaseField
        @SerializedName("url")
        @Expose
        val url: String
) {
        constructor() : this(0, "")
}