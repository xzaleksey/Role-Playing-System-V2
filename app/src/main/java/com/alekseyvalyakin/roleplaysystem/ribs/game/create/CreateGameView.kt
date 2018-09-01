package com.alekseyvalyakin.roleplaysystem.ribs.game.create

import android.app.Dialog
import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import org.jetbrains.anko.*
import org.jetbrains.anko.design.themedFloatingActionButton

/**
 * Top level view for {@link CreateGameBuilder.CreateGameScope}.
 */
class CreateGameView constructor(
        context: Context
) : _FrameLayout(context), CreateGameInteractor.CreateGamePresenter {

    private lateinit var stepTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var inputEditText: EditText
    private lateinit var exampleText: TextView
    private lateinit var backButton: ImageButton
    private lateinit var fab: FloatingActionButton
    private lateinit var deleteButton: ImageButton
    private val textChangeObservable: Observable<String>
    private val relay = PublishRelay.create<CreateGameUiEvent>()
    private var currentDialog: Dialog? = null

    init {

        backgroundColor = getCompatColor(R.color.colorPrimaryDark)
        AnkoContext.createDelegate(this).apply {
            relativeLayout {

                backButton = imageButton {
                    id = R.id.back_btn
                    backgroundResource = getSelectableItemBorderless()
                    tintImage(R.color.colorWhite)
                    imageResource = R.drawable.ic_arrow_back
                }.lparams(width = getIntDimen(R.dimen.dp_40), height = getIntDimen(R.dimen.dp_40)) {
                    topMargin = getStatusBarHeight()
                    leftMargin = getIntDimen(R.dimen.dp_8)
                }

                deleteButton = imageButton {
                    id = R.id.delete_btn
                    backgroundResource = getSelectableItemBorderless()
                    tintImage(R.color.colorWhite)
                    imageResource = R.drawable.ic_delete_black_24dp
                }.lparams(width = getIntDimen(R.dimen.dp_40), height = getIntDimen(R.dimen.dp_40)) {
                    alignParentRight()
                    topMargin = getStatusBarHeight()
                    leftMargin = getIntDimen(R.dimen.dp_8)
                }

                fab = themedFloatingActionButton(R.style.AppTheme_TextWhite) {
                    id = R.id.fab
                    imageResource = R.drawable.ic_arrow_right
                }.lparams(width = wrapContent, height = wrapContent) {
                    topMargin = getIntDimen(R.dimen.dp_16)
                    alignParentBottom()
                    alignParentEnd()
                }
                relativeLayout {

                    stepTextView = textView {
                        id = R.id.step_text
                        textColorResource = R.color.colorWhite
                        setTextSizeFromRes(R.dimen.sp_16)
                    }.lparams(width = wrapContent, height = wrapContent)

                    titleTextView = textView {
                        id = R.id.title
                        setSanserifMediumTypeface()
                        textColorResource = R.color.colorWhite
                        setTextSizeFromRes(R.dimen.sp_20)
                    }.lparams(width = wrapContent, height = wrapContent) {
                        topMargin = getIntDimen(R.dimen.dp_24)
                        below(stepTextView)
                    }

                    inputEditText = themedEditText(R.style.AppTheme_TextWhite) {
                        id = R.id.input_et
                        hintTextColor = getCompatColor(R.color.white54)
                    }.lparams(width = matchParent, height = wrapContent) {
                        leftMargin = -getIntDimen(R.dimen.dp_4)
                        topMargin = getIntDimen(R.dimen.dp_16)
                        below(titleTextView)
                    }

                    exampleText = themedTextView(R.style.AppTheme_TextWhite) {
                        id = R.id.text
                        setTextSizeFromRes(R.dimen.sp_12)
                    }.lparams(width = matchParent, height = wrapContent) {
                        below(inputEditText)
                    }
                }.lparams(width = matchParent, height = matchParent) {
                    above(fab)
                    below(backButton)
                    leftMargin = getIntDimen(R.dimen.dp_40)
                    topMargin = getIntDimen(R.dimen.dp_32)
                }
            }.lparams(width = matchParent, height = matchParent) {
                rightMargin = getIntDimen(R.dimen.dp_40)
                bottomMargin = getIntDimen(R.dimen.dp_40)
            }
        }
        textChangeObservable = RxTextView.textChanges(inputEditText).map { it.toString() }.share()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        inputEditText.showSoftKeyboard(0L)
    }

    override fun updateView(createGameViewModel: CreateGameViewModel) {
        stepTextView.text = createGameViewModel.stepText
        titleTextView.text = createGameViewModel.title
        inputEditText.hint = createGameViewModel.inputHint
        inputEditText.setText(createGameViewModel.inputText)
        inputEditText.maxLines = createGameViewModel.inputMaxLines
        inputEditText.setSelection(inputEditText.length())
        if (inputEditText.maxLines == 1) {
            inputEditText.imeOptions = EditorInfo.IME_ACTION_DONE
        } else {
            inputEditText.imeOptions = EditorInfo.IME_ACTION_UNSPECIFIED
        }
        inputEditText.inputType = createGameViewModel.inputType
        exampleText.text = createGameViewModel.inputExample
        inputEditText.setSelection(inputEditText.length())
    }

    override fun updateFabShowDisposable(viewModelObservable: Observable<CreateGameViewModel>): Disposable {
        return Observable.combineLatest(textChangeObservable,
                viewModelObservable, BiFunction { text: CharSequence, createGameViewModel: CreateGameViewModel ->
            if (createGameViewModel.required && text.isBlank()) {
                fab.hide()
            } else {
                fab.show()
            }
            return@BiFunction createGameViewModel
        }).subscribeWithErrorLogging()
    }

    override fun observeUiEvents(): Observable<CreateGameUiEvent> {
        return Observable.merge(listOf(RxView.clicks(fab).map { CreateGameUiEvent.ClickNext(inputEditText.text.toString()) },
                RxView.clicks(deleteButton).map { CreateGameUiEvent.DeleteGame },
                textChangeObservable.map { CreateGameUiEvent.InputChange(inputEditText.text.toString()) },
                RxView.clicks(backButton).map { CreateGameUiEvent.BackPress },
                relay))
    }

    override fun showConfirmDeleteDialog() {
        if (currentDialog != null) {
            return
        }
        currentDialog = MaterialDialog(context)
                .title(R.string.delete_game)
                .message(R.string.delete)
                .positiveButton(res = android.R.string.ok, click = {
                    relay.accept(CreateGameUiEvent.ConfirmDeleteGame)
                })
                .negativeButton(res = android.R.string.cancel)
        currentDialog?.setOnDismissListener { currentDialog = null }
        currentDialog?.show()
    }
}
