package com.alekseyvalyakin.roleplaysystem.views.backdrop

import android.content.Context
import android.text.InputType
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import org.jetbrains.anko.*

class DefaultBackView(context: Context) : _LinearLayout(context), BackView {

    private var etTitle: EditText
    private var etSubtitle: EditText

    init {
        orientation = VERTICAL
        leftPadding = getDoubleCommonDimen()
        rightPadding = getDoubleCommonDimen()

        etTitle = themedEditText(R.style.AppTheme_TextWhite) {
            singleLine = true
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            imeOptions = EditorInfo.IME_ACTION_NEXT
        }.lparams(matchParent) {
            bottomMargin = getCommonDimen()
        }

        etSubtitle = themedEditText(R.style.AppTheme_TextWhite) {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        }.lparams(matchParent) {
            bottomMargin = getIntDimen(R.dimen.dp_40)
        }

    }

    fun getEtTitleObservable(): Observable<String> {
        return RxTextView.afterTextChangeEvents(etTitle).skipInitialValue().map { it.editable().toString() }
    }

    fun getEtSubtitleObservable(): Observable<String> {
        return RxTextView.afterTextChangeEvents(etSubtitle).skipInitialValue().map { it.editable().toString() }
    }

    fun update(model: Model) {
        etTitle.hint = model.titleHint
        etSubtitle.hint = model.subtitleHint
    }

    override fun onShown() {
        etTitle.showSoftKeyboard(100L)
    }

    override fun onHidden() {
        etTitle.hideKeyboard(100L)
    }

    fun clear() {
        etTitle.setText(StringUtils.EMPTY_STRING)
        etSubtitle.setText(StringUtils.EMPTY_STRING)
    }

    data class Model(
            val titleHint: String,
            val subtitleHint: String,
            val titleText: String = "",
            val subtitleText: String = ""
    )
}