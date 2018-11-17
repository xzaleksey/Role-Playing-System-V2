package com.alekseyvalyakin.roleplaysystem.ribs.game.active.log

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.input.InputCallback
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.log.adapter.LogAdapter
import com.alekseyvalyakin.roleplaysystem.utils.buttonsView
import com.alekseyvalyakin.roleplaysystem.utils.getCommonDimen
import com.alekseyvalyakin.roleplaysystem.utils.getDoubleCommonDimen
import com.alekseyvalyakin.roleplaysystem.utils.getFileNameInputFilter
import com.alekseyvalyakin.roleplaysystem.utils.getFloatDimen
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import com.alekseyvalyakin.roleplaysystem.utils.getSelectableItemBorderless
import com.alekseyvalyakin.roleplaysystem.utils.getString
import com.alekseyvalyakin.roleplaysystem.utils.increaseTouchArea
import com.alekseyvalyakin.roleplaysystem.utils.requestPermissionsExternalReadWriteAndAudioRecord
import com.alekseyvalyakin.roleplaysystem.utils.searchToolbar
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.alekseyvalyakin.roleplaysystem.utils.tintImageRes
import com.alekseyvalyakin.roleplaysystem.utils.updateWithAnimateToStartOnNewItem
import com.alekseyvalyakin.roleplaysystem.views.ButtonsView
import com.alekseyvalyakin.roleplaysystem.views.SearchToolbar
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxrelay2.PublishRelay
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko._LinearLayout
import org.jetbrains.anko.alignParentEnd
import org.jetbrains.anko.alignParentRight
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.centerVertically
import org.jetbrains.anko.dimen
import org.jetbrains.anko.editText
import org.jetbrains.anko.hintResource
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.imageView
import org.jetbrains.anko.leftOf
import org.jetbrains.anko.leftPadding
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.padding
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.rightPadding
import org.jetbrains.anko.textColorResource
import org.jetbrains.anko.textSizeDimen
import org.jetbrains.anko.textView
import org.jetbrains.anko.toast
import org.jetbrains.anko.wrapContent
import java.util.concurrent.TimeUnit

class LogView constructor(
        context: Context
) : _LinearLayout(context), LogPresenter {

    private var searchToolbar: SearchToolbar
    private val recyclerView: RecyclerView
    private val relay = PublishRelay.create<LogPresenter.UiEvent>()
    private val flexibleAdapter = LogAdapter(emptyList(), relay)
    private lateinit var textDisposable: Disposable
    private val rxPermissions = RxPermissions(context as FragmentActivity)
    private lateinit var input: EditText
    private lateinit var tvRecordTime: TextView
    private var recordViewGroup: ViewGroup
    lateinit var ivStopRecord: ImageView
    lateinit var ivPauseRecord: ImageView
    private lateinit var inputActions: ViewGroup
    private lateinit var sendBtn: View
    private lateinit var micBtn: View
    private var latestRecordInfo: LogRecordState? = null
    private val smoothScroller = object : LinearSmoothScroller(getContext()) {
        override fun getVerticalSnapPreference(): Int {
            return LinearSmoothScroller.SNAP_TO_START
        }
    }

    init {
        orientation = VERTICAL
        clipChildren = false

        searchToolbar = searchToolbar({
            id = R.id.search_view
            setTitle(getString(R.string.records))
        }, SearchToolbar.Mode.HIDDEN).lparams(width = matchParent, height = wrapContent) {}
        recordViewGroup = relativeLayout {
            backgroundColorResource = R.color.colorWhite
            elevation = getFloatDimen(R.dimen.dp_1)
            visibility = View.GONE
            tvRecordTime = textView {
                textColorResource = R.color.colorTextPrimary
                textSizeDimen = R.dimen.dp_16
            }.lparams {
                leftMargin = getDoubleCommonDimen()
                centerVertically()
            }

            ivPauseRecord = imageView {
                id = R.id.iv_pause
                backgroundResource = getSelectableItemBorderless()
                imageResource = R.drawable.ic_pause
                padding = getCommonDimen()
            }.lparams(getIntDimen(R.dimen.dp_40), getIntDimen(R.dimen.dp_40)) {
                centerVertically()
                alignParentEnd()
                rightMargin = getCommonDimen()
            }

            ivStopRecord = imageView {
                backgroundResource = getSelectableItemBorderless()
                padding = getCommonDimen()
                imageResource = R.drawable.ic_stop
            }.lparams(getIntDimen(R.dimen.dp_40), getIntDimen(R.dimen.dp_40)) {
                centerVertically()
                leftOf(R.id.iv_pause)
            }

        }.lparams(matchParent, getIntDimen(R.dimen.dp_48))
        buttonsView({}, listOf(ButtonsView.ButtonInfo(getString(R.string.texts), View.OnClickListener {
            relay.accept(LogPresenter.UiEvent.OpenTexts)
        }), ButtonsView.ButtonInfo(getString(R.string.audio), View.OnClickListener {
            Observable.just(LogPresenter.UiEvent.OpenAudio).requestPermissionsExternalReadWriteAndAudioRecord(rxPermissions)
                    .subscribeWithErrorLogging { relay.accept(LogPresenter.UiEvent.OpenAudio) }
        }))).lparams(matchParent, wrapContent) {
            val horMargin = getDoubleCommonDimen()
            val vMargin = getCommonDimen()
            setMargins(horMargin, vMargin, horMargin, vMargin)
        }

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
                    increaseTouchArea()
                    padding = getCommonDimen()
                    backgroundResource = getSelectableItemBorderless()
                    tintImageRes(R.color.colorTextSecondary)
                    imageResource = R.drawable.ic_send_black_24dp
                }.lparams(width = dimen(R.dimen.dp_40), height = dimen(R.dimen.dp_40)) {
                    gravity = Gravity.CENTER
                }
                micBtn = imageView {
                    increaseTouchArea()
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

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        textDisposable = RxTextView.textChanges(input).subscribeWithErrorLogging {
            sendBtn.visibility = if (it.toString().isBlank()) View.GONE else View.VISIBLE
            updateMicVisibility(it)
        }
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

    override fun updateRecordState(viewModel: LogRecordState) {
        latestRecordInfo = viewModel
        val showRecordPlate = viewModel.isShowRecordPlate()
        if (showRecordPlate) {
            recordViewGroup.visibility = View.VISIBLE
            tvRecordTime.text = viewModel.getTimePassed()
            ivPauseRecord.imageResource = if (viewModel.isInProgress()) R.drawable.ic_pause else R.drawable.ic_play
        } else {
            recordViewGroup.visibility = View.GONE
        }
        updateMicVisibility(input.text)
        if (viewModel.isFinished()) {
            MaterialDialog(context)
                    .title(R.string.confirm_record_name)
                    .input(hint = getString(R.string.input_name),
                            waitForPositiveButton = false,
                            inputType = InputType.TYPE_CLASS_TEXT,
                            prefill = viewModel.recordInfo.finalFile.name,
                            callback = object : InputCallback {
                                override fun invoke(dialog: MaterialDialog, text: CharSequence) {
                                    dialog.setActionButtonEnabled(WhichButton.POSITIVE, !text.isBlank())
                                }
                            })
                    .positiveButton(android.R.string.ok, click = object : DialogCallback {
                        override fun invoke(p1: MaterialDialog) {
                            relay.accept(LogPresenter.UiEvent.SaveRecord(viewModel, p1.getInputField()!!.text.toString()))
                        }
                    })
                    .apply { this.getInputField()!!.filters = arrayOf(getFileNameInputFilter()) }
                    .show()

        } else if (viewModel.recordInfo.e != null) {
            context.toast(R.string.record_error)
        }
    }

    override fun clearSearchInput() {
        searchToolbar.clearInput()
    }

    override fun observeUiEvents(): Observable<LogPresenter.UiEvent> {
        return Observable.merge(listOf(sendMessage(),
                startRecording(),
                observeSearchInput(),
                stopRecording(),
                pauseRecording(),
                relay))
    }

    private fun sendMessage(): Observable<LogPresenter.UiEvent.SendTextMessage> {
        return RxView.clicks(sendBtn).map {
            val text = input.text.toString()
            input.text = null
            LogPresenter.UiEvent.SendTextMessage(text)
        }
    }

    private fun stopRecording(): Observable<LogPresenter.UiEvent.StopRecording> {
        return RxView.clicks(ivStopRecord).map {
            LogPresenter.UiEvent.StopRecording
        }
    }

    private fun pauseRecording(): Observable<LogPresenter.UiEvent.PauseRecording> {
        return RxView.clicks(ivPauseRecord).map {
            LogPresenter.UiEvent.PauseRecording(latestRecordInfo!!)
        }
    }

    private fun startRecording(): Observable<LogPresenter.UiEvent.StartRecording> {
        return RxView.clicks(micBtn)
                .requestPermissionsExternalReadWriteAndAudioRecord(rxPermissions)
                .map { LogPresenter.UiEvent.StartRecording }
    }

    private fun observeSearchInput(): Observable<LogPresenter.UiEvent.SearchInput> = searchToolbar.observeSearchInput()
            .debounce(200L, TimeUnit.MILLISECONDS, Schedulers.computation())
            .throttleLast(100L, TimeUnit.MILLISECONDS, Schedulers.computation())
            .map { LogPresenter.UiEvent.SearchInput(it.toString()) }
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())

}
