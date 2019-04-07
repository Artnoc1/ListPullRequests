package com.varunest.listpullrequests.pullrequests.view.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.varunest.listpullrequests.data.network.model.PullRequest
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_pr.*

class PRItemViewHolder(
    val itemView: View,
    override val containerView: View = itemView
) : RecyclerView.ViewHolder(itemView), LayoutContainer {

    fun bind(pullRequest: PullRequest?) {
        title.text = pullRequest?.title
    }

}