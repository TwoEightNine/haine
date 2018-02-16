package global.msnthrp.haine.dialogs

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView
import global.msnthrp.haine.R
import global.msnthrp.haine.base.BaseAdapter
import global.msnthrp.haine.extensions.loadUrl
import global.msnthrp.haine.extensions.setVisible
import global.msnthrp.haine.extensions.view
import global.msnthrp.haine.model.Message
import global.msnthrp.haine.storage.Session
import global.msnthrp.haine.utils.getTime

/**
 * Created by msnthrp on 22/01/18.
 */
class DialogsAdapter(context: Context,
                     private val session: Session,
                     private val onClick: (Int, Message) -> Unit = { _, _ -> }) : BaseAdapter<Message, DialogsAdapter.DialogViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup?,
                                    viewType: Int) = DialogViewHolder(inflater.inflate(R.layout.item_dialog, null))

    override fun onBindViewHolder(holder: DialogViewHolder?, position: Int) {
        if (holder == null) return

        val message = items[position]
        holder.tvTitle.text = message.user?.name
        holder.tvDate.text = getTime(message.time)
        holder.civPhoto.loadUrl(context, message.user?.photo)
        holder.ivUnread.setVisible(session.lastRead < message.id)
    }

    inner class DialogViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val tvTitle: TextView by view(R.id.tvTitle)
        val civPhoto: CircleImageView by view(R.id.civPhoto)
        val tvDate: TextView by view(R.id.tvDate)
        val rlItem: RelativeLayout by view(R.id.rlItemContainer)
        val ivUnread: ImageView by view(R.id.ivUnread)

        init {
            rlItem.setOnClickListener { onClick.invoke(adapterPosition, items[adapterPosition]) }
        }
    }

}