package com.varunest.listpullrequests.pullrequests.presenter

import android.support.v7.widget.RecyclerView
import com.varunest.listpullrequests.pullrequests.view.model.ListAdapterItem

interface ListAdapterDataProvider {
    fun getItemCount(): Int
    fun getItem(position: Int): ListAdapterItem
    fun setViewHelper(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>)
    fun addItems(adapterItems: ArrayList<ListAdapterItem>)
    fun resetItems()
    fun addLoader(flag: Boolean)
    fun getItemViewType(position: Int): Int
}

class ListAdapterDataProviderImpl : ListAdapterDataProvider {
    var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null

    var items: ArrayList<ListAdapterItem> = ArrayList()
    override fun setViewHelper(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        this.adapter = adapter
    }

    override fun addItems(adapterItems: ArrayList<ListAdapterItem>) {
        val startPos = items.size
        items.addAll(adapterItems)
        adapter?.notifyItemRangeChanged(startPos, adapterItems.size)
    }

    override fun resetItems() {
        val size = items.size
        items.clear()
        adapter?.notifyDataSetChanged()
    }

    override fun getItem(position: Int): ListAdapterItem {
        return items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type
    }

    override fun addLoader(flag: Boolean) {
        if (flag) {
            if (items[items.size - 1].type != ListAdapterItem.TYPE_LOADER) {
                items.add(ListAdapterItem(ListAdapterItem.TYPE_LOADER))
                adapter?.notifyItemInserted(items.size - 1)
            }
        } else {
            if (items[items.size - 1].type == ListAdapterItem.TYPE_LOADER) {
                val pos = items.size - 1
                items.removeAt(pos)
                adapter?.notifyItemRemoved(pos)
            }
        }
    }
}