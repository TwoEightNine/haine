package global.msnthrp.messenger.chat

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import global.msnthrp.messenger.R
import global.msnthrp.messenger.base.BaseAdapter
import global.msnthrp.messenger.dialogs.Message
import global.msnthrp.messenger.extensions.loadUrl
import global.msnthrp.messenger.extensions.view
import global.msnthrp.messenger.utils.ApiUtils
import global.msnthrp.messenger.utils.getTime

/**
 * Created by msnthrp on 22/01/18.
 */
class ChatAdapter(context: Context,
                  apiUtils: ApiUtils) : BaseAdapter<Message, ChatAdapter.ChatViewHolder>(context) {

    private val stickers = apiUtils.getStickers()

    override fun onCreateViewHolder(parent: ViewGroup?,
                                    viewType: Int): ChatViewHolder {
        val messageResource = when (viewType) {
            TYPE_OUT -> R.layout.item_message_out
            else -> R.layout.item_message_in
        }
        return ChatViewHolder(inflater.inflate(messageResource, null))
    }

    override fun onBindViewHolder(holder: ChatViewHolder?, position: Int) {
        if (holder == null) return

        val message = items[position]
        holder.tvBody.text = message.body
        holder.tvDate.text = getTime(message.date, true)
        if (message.stickerId != null) {
            holder.ivSticker.loadUrl(context, stickers.find { it.id == message.stickerId }?.url)
        }
    }

    override fun getItemViewType(position: Int) = if (items[position].out) TYPE_OUT else TYPE_IN

    companion object {
        val TYPE_OUT = 1753
        val TYPE_IN = 17053
    }

    inner class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvBody: TextView by view(R.id.tvBody)
        val tvDate: TextView by view(R.id.tvDate)
        val ivSticker: ImageView by view(R.id.ivSticker)

        init {

        }
    }
}