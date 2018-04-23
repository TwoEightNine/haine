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
import global.msnthrp.haine.model.Sticker
import global.msnthrp.haine.utils.ApiUtils
import java.util.ArrayList
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

        val stickersQty = arguments.getInt(STICKER_QTY)
        adapter.addAll(List(stickersQty, { Sticker(it) }))
    }

    companion object {

        fun newInstance(stickersQty: Int): StickersFragment {
            val frag = StickersFragment()
            frag.arguments.putInt(STICKER_QTY, stickersQty)
            return frag
        }
        const val GRID_COLUMNS_COUNT = 4
        const val STICKER_QTY = "stickerQty"
    }
}