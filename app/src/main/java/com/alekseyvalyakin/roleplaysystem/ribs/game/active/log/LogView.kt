package com.alekseyvalyakin.roleplaysystem.ribs.game.active.log

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import com.afollestad.materialdialogs.MaterialDialog
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.log.adapter.LogAdapter
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.jakewharton.rxrelay2.PublishRelay
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

class LogView constructor(
        context: Context
) : _RelativeLayout(context), LogPresenter {

    private val recyclerView: RecyclerView
    private val rxPermissions = RxPermissions(context as FragmentActivity)
    private val relay = PublishRelay.create<LogPresenter.UiEvent>()
    private val flexibleAdapter = LogAdapter(emptyList(), relay)
    private var currentDialog: MaterialDialog? = null

    init {
        view {
            backgroundColorResource = R.color.colorPrimaryDark
        }.lparams(width = matchParent, height = getStatusBarHeight())

        relativeLayout {
            id = R.id.send_form
            backgroundColorResource = R.color.white7
            view {
                id = R.id.divider
                backgroundColorResource = R.color.colorDivider
            }.lparams(width = matchParent, height = dip(1)) {
                below(R.id.tv_name)
            }
            frameLayout {
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
            }
            editText {
                id = R.id.input
                background = null
                hintResource = R.string.your_message
                maxLines = 3
            }.lparams(width = matchParent) {
                centerVertically()
                leftMargin = getDoubleCommonDimen()
                leftOf(R.id.icon_send)
            }
        }.lparams(width = matchParent, height = dimen(R.dimen.dp_48)) {
            alignParentBottom()
        }


        recyclerView = recyclerView {
            id = R.id.recycler_view
            clipToPadding = false
            leftPadding = getCommonDimen()
            rightPadding = getCommonDimen()
            layoutManager = LinearLayoutManager(context)
            adapter = flexibleAdapter
        }.lparams(width = matchParent, height = matchParent) {
            above(R.id.send_form)
            topMargin = getStatusBarHeight()
        }
    }

    override fun update(viewModel: LogViewModel) {
        flexibleAdapter.updateDataSet(viewModel.items, false)
    }

    override fun observeUiEvents(): Observable<LogPresenter.UiEvent> {
        return Observable.empty()
    }


}
