package global.msnthrp.messenger.search

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView
import global.msnthrp.messenger.R
import global.msnthrp.messenger.base.BaseAdapter
import global.msnthrp.messenger.extensions.loadUrl
import global.msnthrp.messenger.extensions.view
import global.msnthrp.messenger.model.User

/**
 * Created by msnthrp on 22/01/18.
 */
class SearchAdapter(context: Context) : BaseAdapter<User, SearchAdapter.SearchViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup?,
                                    viewType: Int) = SearchViewHolder(inflater.inflate(R.layout.item_search, null))

    override fun onBindViewHolder(holder: SearchViewHolder?, position: Int) {
        if (holder == null) return

        val user = items[position]
        holder.tvName.text = user.name
        holder.civPhoto.loadUrl(context, user.photo)
    }

    inner class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvName: TextView by view(R.id.tvName)
        val civPhoto: CircleImageView by view(R.id.civPhoto)

        init {

        }

    }

}