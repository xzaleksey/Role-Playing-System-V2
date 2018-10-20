package com.alekseyvalyakin.roleplaysystem.ribs.game.active.log

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.widget.EditText
import android.widget.FrameLayout
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.log.adapter.LogAdapter
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.alekseyvalyakin.roleplaysystem.views.SearchToolbar
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import java.util.concurrent.TimeUnit

class LogView constructor(
        context: Context
) : _RelativeLayout(context), LogPresenter {

    private var searchToolbar: SearchToolbar
    private val recyclerView: RecyclerView
    private val relay = PublishRelay.create<LogPresenter.UiEvent>()
    private val flexibleAdapter = LogAdapter(emptyList(), relay)


    private lateinit var input: EditText
    private lateinit var sendView: FrameLayout
    private val smoothScroller = object : LinearSmoothScroller(getContext()) {
        override fun getVerticalSnapPreference(): Int {
            return LinearSmoothScroller.SNAP_TO_END
        }
    }

    init {
        searchToolbar = searchToolbar {
            id = R.id.search_view
            setTitle(getString(R.string.log))
        }.lparams(width = matchParent, height = wrapContent)

        relativeLayout {
            id = R.id.send_form
            view {
                id = R.id.divider
                backgroundColorResource = R.color.colorDivider
            }.lparams(width = matchParent, height = dip(1)) {
                below(R.id.tv_name)
            }
            sendView = frameLayout {
                id = R.id.icon_send
                backgroundResource = getSelectableItemBorderless()
                leftPadding = getDoubleCommonDimen()
                rightPadding = getDoubleCommonDimen()
                imageView {
                    tintImageRes(R.color.colorAccent)
                    imageResource = R.drawable.ic_send_black_24dp
                }.lparams(width = dimen(R.dimen.dp_24), height = dimen(R.dimen.dp_24)) {
                    gravity = Gravity.CENTER
                }
            }.lparams(height = dimen(R.dimen.dp_48)) {
                alignParentRight()
                centerVertically()
            }
            input = editText {
                id = R.id.input
                background = null
                hintResource = R.string.your_message
                maxLines = 3
            }.lparams(width = matchParent) {
                centerVertically()
                leftMargin = getDoubleCommonDimen()
                leftOf(R.id.icon_send)
            }
        }.lparams(width = matchParent, height = wrapContent) {
            alignParentBottom()
        }

        recyclerView = recyclerView {
            id = R.id.recycler_view
            clipToPadding = false
            leftPadding = getCommonDimen()
            rightPadding = getCommonDimen()
            layoutManager = LinearLayoutManager(context).apply { stackFromEnd = true }
            adapter = flexibleAdapter
        }.lparams(width = matchParent, height = matchParent) {
            above(R.id.send_form)
            below(R.id.search_view)
        }
    }

    override fun update(viewModel: LogViewModel) {
        flexibleAdapter.updateWithAnimateToEndOnNewItem(
                recyclerView,
                smoothScroller,
                viewModel.items
        )
    }

    override fun clearSearchInput() {
        searchToolbar.clearInput()
    }

    override fun observeUiEvents(): Observable<LogPresenter.UiEvent> {
        return Observable.merge(RxView.clicks(sendView).map {
            val text = input.text.toString()
            input.text = null
            LogPresenter.UiEvent.SendTextMessage(text)
        }, observeSearchInput())
    }

    private fun observeSearchInput(): Observable<LogPresenter.UiEvent.SearchInput> = searchToolbar.observeSearchInput()
            .debounce(200L, TimeUnit.MILLISECONDS, Schedulers.computation())
            .throttleLast(100L, TimeUnit.MILLISECONDS, Schedulers.computation())
            .map { LogPresenter.UiEvent.SearchInput(it.toString()) }
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())

}
