package com.alekseyvalyakin.roleplaysystem.views.backdrop.back

import android.content.Context
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.IconViewModel
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import org.jetbrains.anko.*

open class DefaultBackView(context: Context) : _LinearLayout(context), BackView {

    private var etTitle: EditText
    private var etSubtitle: EditText
    private lateinit var ivIcon: ImageView
    private lateinit var tvIcon: TextView
    private var imageContainer: ViewGroup

    init {
        orientation = VERTICAL
        leftPadding = getDoubleCommonDimen()
        rightPadding = getDoubleCommonDimen()

        etTitle = themedEditText(R.style.AppTheme_TextWhite) {
            singleLine = true
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
            imeOptions = EditorInfo.IME_ACTION_NEXT
            backgroundResource = R.drawable.edittext_white_bg
        }.lparams(matchParent) {
            bottomMargin = getCommonDimen()
        }

        etSubtitle = themedEditText(R.style.AppTheme_TextWhite) {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
            backgroundResource = R.drawable.edittext_white_bg
        }.lparams(matchParent) {
        }

        imageContainer = linearLayout {
            orientation = HORIZONTAL
            backgroundResource = getSelectableItemBackGround()
            topPadding = getCommonDimen()
            bottomPadding = getCommonDimen()

            ivIcon = imageView {
                imageResource = R.drawable.ic_photo
                tintImageRes(R.color.colorWhite)
            }.lparams(getIntDimen(R.dimen.dp_24), getIntDimen(R.dimen.dp_24)) {
                rightMargin = getDoubleCommonDimen()
                gravity = Gravity.CENTER_VERTICAL
            }

            tvIcon = textView {
                textColorResource = R.color.colorWhite
                textSizeDimen = R.dimen.dp_16
                textResource = R.string.icon
            }
        }.lparams(matchParent, wrapContent) {
            bottomMargin = getDoubleCommonDimen()
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
        if (etTitle.text.toString() != model.titleText) {
            etTitle.setText(model.titleText)
        }
        if (etSubtitle.text.toString() != model.subtitleText) {
            etSubtitle.setText(model.subtitleText)
        }
        ivIcon.setImageDrawable(model.iconModel.drawable)
        imageContainer.setOnClickListener { model.chooseIconListener() }
        ivIcon.visibility = if (model.iconVisible) View.VISIBLE else View.GONE
        tvIcon.visibility = if (model.iconVisible) View.VISIBLE else View.GONE
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
            val titleHint: String,
            val subtitleHint: String,
            val titleText: String = StringUtils.EMPTY_STRING,
            val subtitleText: String = StringUtils.EMPTY_STRING,
            val iconModel: IconViewModel,
            val chooseIconListener: () -> Unit,
            val titleVisible: Boolean = true,
            val iconVisible: Boolean = true
    )
}