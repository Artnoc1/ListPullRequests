package com.varunest.listpullrequests.pullrequests.view

import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.varunest.listpullrequests.pullrequests.presenter.ListAdapterDataProvider
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_main.*

interface PRViewHelper {

    fun queryInputObserver(): Observable<String>
    fun showToast(query: String?)
    fun wireUpWidgets(dataProvider: ListAdapterDataProvider)
}

class PRViewHelperImpl(val rootView: View) : PRViewHelper, LayoutContainer {
    override val containerView: View = rootView

    private val queryInputSubject = PublishSubject.create<String>()

    init {
        queryInputSubject.onNext("")
        searchInputEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                queryInputSubject.onNext(searchInputEditText.text.toString())
                true
            } else {
                false
            }
        }
        pullrequestRecyclerView.layoutManager = LinearLayoutManager(rootView.context)
        pullrequestRecyclerView.itemAnimator = DefaultItemAnimator()

    }

    override fun wireUpWidgets(dataProvider: ListAdapterDataProvider) {
        val adapter = ListAdapter(dataProvider)
        dataProvider.setViewHelper(adapter)
        pullrequestRecyclerView.adapter = adapter
    }

    override fun showToast(query: String?) {
        Toast.makeText(rootView.context, query, Toast.LENGTH_SHORT).show()
    }

    override fun queryInputObserver(): Observable<String> {
        return queryInputSubject.distinctUntilChanged()
    }
}