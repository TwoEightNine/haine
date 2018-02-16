package global.msnthrp.haine.chat

import android.os.Bundle
import android.view.View
import android.widget.TextView
import global.msnthrp.haine.R
import global.msnthrp.haine.base.BaseFragment
import global.msnthrp.haine.extensions.*
import global.msnthrp.haine.view.TouchImageView

/**
 * Created by twoeightnine on 2/14/18.
 */
class PhotoFragment : BaseFragment() {

    private val touchImageView: TouchImageView by view(R.id.touchImageView)
    private val tvTitle: TextView by view(R.id.tvTitle)

    override fun getLayoutId() = R.layout.fragment_photo

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val path = arguments.getString(PATH) ?: return
        touchImageView.loadUrl(context, "file://$path")
        touchImageView.dismissListener = {
            activity.onBackPressed()
        }
        touchImageView.tapListener = { tvTitle.toggleVisibility() }
    }

    companion object {
        const val PATH = "path"

        fun newInstance(path: String): PhotoFragment {
            val fragment = PhotoFragment()
            val extras = Bundle()
            extras.putString(PATH, path)
            fragment.arguments = extras
            return fragment
        }
    }
}