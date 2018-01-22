package global.msnthrp.messenger.dialogs

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import global.msnthrp.messenger.R
import global.msnthrp.messenger.base.BaseAdapter
import global.msnthrp.messenger.extensions.loadUrl
import global.msnthrp.messenger.extensions.view
import global.msnthrp.messenger.utils.getTime

/**
 * Created by msnthrp on 22/01/18.
 */
class DialogsAdapter(context: Context,
                     private val onClick: (Int, Message) -> Unit = { _,_ -> }) : BaseAdapter<Message, DialogsAdapter.DialogViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup?,
                                    viewType: Int) = DialogViewHolder(inflater.inflate(R.layout.item_dialog, null))

    override fun onBindViewHolder(holder: DialogViewHolder?, position: Int) {
        if (holder == null) return

        val message = items[position]
        holder.tvTitle.text = message.user?.name
        holder.tvBody.text = message.body
        holder.tvDate.text = getTime(message.date)
        holder.civPhoto.loadUrl(context, message.user?.photo)
    }

    inner class DialogViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val tvTitle: TextView by view(R.id.tvTitle)
        val tvBody: TextView by view(R.id.tvBody)
        val civPhoto: CircleImageView by view(R.id.civPhoto)
        val tvDate: TextView by view(R.id.tvDate)
        val rlItem: RelativeLayout by view(R.id.rlItemContainer)

        init {
            rlItem.setOnClickListener { onClick.invoke(adapterPosition, items[adapterPosition]) }
        }
    }

}