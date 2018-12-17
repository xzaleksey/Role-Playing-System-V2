package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills

import android.content.Context
import android.content.res.ColorStateList
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.LinearLayout
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills.UserGameSkill
import com.alekseyvalyakin.roleplaysystem.data.firestore.tags.Tag
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.alekseyvalyakin.roleplaysystem.views.backdrop.back.BackView
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.addTo
import org.jetbrains.anko.*

open class SkillBackView(context: Context) : _LinearLayout(context), BackView {

    private var etTitle: EditText
    private var etSubtitle: EditText
    private var etTags: AutoCompleteTextView
    private lateinit var tagsContainer: LinearLayout
    private var successCheck: ViewGroup
    private var resultCheck: ViewGroup
    private val compositeDisposable = CompositeDisposable()
    private var tagDisposable = Disposables.disposed()
    private var latestModel: Model? = null
    private var tagsAdapter: ArrayAdapter<String>
    private var relay = PublishRelay.create<GameSettingsSkillsPresenter.UiEvent>()

    init {
        orientation = VERTICAL
        leftPadding = getDoubleCommonDimen()
        rightPadding = getDoubleCommonDimen()
        tagsAdapter = getTagsAdapter(emptyList())

        etTitle = themedEditText(R.style.AppTheme_TextWhite) {
            singleLine = true
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            imeOptions = EditorInfo.IME_ACTION_NEXT
            hintResource = R.string.name
            backgroundResource = R.drawable.edittext_white_bg
        }.lparams(matchParent) {
            bottomMargin = getCommonDimen()
        }

        etSubtitle = themedEditText(R.style.AppTheme_TextWhite) {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            hintResource = R.string.description
            backgroundResource = R.drawable.edittext_white_bg
        }.lparams(matchParent) {
        }

        etTags = themedAutoCompleteTextView(R.style.AppTheme_TextWhite) {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            imeOptions = EditorInfo.IME_ACTION_DONE
            backgroundResource = R.drawable.edittext_white_bg_with_left_icon
            setCompoundDrawablesWithIntrinsicBounds(getCompatDrawable(R.drawable.ic_tag), null, null, null)
            compoundDrawablePadding = getCommonDimen()
            hintResource = R.string.add_tag
            setOnItemClickListener { _, _, position, _ ->
                addTag(tagsAdapter.getItem(position)!!)
            }

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (addTag(text.toString())) {
                        return@setOnEditorActionListener true
                    }
                }

                return@setOnEditorActionListener false
            }

        }.lparams(matchParent) {
            topMargin = getCommonDimen()
            bottomMargin = getCommonDimen()
        }
        horizontalScrollView {
            tagsContainer = linearLayout {

            }.lparams(wrapContent, wrapContent)
        }.lparams(matchParent, wrapContent) {
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

    private fun addTag(item: String): Boolean {
        latestModel?.run {
            if (this.userGameSkill.tags.addIfNotContainsAndNotBlank(item)) {
                addTagView(item)
                relay.accept(GameSettingsSkillsPresenter.UiEvent.TagAdd(item, this.userGameSkill))
                etTags.setText(StringUtils.EMPTY_STRING)
            }
        }
        return false
    }

    private fun removeTag(item: String) {
        latestModel?.run {
            this.userGameSkill.tags.remove(item)
            relay.accept(GameSettingsSkillsPresenter.UiEvent.TagRemove(item, this.userGameSkill))
        }
    }

    private fun getTagsAdapter(items: List<String>): ArrayAdapter<String> {
        val tags = items.toMutableList()
        latestModel?.run {
            tags.removeAll(this.userGameSkill.tags)
        }
        return ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, tags)
    }

    private fun subscribeTitle() {
        RxTextView.textChanges(etTitle).skipInitialValue()
                .subscribeWithErrorLogging {
                    latestModel?.run {
                        this.userGameSkill.name = it.toString()
                    }
                }.addTo(compositeDisposable)
    }

    private fun subscribeSubtitle() {
        RxTextView.textChanges(etSubtitle).skipInitialValue()
                .subscribeWithErrorLogging {
                    latestModel?.run {
                        this.userGameSkill.description = it.toString()
                    }
                }.addTo(compositeDisposable)
    }

    fun getClickAddSuccessCheckObservable(): Observable<GameSettingsSkillsPresenter.UiEvent.AddSuccessCheck> {
        return RxView.clicks(successCheck).map { GameSettingsSkillsPresenter.UiEvent.AddSuccessCheck }
    }

    fun getClickAddResultCheckObservable(): Observable<GameSettingsSkillsPresenter.UiEvent.AddResultCheck> {
        return RxView.clicks(resultCheck).map { GameSettingsSkillsPresenter.UiEvent.AddResultCheck }
    }

    fun update(model: Model) {
        if (latestModel != model) {
            latestModel = model
            etTags.text = null
            tagDisposable.dispose()
            tagDisposable = subscribeTags(model.tagsObservable)
        }

        val userGameSkill = model.userGameSkill
        if (etTitle.text.toString() != userGameSkill.name) {
            etTitle.setText(userGameSkill.name)
        }

        if (etSubtitle.text.toString() != userGameSkill.description) {
            etSubtitle.setText(userGameSkill.description)
        }

        val isVisible = !userGameSkill.isDefaultSkill()
        etTitle.visibility = if (isVisible) View.VISIBLE else View.GONE

        if (isVisible && etTitle.hasFocus()) {
            etTitle.setSelection(etTitle.length())
        } else {
            etSubtitle.setSelection(etSubtitle.length())
        }

        updateTags(model.userGameSkill.tags)
    }

    private fun updateTags(tags: MutableList<String>) {
        tagsContainer.removeAllViews()
        for (tag in tags) {
            addTagView(tag)
        }
    }

    private fun addTagView(tag: String) {
        tagsContainer.run {
            chip {
                setChipBackgroundColorResource(R.color.colorPrimary)
                setChipStrokeColorResource(R.color.colorWhite)
                textColorResource = R.color.colorWhite
                val closeDrawable = getCompatDrawable(R.drawable.ic_close_backdrop)
                isCloseIconVisible = true
                closeIcon = closeDrawable
                closeIconTint = ColorStateList.valueOf(getCompatColor(R.color.colorWhite))
                rippleColor = ColorStateList.valueOf(getCompatColor(R.color.colorWhite))
                highlightColor = getCompatColor(R.color.colorWhite)
                chipStrokeWidth = getFloatDimen(R.dimen.dp_1)
                text = tag
                setOnCloseIconClickListener {
                    removeTag(tag)
                    (parent as ViewGroup).removeView(this)
                }
            }.lparams(wrapContent, wrapContent) {
                marginEnd = getCommonDimen()
                bottomMargin = getDoubleCommonDimen()
            }
        }
    }

    override fun onAttachedToWindow() {
        subscribeTitle()
        subscribeSubtitle()
        super.onAttachedToWindow()
    }

    private fun subscribeTags(tagsObservable: Observable<List<Tag>>): Disposable {
        return tagsObservable
                .subscribeWithErrorLogging { list ->
                    tagsAdapter = getTagsAdapter(list.map { it -> it.id })
                    etTags.setAdapter(tagsAdapter)
                }
    }

    fun getRelay() = relay.hide()

    override fun onDetachedFromWindow() {
        compositeDisposable.clear()
        tagDisposable.dispose()
        super.onDetachedFromWindow()
    }

    override fun onShown() {
        etTitle.showSoftKeyboard(100L)
    }

    override fun onHidden() {
        etTitle.hideKeyboard(100L)
    }

    data class Model(
            val userGameSkill: UserGameSkill,
            val tagsObservable: Observable<List<Tag>>
    )
}