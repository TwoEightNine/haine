package global.msnthrp.haine.chat.stickers

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import global.msnthrp.haine.R
import global.msnthrp.haine.base.BaseAdapter
import global.msnthrp.haine.extensions.loadUrl
import global.msnthrp.haine.extensions.view
import global.msnthrp.haine.model.Sticker

/**
 * Created by msnthrp on 28/01/18.
 */
class StickersAdapter(context: Context,
                      private val onClick: (Sticker) -> Unit = {}) : BaseAdapter<Sticker, StickersAdapter.StickerViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup?,
                                    viewType: Int) = StickerViewHolder(inflater.inflate(R.layout.item_sticker, null))

    override fun onBindViewHolder(holder: StickerViewHolder?, position: Int) {
        if (holder == null) return

        val sticker = items[position]
        holder.ivSticker.loadUrl(context, sticker.stickerUrl())
    }

    inner class StickerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val ivSticker: ImageView by view(R.id.ivSticker)

        init {
            view.setOnClickListener { onClick.invoke(items[adapterPosition]) }
        }

    }
}