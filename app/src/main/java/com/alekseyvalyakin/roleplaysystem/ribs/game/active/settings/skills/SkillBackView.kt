package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills

import android.content.Context
import android.text.InputType
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.alekseyvalyakin.roleplaysystem.views.backdrop.back.BackView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import org.jetbrains.anko.*

open class SkillBackView(context: Context) : _LinearLayout(context), BackView {

    private var etTitle: EditText
    private var etSubtitle: EditText
    private var etTags: EditText

    init {
        orientation = VERTICAL
        leftPadding = getDoubleCommonDimen()
        rightPadding = getDoubleCommonDimen()

        etTitle = themedEditText(R.style.AppTheme_TextWhite) {
            singleLine = true
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            imeOptions = EditorInfo.IME_ACTION_NEXT
            hintResource = R.string.name
        }.lparams(matchParent) {
            bottomMargin = getCommonDimen()
        }

        etSubtitle = themedEditText(R.style.AppTheme_TextWhite) {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            hintResource = R.string.description
        }.lparams(matchParent) {
        }

        etTags = themedEditText(R.style.AppTheme_TextWhite) {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            setCompoundDrawablesWithIntrinsicBounds(getCompatDrawable(R.drawable.ic_tag), null, null, null)
            hintResource = R.string.add_tag
        }.lparams(matchParent) {
        }
    }

    fun getEtTitleObservable(): Observable<String> {
        return RxTextView.afterTextChangeEvents(etTitle).skipInitialValue().map { it.editable().toString() }
    }

    fun getEtSubtitleObservable(): Observable<String> {
        return RxTextView.afterTextChangeEvents(etSubtitle).skipInitialValue().map { it.editable().toString() }
    }

    fun getTagObservable(): Observable<String> {
        return RxTextView.afterTextChangeEvents(etTags).skipInitialValue().map { it.editable().toString() }
    }

    fun update(model: Model) {
        if (etTitle.text.toString() != model.titleText) {
            etTitle.setText(model.titleText)
        }
        if (etSubtitle.text.toString() != model.subtitleText) {
            etSubtitle.setText(model.subtitleText)
        }
        etTitle.visibility = if (model.titleVisible) View.VISIBLE else View.GONE
        if (model.titleVisible && etTitle.hasFocus()) {
            etTitle.setSelection(etTitle.length())
        } else {
            etSubtitle.setSelection(etSubtitle.length())
        }
    }

    override fun onShown() {
        etTitle.showSoftKeyboard(100L)
    }

    override fun onHidden() {
        etTitle.hideKeyboard(100L)
    }

    data class Model(
            val titleText: String = StringUtils.EMPTY_STRING,
            val subtitleText: String = StringUtils.EMPTY_STRING,
            val titleVisible: Boolean = true
    )
}