package global.msnthrp.messenger.chat

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import global.msnthrp.messenger.R
import global.msnthrp.messenger.extensions.loadUrl
import global.msnthrp.messenger.extensions.view
import global.msnthrp.messenger.view.TouchImageView

/**
 * Created by twoeightnine on 2/14/18.
 */
class PhotoFragment : Fragment() {

    private val touchImageView: TouchImageView by view(R.id.touchImageView)

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) = View.inflate(context, R.layout.fragment_photo, null)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val path = arguments.getString(PATH) ?: return
        touchImageView.loadUrl(context, "file://$path")
        touchImageView.dismissListener = {
            activity.onBackPressed()
        }
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