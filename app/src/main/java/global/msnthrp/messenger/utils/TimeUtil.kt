package global.msnthrp.messenger.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by msnthrp on 22/01/18.
 */

fun getTime(ts: Int, full: Boolean = false, format: String = "HH:mm"): String {

    val dateTs = Date(ts * 1000L)
    val dateCurr = Date(System.currentTimeMillis())
    val year = SimpleDateFormat("yyyy")
    val month = SimpleDateFormat("MM")
    val day = SimpleDateFormat("dd")
    val hour = SimpleDateFormat("HH")
    val min = SimpleDateFormat("mm")
    val time = SimpleDateFormat(format)
    year.timeZone = TimeZone.getDefault()
    month.timeZone = TimeZone.getDefault()
    day.timeZone = TimeZone.getDefault()
    hour.timeZone = TimeZone.getDefault()
    min.timeZone = TimeZone.getDefault()
    val result: String
    if (year.format(dateTs) != year.format(dateCurr)) {
        result = "${day.format(dateTs)}.${month.format(dateTs)}.${year.format(dateTs)}"
        return if (full) {
            "$result ${time.format(dateTs)}"
        } else {
            result
        }
    }
    if (month.format(dateTs) != month.format(dateCurr)) {
        result = "${day.format(dateTs)}.${month.format(dateTs)}"
        return if (full) {
            "$result ${time.format(dateTs)}"
        } else {
            result
        }
    }
    if (day.format(dateTs) != day.format(dateCurr)) {
        result = "${day.format(dateTs)}.${month.format(dateTs)}"
        return if (full) {
            "$result ${time.format(dateTs)}"
        } else {
            result
        }
    }
    return time.format(dateTs)
}

fun time() = (System.currentTimeMillis() / 1000L).toInt()