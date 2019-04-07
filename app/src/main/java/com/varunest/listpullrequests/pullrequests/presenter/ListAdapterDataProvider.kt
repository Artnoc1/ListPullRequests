package com.varunest.listpullrequests.pullrequests.presenter

import android.support.v7.widget.RecyclerView
import com.varunest.listpullrequests.pullrequests.view.model.ListAdapterItem

interface ListAdapterDataProvider {
    fun getItemCount(): Int
    fun getItem(position: Int): ListAdapterItem
    fun setViewHelper(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>)
    fun addItems(adapterItems: ArrayList<ListAdapterItem>)
}

class ListAdapterDataProviderImpl : ListAdapterDataProvider {
    var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null
    var items: ArrayList<ListAdapterItem> = ArrayList()

    override fun setViewHelper(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        this.adapter = adapter
    }

    override fun addItems(adapterItems: ArrayList<ListAdapterItem>) {
        items.addAll(adapterItems)
        adapter?.notifyDataSetChanged()
    }

    override fun getItem(position: Int): ListAdapterItem {
        return items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }
}