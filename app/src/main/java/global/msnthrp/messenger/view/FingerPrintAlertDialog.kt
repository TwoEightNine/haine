package global.msnthrp.messenger.view

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import global.msnthrp.messenger.R
import global.msnthrp.messenger.extensions.loadUrl
import global.msnthrp.messenger.utils.getUiFriendlyHash

/**
 * Created by twoeightnine on 2/10/18.
 */


class FingerPrintAlertDialog(context: Context,
                             fingerPrint: String) : AlertDialog(context) {

    init {
        val view = View.inflate(context, R.layout.dialog_fingerprint, null)
        setView(view)
        val tvPrint = view.findViewById<TextView>(R.id.tvPrint)
        val ivGravatar = view.findViewById<ImageView>(R.id.ivGravatar)

        tvPrint.text = getUiFriendlyHash(fingerPrint)
        ivGravatar.loadUrl(context, "https://www.gravatar.com/avatar/$fingerPrint?s=256&d=identicon&r=PG")

        tvPrint.setOnClickListener(::onClick)
        ivGravatar.setOnClickListener(::onClick)

    }

    private fun onClick(v: View) = dismiss()
}