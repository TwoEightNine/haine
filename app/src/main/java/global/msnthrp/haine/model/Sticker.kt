package global.msnthrp.haine.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import global.msnthrp.haine.App

/**
 * Created by msnthrp on 27/01/18.
 */
@DatabaseTable(tableName = "sticker")
data class Sticker(
        @DatabaseField(id = true)
        @SerializedName("id")
        @Expose
        val id: Int
) {
    constructor() : this(0) // for ormlite

    fun stickerUrl() = App.BASE_URL + "/stickers.direct/$id"
}