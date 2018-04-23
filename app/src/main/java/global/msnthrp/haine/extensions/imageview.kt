package global.msnthrp.haine.extensions

import android.content.Context
import android.widget.ImageView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

/**
 * Created by msnthrp on 22/01/18.
 */

val emptyImage = "https://herschel.com/content/dam/herschel/swatches/01553.png"

fun ImageView.loadUrl(context: Context, url: String?) {
    val notBlank = if (url == null || url.isBlank()) emptyImage else url
    Picasso.with(context)
            .load(notBlank)
            .into(this)
}

fun ImageView.loadUrlForce(context: Context, url: String?) {
    val notBlank = if (url == null || url.isBlank()) emptyImage else url
    Picasso.with(context).invalidate(url)
    Picasso.with(context)
            .load(notBlank)
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .into(this)
}