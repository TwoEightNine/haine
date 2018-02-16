package global.msnthrp.haine.chat

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import global.msnthrp.haine.R
import global.msnthrp.haine.base.BaseFragment
import global.msnthrp.haine.extensions.*
import global.msnthrp.haine.utils.isUrl
import global.msnthrp.haine.view.TouchImageView

/**
 * Created by twoeightnine on 2/14/18.
 */
class PhotoFragment : BaseFragment() {

    private val touchImageView: TouchImageView by view(R.id.touchImageView)
    private val tvTitle: TextView by view(R.id.tvTitle)
    private val rlTitle: RelativeLayout by view(R.id.rlTitle)

    override fun getLayoutId() = R.layout.fragment_photo

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val path = arguments.getString(PATH) ?: return
        val name = arguments.getString(NAME) ?: return
        tvTitle.text = if (name.isNotEmpty()) name else path
        touchImageView.loadUrl(context, getFormattedPath(path))
        touchImageView.dismissListener = {
            activity.onBackPressed()
        }
        touchImageView.tapListener = { rlTitle.toggleVisibility() }
    }

    private fun getFormattedPath(path: String) = if (isUrl(path)) path else "file://$path"

    companion object {
        const val PATH = "path"
        const val NAME = "name"

        fun newInstance(path: String, name: String = ""): PhotoFragment {
            val fragment = PhotoFragment()
            val extras = Bundle()
            extras.putString(PATH, path)
            extras.putString(NAME, name)
            fragment.arguments = extras
            return fragment
        }
    }
}