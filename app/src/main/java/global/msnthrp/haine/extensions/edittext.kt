package global.msnthrp.haine.extensions

import android.widget.EditText

fun EditText.clear() = setText("")

fun EditText.getAsString() = text.toString()