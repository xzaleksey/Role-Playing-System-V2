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
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.restriction.AllRestrictions
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.restriction.Restriction
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.restriction.RestrictionInfo
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills.UserGameSkill
import com.alekseyvalyakin.roleplaysystem.data.firestore.tags.Tag
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.alekseyvalyakin.roleplaysystem.views.backdrop.back.BackView
import com.alekseyvalyakin.roleplaysystem.views.dialog.RestrictionInfoDialogView
import com.google.android.material.chip.Chip
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.addTo
import org.jetbrains.anko.*

open class SkillBackView(context: Context) : _ScrollView(context), BackView {

    private lateinit var etTitle: EditText
    private lateinit var etSubtitle: EditText
    private lateinit var etTags: AutoCompleteTextView
    private lateinit var etRaces: EditText
    private lateinit var etClasses: EditText
    private lateinit var tagsContainer: LinearLayout
    private lateinit var successCheck: ViewGroup
    private lateinit var resultCheck: ViewGroup
    private lateinit var toggleStateView: View

    private val compositeDisposable = CompositeDisposable()
    private var tagDisposable = Disposables.disposed()
    private var restrictionsDisposable = Disposables.disposed()
    private var latestModel: Model? = null
    private var allRestrictions: AllRestrictions = AllRestrictions()
    private var tagsAdapter: ArrayAdapter<String>
    private var relay = PublishRelay.create<GameSettingsSkillsPresenter.UiEvent>()
    private val hiddenViews: MutableList<View> = mutableListOf()

    init {
        leftPadding = getDoubleCommonDimen()
        rightPadding = getDoubleCommonDimen()
        tagsAdapter = getTagsAdapter(emptyList())
        bottomPadding = dimen(R.dimen.dp_14)
        isVerticalScrollBarEnabled = false

        verticalLayout {
            etTitle = themedEditText(R.style.AppTheme_TextWhite) {
                singleLine = true
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                imeOptions = EditorInfo.IME_ACTION_NEXT
                hintResource = R.string.name
                backgroundResource = R.drawable.edittext_white_bg
            }.lparams(matchParent) {
                bottomMargin = getCommonDimen()
            }

            etSubtitle = themedEditText(R.style.AppTheme_TextWhite) {
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                hintResource = R.string.description
                backgroundResource = R.drawable.edittext_white_bg
            }.lparams(matchParent) {
            }

            etTags = themedAutoCompleteTextView(R.style.AppTheme_TextWhite) {
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                imeOptions = EditorInfo.IME_ACTION_DONE
                backgroundResource = R.drawable.edittext_white_bg_with_left_icon
                setCompoundDrawablesWithIntrinsicBounds(
                        getCompatDrawable(R.drawable.ic_tag).tintColorState(getCompatColorStateList(R.color.et_left_icon_color)), null, null, null)
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

            toggleStateView = relativeLayout {
                backgroundResource = getSelectableItemBackGround()
                imageView {
                    id = R.id.left_icon
                    imageResource = R.drawable.ic_add
                    tintImageRes(R.color.colorWhite)
                }.lparams(dimen(R.dimen.dp_24), dimen(R.dimen.dp_24)) {
                    marginStart = getCommonDimen()
                    marginEnd = getDoubleCommonDimen()
                    centerVertically()
                }

                themedTextView(R.style.AppTheme_TextWhite) {
                    textResource = R.string.skills_view_toggle_view_text
                }.lparams(matchParent) {
                    endOf(R.id.left_icon)
                    centerVertically()
                }

                this.setOnClickListener { handleToggleState(true) }

            }.lparams(matchParent, getIntDimen(R.dimen.dp_48))

            etRaces = themedEditText(R.style.AppTheme_TextWhite) {
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                backgroundResource = R.drawable.edittext_white_bg_with_left_icon
                setCompoundDrawablesWithIntrinsicBounds(getCompatDrawable(R.drawable.ic_races)
                        .tint(getCompatColor(R.color.colorWhite)), null, null, null)
                compoundDrawablePadding = dimen(R.dimen.dp_3)
                hintResource = R.string.race_restriction
                isFocusable = false
                isFocusableInTouchMode = false
                setOnClickListener {
                    latestModel?.run {
                        showRestrictionRaces(this.userGameSkill)
                    }
                }

            }.lparams(matchParent) {
                bottomMargin = getCommonDimen()
            }

            etClasses = themedEditText(R.style.AppTheme_TextWhite) {
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                backgroundResource = R.drawable.edittext_white_bg_with_left_icon
                setCompoundDrawablesWithIntrinsicBounds(getCompatDrawable(R.drawable.ic_classes)
                        .tint(getCompatColor(R.color.colorWhite)), null, null, null)
                compoundDrawablePadding = dimen(R.dimen.dp_3)
                hintResource = R.string.class_restriction
                isFocusable = false
                isFocusableInTouchMode = false
                setOnClickListener {
                    latestModel?.run {
                        showRestrictionClasses(this.userGameSkill)
                    }
                }
            }.lparams(matchParent) {
                bottomMargin = getCommonDimen()
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
                    tintImageRes(R.color.colorWhite)
                }.lparams(dimen(R.dimen.dp_18), dimen(R.dimen.dp_18)) {
                    alignParentStart()
                    centerVertically()
                    marginStart = getCommonDimen()
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
                    tintImageRes(R.color.colorWhite)
                }.lparams(dimen(R.dimen.dp_18), dimen(R.dimen.dp_18)) {
                    alignParentStart()
                    centerVertically()
                    marginStart = getCommonDimen()
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
            }
        }.lparams(matchParent, wrapContent) {
        }

        hiddenViews.add(resultCheck)
        hiddenViews.add(successCheck)
        hiddenViews.add(etRaces)
        hiddenViews.add(etClasses)
        handleToggleState(false)
    }

    private fun handleToggleState(full: Boolean) {
        hiddenViews.forEach { it.visibility = if (full) View.VISIBLE else View.GONE }
        toggleStateView.visibility = if (full) View.GONE else View.VISIBLE
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
        if (latestModel !== model) {
            latestModel = model
            etTags.text = null
            tagDisposable.dispose()
            tagDisposable = subscribeTags(model.tagsObservable)
            restrictionsDisposable.dispose()
            restrictionsDisposable = subscribeRestrictions(model)
            handleToggleState(false)
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

    private fun subscribeRestrictions(model: Model): Disposable {
        return model.allRestrictionsObservable
                .subscribeWithErrorLogging {
                    this.allRestrictions = it
                    updateRestrictions(model.userGameSkill.restrictions)
                }
    }

    private fun updateRestrictions(restrictions: MutableList<Restriction>) {
        etClasses.setText(allRestrictions.getClassesRestrictions(restrictions)
                .foldIndexed("") { index: Int, acc: String, restrictionInfo: RestrictionInfo ->
                    acc + (if (index != 0) ", " else "") + restrictionInfo.name
                })

        etRaces.setText(allRestrictions.getRacesRestrictions(restrictions)
                .foldIndexed("") { index: Int, acc: String, restrictionInfo: RestrictionInfo ->
                    acc + (if (index != 0) ", " else "") + restrictionInfo.name
                })
    }

    private fun updateTags(tags: MutableList<String>) {
        tagsContainer.removeAllViews()
        for (tag in tags) {
            addTagView(tag)
        }
    }

    private fun addTagView(tag: String) {
        tagsContainer.run {
            addView(Chip(context)
                    .apply {
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
                    }, LinearLayout.LayoutParams(wrapContent, wrapContent)
                    .apply {
                        marginEnd = getCommonDimen()
                        bottomMargin = getDoubleCommonDimen()
                    })
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

    private fun showRestrictionClasses(gameSkill: UserGameSkill) {
        val chosenRestrictions = gameSkill.restrictions.toMutableList()
        MaterialDialog(context)
                .title(R.string.class_restriction)
                .customView(view = RestrictionInfoDialogView(context, allRestrictions.classes, chosenRestrictions), scrollable = true)
                .negativeButton(android.R.string.cancel)
                .positiveButton(android.R.string.ok, click = {
                    gameSkill.restrictions = chosenRestrictions
                    updateRestrictions(chosenRestrictions)
                }).show()
    }

    private fun showRestrictionRaces(gameSkill: UserGameSkill) {
        val chosenRestrictions = gameSkill.restrictions.toMutableList()
        MaterialDialog(context)
                .title(R.string.race_restriction)
                .customView(view = RestrictionInfoDialogView(context, allRestrictions.races, chosenRestrictions), scrollable = true)
                .negativeButton(android.R.string.cancel)
                .positiveButton(android.R.string.ok, click = {
                    gameSkill.restrictions = chosenRestrictions
                    updateRestrictions(chosenRestrictions)
                }).show()
    }

    fun getRelay() = relay.hide()!!

    override fun onDetachedFromWindow() {
        compositeDisposable.clear()
        tagDisposable.dispose()
        restrictionsDisposable.dispose()
        super.onDetachedFromWindow()
    }

    override fun onShown() {
    }

    override fun onHidden() {
        etTitle.hideKeyboard(100L)
    }

    data class Model(
            val userGameSkill: UserGameSkill,
            val tagsObservable: Observable<List<Tag>>,
            val allRestrictionsObservable: Observable<AllRestrictions>
    )
}