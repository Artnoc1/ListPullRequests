package com.varunest.listpullrequests.pullrequests.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.varunest.listpullrequests.R
import com.varunest.listpullrequests.pullrequests.presenter.ListAdapterDataProvider
import com.varunest.listpullrequests.pullrequests.view.model.ListAdapterItem
import com.varunest.listpullrequests.pullrequests.view.viewholder.LoaderViewHolder
import com.varunest.listpullrequests.pullrequests.view.viewholder.PRItemViewHolder

class ListAdapter(val dataProvider: ListAdapterDataProvider) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ListAdapterItem.TYPE_PR -> return PRItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_pr,
                    parent,
                    false
                )
            )
            ListAdapterItem.TYPE_LOADER -> return LoaderViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_loader,
                    parent,
                    false
                )
            )
        }
        return object : RecyclerView.ViewHolder(View(parent.context)){}
    }

    override fun getItemCount(): Int {
        return dataProvider.getItemCount()
    }

    override fun getItemViewType(position: Int): Int {
        return dataProvider.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val listItem = dataProvider.getItem(position)
        when (listItem.type) {
            ListAdapterItem.TYPE_PR -> (holder as PRItemViewHolder).bind(listItem.pullRequest)
        }
    }

}