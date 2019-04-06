package com.varunest.listpullrequests.view

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_main.*

interface PullRequestViewHelper {

    fun queryInputObserver(): Observable<String>
    fun showToast(query: String?)
}

class PullRequestViewHelperImpl(val rootView: View) : PullRequestViewHelper, LayoutContainer {
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
    }

    override fun showToast(query: String?) {
        Toast.makeText(rootView.context, query, Toast.LENGTH_SHORT).show()
    }

    override fun queryInputObserver(): Observable<String> {
        return queryInputSubject.distinctUntilChanged()
    }
}