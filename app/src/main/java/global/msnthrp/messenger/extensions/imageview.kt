package global.msnthrp.messenger.extensions

import android.content.Context
import android.widget.ImageView
import com.squareup.picasso.Picasso

/**
 * Created by msnthrp on 22/01/18.
 */

val emptyImage = "http://dataduppedings.no/subcube/Colour_Gradients/Blue-Magenta_256x256.png"

fun ImageView.loadUrl(context: Context, url: String?) {
    val notBlank = if (url == null || url.isBlank()) emptyImage else url
    Picasso.with(context)
            .load(notBlank)
            .into(this)
}