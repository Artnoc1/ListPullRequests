package com.varunest.listpullrequests.pullrequests.view.viewholder

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.varunest.listpullrequests.R
import com.varunest.listpullrequests.data.network.model.PullRequest
import com.varunest.listpullrequests.utils.CommonUtils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_pr.*
import java.text.SimpleDateFormat
import java.util.*

class PRItemViewHolder(
    val view: View,
    override val containerView: View = view
) : RecyclerView.ViewHolder(view), LayoutContainer {

    fun bind(pullRequest: PullRequest?) {
        name.text = pullRequest?.user?.name
        title.text = pullRequest?.title
        body.text = pullRequest?.body
        try {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")
            val date = simpleDateFormat.parse(pullRequest?.createdAt)
            val timeAgo = CommonUtils.getTimeAgo(date)
            timeAgoTextView.text = view.context.getString(R.string.time_ago_string, timeAgo)
        } catch (e: Exception) {
            Log.e("PRItemViewHolder", "Failed to parse Date")
        }

    }

}