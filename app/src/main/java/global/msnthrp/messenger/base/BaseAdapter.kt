package global.msnthrp.messenger.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater

/**
 * Created by msnthrp on 22/01/18.
 */

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder>(protected var context: Context) : RecyclerView.Adapter<VH>() {

    open val items: ArrayList<T> = arrayListOf()

    protected var inflater = LayoutInflater.from(context)

    var multiListener: OnMultiSelected? = null

    fun add(item: T, pos: Int) {
        items.add(pos, item)
        notifyItemInserted(pos)
    }

    open fun add(item: T) {
        add(item, items.size)
    }

    @JvmOverloads fun addAll(items: List<T>, pos: Int = this.items.size) {
        val size = items.size
        this.items.addAll(pos, items)
        notifyItemRangeInserted(pos, size)
    }

    fun removeAt(pos: Int): T {
        val removed = items.removeAt(pos)
        notifyItemRemoved(pos)
        return removed
    }

    fun remove(obj: T) {
        for (i in items.indices) {
            if (obj == items[i]) {
                removeAt(i)
                break
            }
        }
    }

    fun update(pos: Int, item: T): T {
        val oldItem = items[pos]
        items[pos] = item
        notifyItemChanged(pos)
        return oldItem
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    val isEmpty: Boolean
        get() = items.isEmpty()


    override fun getItemCount(): Int {
        return items.size
    }

    //multiselect

    var multiSelectRaw: ArrayList<T> = arrayListOf()
        protected set

    fun multiSelect(item: T) {
        if (multiSelectRaw.contains(item)) {
            removeFromMultiSelect(item)
        } else {
            addToMultiSelect(item)
        }
        if (multiListener != null) {
            notifyMultiSelect()
        }
    }

    open fun notifyMultiSelect() {
        if (multiSelectRaw.size == 0) {
            multiListener!!.onEmpty()
        } else if (multiSelectRaw.size == 1) {
            multiListener!!.onNonEmpty()
        }
    }

    fun clearMultiSelect() {
        multiSelectRaw.clear()
        if (multiListener != null) {
            multiListener!!.onEmpty()
        }
        notifyDataSetChanged()
    }

    private fun addToMultiSelect(item: T) {
        multiSelectRaw.add(item)
    }

    private fun removeFromMultiSelect(item: T) {
        multiSelectRaw.remove(item)
    }

    interface OnMultiSelected {
        fun onNonEmpty()
        fun onEmpty()
    }

}