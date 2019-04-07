package com.varunest.listpullrequests.pullrequests.view

import android.content.Context
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.varunest.listpullrequests.data.network.model.PullRequest
import com.varunest.listpullrequests.pullrequests.presenter.ListAdapterDataProvider
import com.varunest.listpullrequests.utils.CommonUtils
import com.varunest.listpullrequests.utils.OnRecyclerScrolledToBottomListener
import io.reactivex.Observable
import io.reactivex.processors.PublishProcessor
import io.reactivex.subjects.PublishSubject
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_main.*

interface PRViewHelper {

    fun queryInputObservable(): Observable<String>
    fun scrolledToBottomObservable(): Observable<Unit>
    fun showToast(query: String?)
    fun wireUpWidgets(dataProvider: ListAdapterDataProvider)
    fun showFullPageLoader(flag: Boolean)
    fun showPRListView(flag: Boolean)
    fun getContext(): Context
    fun showBigMessage(string: String)
    fun getPRClickObservable(): Observable<PullRequest>
}

class PRViewHelperImpl(val rootView: View) : PRViewHelper, LayoutContainer {

    override val containerView: View = rootView

    private val queryInputSubject = PublishSubject.create<String>()
    private val bottomScrollSubject = PublishSubject.create<Unit>()

    private val prRecyclerViewScrollListener = object : OnRecyclerScrolledToBottomListener() {
        override val layoutManager: LinearLayoutManager?
            get() = pullrequestRecyclerView.layoutManager as LinearLayoutManager?
        override val adapter: RecyclerView.Adapter<*>?
            get() = pullrequestRecyclerView.adapter

        override fun onScrolledToBottom() {
            bottomScrollSubject.onNext(Unit)
        }
    }

    init {
        queryInputSubject.onNext("")
        searchInputEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                CommonUtils.hideKeyboard(rootView.context, searchInputEditText)
                queryInputSubject.onNext(searchInputEditText.text.toString())
                true
            } else {
                false
            }
        }
        pullrequestRecyclerView.layoutManager = LinearLayoutManager(rootView.context)
        pullrequestRecyclerView.itemAnimator = DefaultItemAnimator()
        pullrequestRecyclerView.addOnScrollListener(prRecyclerViewScrollListener)
    }

    override fun wireUpWidgets(dataProvider: ListAdapterDataProvider) {
        val adapter = ListAdapter(dataProvider)
        dataProvider.setViewHelper(adapter)
        pullrequestRecyclerView.adapter = adapter
    }

    override fun showFullPageLoader(flag: Boolean) {
        progressBar.visibility = if (flag) View.VISIBLE else View.GONE
    }

    override fun showPRListView(flag: Boolean) {
        pullrequestRecyclerView.visibility = if (flag) View.VISIBLE else View.GONE
    }

    override fun showToast(query: String?) {
        Toast.makeText(rootView.context, query, Toast.LENGTH_SHORT).show()
    }

    override fun getContext(): Context {
        return rootView.context
    }

    override fun showBigMessage(string: String) {
        if (string.isEmpty()) {
            bigMessage.visibility = View.GONE
        } else {
            bigMessage.visibility = View.VISIBLE
        }
        bigMessage.text = string
    }

    override fun queryInputObservable(): Observable<String> {
        return queryInputSubject
    }

    override fun scrolledToBottomObservable(): Observable<Unit> {
        return bottomScrollSubject
    }

    override fun getPRClickObservable(): Observable<PullRequest> {
        return (pullrequestRecyclerView.adapter as ListAdapter).getPRClickObservable()
    }
}