package com.alekseyvalyakin.roleplaysystem.ribs.game.create

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.widget.EditText
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxrelay2.BehaviorRelay
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
    private lateinit var fab: FloatingActionButton
    private val modelRelay = BehaviorRelay.create<CreateGameViewModel>()
    private val textChangeObservable: Observable<String>

    init {

        backgroundColor = getCompatColor(R.color.colorPrimaryDark)
        AnkoContext.createDelegate(this).apply {
            relativeLayout {

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
                }.lparams( width= matchParent, height = matchParent){
                    above(fab)
                }
            }.lparams(width = matchParent, height = matchParent) {
                topMargin = getIntDimen(R.dimen.dp_32) + getStatusBarHeight()
                leftMargin = getIntDimen(R.dimen.dp_40)
                rightMargin = getIntDimen(R.dimen.dp_40)
                bottomMargin = getIntDimen(R.dimen.dp_40)
            }
        }
        textChangeObservable = RxTextView.textChanges(inputEditText).map { it.toString() }.share()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        inputEditText.showSoftKeyboard()
    }

    override fun updateView(createGameViewModel: CreateGameViewModel) {
        stepTextView.text = createGameViewModel.stepText
        titleTextView.text = createGameViewModel.title
        inputEditText.hint = createGameViewModel.inputHint
        exampleText.text = createGameViewModel.inputExample
        modelRelay.accept(createGameViewModel)
    }

    override fun updateFabShowDisposable(): Disposable {
        return Observable.combineLatest(textChangeObservable,
                modelRelay, BiFunction { text: CharSequence, createGameViewModel: CreateGameViewModel ->
            if (createGameViewModel.required && text.isBlank()) {
                fab.hide()
            } else {
                fab.show()
            }
            return@BiFunction createGameViewModel
        }).subscribeWithErrorLogging()
    }

    override fun observeUiEvents(): Observable<CreateGameUiEvent> {
        return Observable.merge(RxView.clicks(fab).map {
            CreateGameUiEvent.ClickNext(inputEditText.text.toString())
        }, textChangeObservable.map { CreateGameUiEvent.InputChange(inputEditText.text.toString()) })
    }
}
