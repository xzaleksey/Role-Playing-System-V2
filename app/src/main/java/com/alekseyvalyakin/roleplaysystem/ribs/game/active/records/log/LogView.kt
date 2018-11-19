package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.log


import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.RecordState
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.log.adapter.LogAdapter
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxrelay2.PublishRelay
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

class LogView constructor(
        context: Context
) : _LinearLayout(context), LogPresenter {

    private val recyclerView: RecyclerView
    private val relay = PublishRelay.create<LogPresenter.UiEvent>()
    private val flexibleAdapter = LogAdapter(emptyList(), relay)
    private lateinit var textDisposable: Disposable
    private lateinit var input: EditText
    private lateinit var inputActions: ViewGroup
    private lateinit var sendBtn: View
    private lateinit var micBtn: View
    private val rxPermissions = RxPermissions(context as FragmentActivity)
    private var latestRecordInfo: RecordState? = null
    private val smoothScroller = object : LinearSmoothScroller(getContext()) {
        override fun getVerticalSnapPreference(): Int {
            return LinearSmoothScroller.SNAP_TO_START
        }
    }

    init {
        orientation = VERTICAL

        relativeLayout {
            id = R.id.send_form
            backgroundResource = R.drawable.white_cornered_background
            elevation = getFloatDimen(R.dimen.dp_16)
            minimumHeight = getIntDimen(R.dimen.dp_48)
            inputActions = linearLayout {
                orientation = HORIZONTAL
                id = R.id.input_actions
                leftPadding = getCommonDimen()
                rightPadding = getCommonDimen()

                sendBtn = imageView {
                    padding = getCommonDimen()
                    backgroundResource = getSelectableItemBorderless()
                    tintImageRes(R.color.colorTextSecondary)
                    imageResource = R.drawable.ic_send_black_24dp
                }.lparams(width = dimen(R.dimen.dp_40), height = dimen(R.dimen.dp_40)) {
                    gravity = Gravity.CENTER
                }
                micBtn = imageView {
                    padding = getCommonDimen()
                    backgroundResource = getSelectableItemBorderless()
                    tintImageRes(R.color.colorTextSecondary)
                    imageResource = R.drawable.ic_mic
                }.lparams(width = dimen(R.dimen.dp_40), height = dimen(R.dimen.dp_40)) {
                    gravity = Gravity.CENTER
                }
            }.lparams(height = dimen(R.dimen.dp_48)) {
                alignParentRight()
                centerVertically()
            }

            input = editText {
                id = R.id.input
                background = null
                hintResource = R.string.input_something
                maxLines = 3
            }.lparams(width = matchParent) {
                centerVertically()
                leftMargin = getDoubleCommonDimen()
                leftOf(R.id.input_actions)
            }
        }.lparams(width = matchParent, height = wrapContent) {
            leftMargin = getDoubleCommonDimen()
            rightMargin = getDoubleCommonDimen()
        }

        recyclerView = recyclerView {
            id = R.id.recycler_view
            clipToPadding = false
            clipChildren = false
            layoutManager = LinearLayoutManager(context)
            adapter = flexibleAdapter
        }.lparams(width = matchParent, height = matchParent) {
        }
    }

    override fun updateRecordState(recordState: RecordState) {
        latestRecordInfo = recordState
        updateMicVisibility(input.text.toString())
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        textDisposable = RxTextView.textChanges(input).subscribeWithErrorLogging {
            sendBtn.visibility = if (it.toString().isBlank()) View.GONE else View.VISIBLE
            updateMicVisibility(it)
        }
        sendBtn.increaseTouchArea()
        micBtn.increaseTouchArea()
    }

    private fun updateMicVisibility(it: CharSequence) {
        micBtn.visibility = if (it.toString().isBlank() && latestRecordInfo?.isShowMic() == true) View.VISIBLE else View.GONE
    }

    override fun onDetachedFromWindow() {
        textDisposable.dispose()
        super.onDetachedFromWindow()
    }

    override fun update(viewModel: LogViewModel) {
        flexibleAdapter.updateWithAnimateToStartOnNewItem(
                recyclerView,
                smoothScroller,
                viewModel.items
        )
    }

    private fun startRecording(): Observable<LogPresenter.UiEvent.StartRecording> {
        return RxView.clicks(micBtn)
                .requestPermissionsExternalReadWriteAndAudioRecord(rxPermissions)
                .map { LogPresenter.UiEvent.StartRecording }
    }


    override fun observeUiEvents(): Observable<LogPresenter.UiEvent> {
        return Observable.merge(listOf(sendMessage(), relay, startRecording()))
    }

    private fun sendMessage(): Observable<LogPresenter.UiEvent.SendTextMessage> {
        return RxView.clicks(sendBtn).map {
            val text = input.text.toString()
            input.text = null
            LogPresenter.UiEvent.SendTextMessage(text)
        }
    }

}
