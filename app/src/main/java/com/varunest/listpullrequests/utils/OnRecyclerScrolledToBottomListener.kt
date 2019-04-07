package com.varunest.listpullrequests.utils

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

abstract class OnRecyclerScrolledToBottomListener : RecyclerView.OnScrollListener() {

    var totalScrollY: Int = 0
    internal var firstVisibleItem: Int = 0
    internal var visibleItemCount: Int = 0

    abstract val layoutManager: LinearLayoutManager?

    abstract val adapter: RecyclerView.Adapter<*>?

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        totalScrollY += dy
        layoutManager?.apply {
            visibleItemCount = childCount
            firstVisibleItem = findFirstVisibleItemPosition()

            adapter?.apply {
                if (itemCount - visibleItemCount <= firstVisibleItem) {
                    recyclerView.post { onScrolledToBottom() }
                }
            }
        }
    }

    abstract fun onScrolledToBottom()
}