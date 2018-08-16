package com.alekseyvalyakin.roleplaysystem.ribs.profile

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.input.InputCallback
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView


/**
 * Top level view for {@link ProfileBuilder.ProfileScope}.
 */
class ProfileView constructor(
        context: Context
) : _LinearLayout(context), ProfilePresenter {

    private lateinit var btnBack: ImageView
    private lateinit var btnEdit: ImageView
    private lateinit var btnChooseImage: ImageView
    private lateinit var tvTotalGamesCount: TextView
    private lateinit var tvEmail: TextView
    private lateinit var ivAvatar: ImageView
    private lateinit var tvDisplayName: TextView
    private lateinit var tvMasterGamesCount: TextView
    private val recyclerView: RecyclerView
    private var currentDialog: Dialog? = null
    private val relay = PublishRelay.create<ProfilePresenter.Event>()

    init {
        id = R.id.main_container
        backgroundColor = getCompatColor(R.color.backgroundColor)
        orientation = LinearLayout.VERTICAL

        relativeLayout {
            clipChildren = false
            imageView {
                id = R.id.iv_background
                isFocusable = false
                scaleType = ImageView.ScaleType.CENTER_CROP
                imageResource = R.drawable.top_background
            }.lparams(width = matchParent, height = getIntDimen(R.dimen.profile_background_height))
            relativeLayout {
                id = R.id.top_actions_container
                clipChildren = false

                btnBack = imageView {
                    id = R.id.iv_back
                    padding = getIntDimen(R.dimen.dp_8)
                    backgroundResource = getSelectableItemBorderless()
                    tintImage(R.color.colorWhite)
                    imageResource = R.drawable.ic_arrow_back
                }.lparams(width = getIntDimen(R.dimen.dp_40), height = getIntDimen(R.dimen.dp_40)) {
                    marginStart = getIntDimen(R.dimen.dp_8)
                    topMargin = getIntDimen(R.dimen.dp_8)
                }

                btnEdit = imageView {
                    id = R.id.iv_edit
                    padding = getIntDimen(R.dimen.dp_8)
                    backgroundResource = getSelectableItemBorderless()
                    tintImage(R.color.colorWhite)
                    visibility = View.INVISIBLE
                    imageResource = R.drawable.ic_mode_edit_black_24dp
                }.lparams(width = getIntDimen(R.dimen.dp_40), height = getIntDimen(R.dimen.dp_40)) {
                    alignParentRight()
                    marginEnd = getIntDimen(R.dimen.dp_8)
                    topMargin = getIntDimen(R.dimen.dp_8)
                }

                tvDisplayName = textView {
                    id = R.id.display_name
                    gravity = Gravity.CENTER
                    maxLines = 1
                    text = resources.getString(R.string.total_games)
                    textColor = Color.WHITE
                    setTextSizeFromRes(R.dimen.dp_24)
                }.lparams(width = matchParent) {
                    topMargin = getIntDimen(R.dimen.dp_8)
                    centerHorizontally()
                    leftOf(R.id.iv_edit)
                    rightOf(R.id.iv_back)
                }

                tvEmail = textView {
                    id = R.id.tv_email
                    gravity = Gravity.CENTER
                    maxLines = 1
                    text = resources.getString(R.string.total_games)
                    textColor = getCompatColor(R.color.white7)
                    setTextSizeFromRes(R.dimen.dp_16)
                }.lparams(width = matchParent) {
                    below(R.id.display_name)
                    centerHorizontally()
                }
            }.lparams(width = matchParent, height = matchParent) {
                topMargin = dimen(R.dimen.status_bar_height)
            }
            view {
                backgroundColor = Color.WHITE
            }.lparams(width = matchParent, height = matchParent) {
                below(R.id.iv_background)
            }

            ivAvatar = imageView {
                id = R.id.avatar
                backgroundResource = R.drawable.profile_border
                padding = getIntDimen(R.dimen.dp_6)
                scaleType = ImageView.ScaleType.CENTER_CROP
            }.lparams(width = getIntDimen(R.dimen.dp_100), height = getIntDimen(R.dimen.dp_100)) {
                centerHorizontally()
                topMargin = getIntDimen(R.dimen.profile_avatar_top_margin)
            }

            btnChooseImage = imageView {
                id = R.id.fab_image
                backgroundResource = getSelectableItemBorderless()
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                visibility = View.GONE
                imageResource = R.drawable.ic_profile_edit
            }.lparams(width = getIntDimen(R.dimen.dp_40), height = getIntDimen(R.dimen.dp_40)) {
                below(R.id.avatar)
                leftMargin = getIntDimen(R.dimen.dp_30_inverse)
                marginStart = getIntDimen(R.dimen.dp_40_inverse)
                topMargin = getIntDimen(R.dimen.dp_30_inverse)
                rightOf(R.id.avatar)
            }

            tvTotalGamesCount = textView {
                id = R.id.tv_total_games_count
                text = "2"
                setRufinaRegularTypeface()
                gravity = Gravity.CENTER
                textColor = getCompatColor(R.color.colorPrimary)
                setTextSizeFromRes(R.dimen.dp_24)
            }.lparams(width = matchParent) {
                below(R.id.iv_background)
                topMargin = getIntDimen(R.dimen.dp_10)
                leftOf(R.id.avatar)
            }
            textView {
                gravity = Gravity.CENTER
                text = resources.getString(R.string.total_games)
                textColor = getCompatColor(R.color.colorPrimary)
            }.lparams(width = matchParent) {
                below(R.id.tv_total_games_count)
                topMargin = getIntDimen(R.dimen.dp_2)
                leftOf(R.id.avatar)
            }

            tvMasterGamesCount = textView {
                id = R.id.tv_master_games_count

                text = "1"
                setRufinaRegularTypeface()
                gravity = Gravity.CENTER
                textColor = getCompatColor(R.color.colorPrimary)
                setTextSizeFromRes(R.dimen.dp_24)
            }.lparams(width = matchParent) {
                below(R.id.iv_background)
                topMargin = getIntDimen(R.dimen.dp_10)
                rightOf(R.id.avatar)
            }
            textView {
                gravity = Gravity.CENTER
                text = resources.getString(R.string.master_games)
                textColor = getCompatColor(R.color.colorPrimary)
            }.lparams(width = matchParent) {
                below(R.id.tv_master_games_count)
                topMargin = getIntDimen(R.dimen.dp_2)
                rightOf(R.id.avatar)
            }
            view {
                id = R.id.divider
                backgroundResource = R.drawable.shadow_bottom_divider
            }.lparams(width = matchParent, height = getIntDimen(R.dimen.dp_3)) {
                alignParentBottom()
            }
        }.lparams(width = matchParent, height = getIntDimen(R.dimen.profile_top_content_height))

        recyclerView = recyclerView {
            id = R.id.recycler_view
            isVerticalScrollBarEnabled = true
            layoutManager = LinearLayoutManager(context)
        }.lparams(width = matchParent, height = matchParent)
    }

    override fun updateViewModel(profileViewModel: ProfileViewModel) {
        tvDisplayName.text = profileViewModel.displayName
        tvEmail.text = profileViewModel.email
        tvMasterGamesCount.text = profileViewModel.totalMasterGamesCount
        tvTotalGamesCount.text = profileViewModel.totalGamesCount
        if (profileViewModel.isEditor) {
            btnEdit.visibility = View.VISIBLE
            btnChooseImage.visibility = View.VISIBLE
        } else {
            btnEdit.visibility = View.INVISIBLE
            btnChooseImage.visibility = View.GONE
        }
    }

    override fun observeUiEvents(): Observable<ProfilePresenter.Event> {
        return Observable.merge(observeBackPress(), observeEditNamePress(), relay)
    }

    private fun observeBackPress(): Observable<ProfilePresenter.Event> {
        return RxView.clicks(btnBack).map { ProfilePresenter.Event.BackPress }
    }

    private fun observeEditNamePress(): Observable<ProfilePresenter.Event> {
        return RxView.clicks(btnEdit).map { ProfilePresenter.Event.EditNamePress }
    }


    override fun showEditDisplayNameDialog(displayName: String) {
        if (currentDialog != null) {
            return
        }
        currentDialog = MaterialDialog(context)
                .title(R.string.name)
                .positiveButton(res = android.R.string.ok, click = {
                    relay.accept(ProfilePresenter.Event.EditNameConfirm(it.getInputField()!!.text.toString()))
                })
                .negativeButton(res = android.R.string.cancel)
                .input(hint = getString(R.string.input_name),
                        waitForPositiveButton = false,
                        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES,
                        prefill = displayName, callback = object : InputCallback {
                    override fun invoke(dialog: MaterialDialog, text: CharSequence) {
                        dialog.setActionButtonEnabled(WhichButton.POSITIVE, !text.isBlank())
                    }
                })
        currentDialog?.setOnDismissListener { currentDialog = null }
        currentDialog?.show()
    }


}
