package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills

import android.content.Context
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.alekseyvalyakin.roleplaysystem.views.backdrop.back.BackView
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import org.jetbrains.anko.*
import timber.log.Timber

open class SkillBackView(context: Context) : _LinearLayout(context), BackView {

    private var etTitle: EditText
    private var etSubtitle: EditText
    private var etTags: AutoCompleteTextView
    private var successCheck: ViewGroup
    private var resultCheck: ViewGroup

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

        etTags = themedAutoCompleteTextView(R.style.AppTheme_TextWhite) {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            imeOptions = EditorInfo.IME_ACTION_DONE
            setCompoundDrawablesWithIntrinsicBounds(getCompatDrawable(R.drawable.ic_tag), null, null, null)
            compoundDrawablePadding = getCommonDimen()
            hintResource = R.string.add_tag
            val arrayAdapter = ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, arrayOf("test", "mest"))
            setAdapter(arrayAdapter)
            this.setOnItemClickListener { parent, view, position, id ->
                Timber.d(arrayAdapter.getItem(position))
            }
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Timber.d("Action done")
                    return@setOnEditorActionListener true
                }

                return@setOnEditorActionListener false
            }

        }.lparams(matchParent) {
        }

        successCheck = relativeLayout {
            backgroundResource = getSelectableItemBackGround()
            imageView {
                id = R.id.right_icon
                imageResource = R.drawable.ic_add
                tintImageRes(R.color.colorWhite)
            }.lparams(dimen(R.dimen.dp_18), dimen(R.dimen.dp_18)) {
                alignParentEnd()
                centerVertically()
            }

            imageView {
                id = R.id.left_icon
                imageResource = R.drawable.dice_d6
                tintImageRes(R.color.colorDisabled)
            }.lparams(dimen(R.dimen.dp_18), dimen(R.dimen.dp_18)) {
                alignParentStart()
                centerVertically()
            }

            themedTextView(R.style.AppTheme_TextWhite) {
                textResource = R.string.success_check
                body1Style()
            }.lparams(matchParent) {
                endOf(R.id.left_icon)
                startOf(R.id.right_icon)
                centerVertically()
                marginStart = getDoubleCommonDimen()
                marginEnd = getDoubleCommonDimen()
            }
        }.lparams(matchParent, getIntDimen(R.dimen.dp_48)) {
            bottomMargin = getCommonDimen()
        }

        resultCheck = relativeLayout {
            backgroundResource = getSelectableItemBackGround()
            imageView {
                id = R.id.right_icon
                imageResource = R.drawable.ic_add
                tintImageRes(R.color.colorWhite)
            }.lparams(dimen(R.dimen.dp_18), dimen(R.dimen.dp_18)) {
                alignParentEnd()
                centerVertically()
            }

            imageView {
                id = R.id.left_icon
                imageResource = R.drawable.dice_d6
                tintImageRes(R.color.colorDisabled)
            }.lparams(dimen(R.dimen.dp_18), dimen(R.dimen.dp_18)) {
                alignParentStart()
                centerVertically()
            }

            themedTextView(R.style.AppTheme_TextWhite) {
                textResource = R.string.result_check
                body1Style()
            }.lparams(matchParent) {
                endOf(R.id.left_icon)
                startOf(R.id.right_icon)
                centerVertically()
                marginStart = getDoubleCommonDimen()
                marginEnd = getDoubleCommonDimen()
            }
        }.lparams(matchParent, getIntDimen(R.dimen.dp_48)) {
            bottomMargin = dimen(R.dimen.dp_14)
        }
    }

    fun getEtTitleObservable(): Observable<GameSettingsSkillsPresenter.UiEvent.TitleInput> {
        return RxTextView.afterTextChangeEvents(etTitle).skipInitialValue()
                .map {
                    GameSettingsSkillsPresenter.UiEvent.TitleInput(it.editable().toString())
                }
    }

    fun getEtSubtitleObservable(): Observable<GameSettingsSkillsPresenter.UiEvent.SubtitleInput> {
        return RxTextView.afterTextChangeEvents(etSubtitle).skipInitialValue().map {
            GameSettingsSkillsPresenter.UiEvent.SubtitleInput(it.editable().toString())
        }
    }

    fun getTagObservable(): Observable<GameSettingsSkillsPresenter.UiEvent.TagInput> {
        return RxTextView.afterTextChangeEvents(etTags).skipInitialValue()
                .map { GameSettingsSkillsPresenter.UiEvent.TagInput(it.editable().toString()) }
    }

    fun getClickAddSuccessCheckObservable(): Observable<GameSettingsSkillsPresenter.UiEvent.AddSuccessCheck> {
        return RxView.clicks(successCheck).map { GameSettingsSkillsPresenter.UiEvent.AddSuccessCheck }
    }

    fun getClickAddResultCheckObservable(): Observable<GameSettingsSkillsPresenter.UiEvent.AddResultCheck> {
        return RxView.clicks(resultCheck).map { GameSettingsSkillsPresenter.UiEvent.AddResultCheck }
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