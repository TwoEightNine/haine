package global.msnthrp.haine.chat.stickers

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import global.msnthrp.haine.App
import global.msnthrp.haine.R
import global.msnthrp.haine.chat.ChatBus
import global.msnthrp.haine.extensions.view
import global.msnthrp.haine.utils.ApiUtils
import javax.inject.Inject

/**
 * Created by msnthrp on 28/01/18.
 */
class StickersFragment : Fragment() {

    @Inject
    lateinit var apiUtils: ApiUtils

    private val recyclerView: RecyclerView by view(R.id.recyclerView)

    private val adapter by lazy { StickersAdapter(context, ChatBus::publishSticker) }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) = View.inflate(context, R.layout.fragment_stickers, null)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        App.appComponent.inject(this)

        recyclerView.layoutManager = GridLayoutManager(context, GRID_COLUMNS_COUNT)
        recyclerView.adapter = adapter
        adapter.addAll(apiUtils.getStickers())
    }

    companion object {
        const val GRID_COLUMNS_COUNT = 4
    }
}