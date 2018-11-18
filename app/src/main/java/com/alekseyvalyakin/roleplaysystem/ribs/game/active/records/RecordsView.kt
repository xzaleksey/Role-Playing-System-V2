package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.text.InputType
import android.view.View
import android.view.ViewGroup
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
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.alekseyvalyakin.roleplaysystem.views.ButtonsView
import com.alekseyvalyakin.roleplaysystem.views.SearchToolbar
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxrelay2.PublishRelay
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*
import java.util.concurrent.TimeUnit

class RecordsView constructor(
        context: Context
) : _LinearLayout(context), RecordsPresenter {

    private var searchToolbar: SearchToolbar
    private val relay = PublishRelay.create<RecordsPresenter.UiEvent>()
    private val rxPermissions = RxPermissions(context as FragmentActivity)
    private lateinit var tvRecordTime: TextView
    private var recordViewGroup: ViewGroup
    lateinit var ivStopRecord: ImageView
    lateinit var ivPauseRecord: ImageView
    var container: ViewGroup
    private var latestRecordInfo: RecordState? = null
    private var buttonsView: ButtonsView

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

        buttonsView = buttonsView({}, listOf(ButtonsView.ButtonInfo(getString(R.string.texts), View.OnClickListener {
            relay.accept(RecordsPresenter.UiEvent.OpenTexts)
        }), ButtonsView.ButtonInfo(getString(R.string.audio), View.OnClickListener {
            Observable.just(RecordsPresenter.UiEvent.OpenAudio).requestPermissionsExternalReadWriteAndAudioRecord(rxPermissions)
                    .subscribeWithErrorLogging { relay.accept(RecordsPresenter.UiEvent.OpenAudio) }
        }))).lparams(matchParent, wrapContent) {
            val horMargin = getDoubleCommonDimen()
            val vMargin = getCommonDimen()
            setMargins(horMargin, vMargin, horMargin, vMargin)
        }
        container = frameLayout {
            clipChildren = false
        }.lparams(matchParent, matchParent)
    }

    override fun update(viewModel: RecordsViewModel) {
        buttonsView.setCurrentIndex(viewModel.recordTab.index)
    }

    override fun updateRecordState(viewModel: RecordState) {
        latestRecordInfo = viewModel
        val showRecordPlate = viewModel.isShowRecordPlate()
        if (showRecordPlate) {
            recordViewGroup.visibility = View.VISIBLE
            tvRecordTime.text = viewModel.getTimePassed()
            ivPauseRecord.imageResource = if (viewModel.isInProgress()) R.drawable.ic_pause else R.drawable.ic_play
        } else {
            recordViewGroup.visibility = View.GONE
        }
        if (viewModel.isFinished()) {
            MaterialDialog(context)
                    .title(R.string.confirm_record_name)
                    .input(hint = getString(R.string.input_name),
                            waitForPositiveButton = false,
                            inputType = InputType.TYPE_CLASS_TEXT,
                            prefill = viewModel.recordInfo.finalFile.nameWithoutExtension,
                            callback = object : InputCallback {
                                override fun invoke(dialog: MaterialDialog, text: CharSequence) {
                                    dialog.setActionButtonEnabled(WhichButton.POSITIVE, !text.isBlank())
                                }
                            })
                    .positiveButton(android.R.string.ok, click = object : DialogCallback {
                        override fun invoke(m: MaterialDialog) {
                            relay.accept(RecordsPresenter.UiEvent.SaveRecord(viewModel, m.getInputField()!!.text.toString()))
                        }
                    })
                    .apply {
                        this.getInputField()!!.run {
                            this.filters = arrayOf(getFileNameInputFilter())
                            this.setSelection(this.length())
                        }
                    }
                    .show()

        } else if (viewModel.recordInfo.e != null) {
            context.toast(R.string.record_error)
        }
    }

    override fun showSuccessSave() {
        context.toast(R.string.record_saved)
    }

    override fun clearSearchInput() {
        searchToolbar.clearInput()
    }

    override fun observeUiEvents(): Observable<RecordsPresenter.UiEvent> {
        return Observable.merge(listOf(
                stopRecording(),
                pauseRecording(),
                observeSearchInput(),
                relay))
    }

    private fun stopRecording(): Observable<RecordsPresenter.UiEvent.StopRecording> {
        return RxView.clicks(ivStopRecord).map {
            RecordsPresenter.UiEvent.StopRecording
        }
    }

    private fun pauseRecording(): Observable<RecordsPresenter.UiEvent.PauseRecording> {
        return RxView.clicks(ivPauseRecord).map {
            RecordsPresenter.UiEvent.PauseRecording(latestRecordInfo!!)
        }
    }

    private fun observeSearchInput(): Observable<RecordsPresenter.UiEvent.SearchInput> = searchToolbar.observeSearchInput()
            .debounce(200L, TimeUnit.MILLISECONDS, Schedulers.computation())
            .throttleLast(100L, TimeUnit.MILLISECONDS, Schedulers.computation())
            .map { RecordsPresenter.UiEvent.SearchInput(it.toString()) }
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())

}
