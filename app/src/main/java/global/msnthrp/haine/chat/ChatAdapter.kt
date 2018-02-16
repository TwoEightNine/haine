package global.msnthrp.haine.chat

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import global.msnthrp.haine.R
import global.msnthrp.haine.base.BaseAdapter
import global.msnthrp.haine.extensions.setVisible
import global.msnthrp.haine.model.Message
import global.msnthrp.haine.extensions.view
import global.msnthrp.haine.utils.ApiUtils
import global.msnthrp.haine.utils.getTime

/**
 * Created by msnthrp on 22/01/18.
 */
class ChatAdapter(context: Context,
                  apiUtils: ApiUtils,
                  private val onAttachmentClick: ((String) -> Unit)? = {}) : BaseAdapter<Message, ChatAdapter.ChatViewHolder>(context) {

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
        holder.tvBody.text = message.text
        holder.tvDate.text = getTime(message.time, true)
        val hasAttached = message.attachment != null
        holder.rlAttachment.setVisible(hasAttached)
        if (hasAttached) {
            holder.tvAttachment.text = message.attachment
        }
    }

    override fun getItemViewType(position: Int) = if (items[position].out) TYPE_OUT else TYPE_IN

    companion object {
        const val TYPE_OUT = 1753
        const val TYPE_IN = 17053
    }

    inner class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvBody: TextView by view(R.id.tvBody)
        val tvDate: TextView by view(R.id.tvDate)
        val rlAttachment: RelativeLayout by view(R.id.rlAttachment)
        val tvAttachment: TextView by view(R.id.tvAttachment)

        init {
            rlAttachment.setOnClickListener {
                onAttachmentClick?.invoke(items[adapterPosition].attachment ?: return@setOnClickListener)
            }
        }
    }
}