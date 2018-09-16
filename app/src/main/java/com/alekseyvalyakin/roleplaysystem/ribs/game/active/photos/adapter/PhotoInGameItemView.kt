package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.adapter

import android.content.Context
import android.support.v7.widget.SwitchCompat
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.base.image.ImageProvider
import com.alekseyvalyakin.roleplaysystem.utils.*
import io.reactivex.disposables.Disposables
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.switchCompat
import org.jetbrains.anko.cardview.v7._CardView

class PhotoInGameItemView(context: Context) : _CardView(context) {

    private lateinit var photoImageView: ImageView
    private lateinit var deleteImageView: ImageView
    private lateinit var tvName: TextView
    private lateinit var masterContainer: RelativeLayout
    private var imageProvider: ImageProvider? = null
    private lateinit var divider: View
    private var disposable = Disposables.disposed()
    private lateinit var switcherVisibility: SwitchCompat

    init {
        id = R.id.card_view
        radius = getFloatDimen(R.dimen.dp_2)
        relativeLayout {
            photoImageView = imageView {
                id = R.id.icon
                scaleType = ImageView.ScaleType.CENTER_CROP
            }.lparams(width = matchParent, height = getIntDimen(R.dimen.dp_132))
            relativeLayout {
                leftPadding = getCommonDimen()
                rightPadding = getCommonDimen()
                topPadding = getCommonDimen()
                deleteImageView = imageView {
                    id = R.id.iv_delete
                    backgroundResource = getSelectableItemBorderless()
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    padding = getIntDimen(R.dimen.dp_2)
                    tintImage(R.color.colorTextSecondary)
                    visibility = View.GONE
                    imageResource = R.drawable.ic_delete_black_24dp
                }.lparams(width = getIntDimen(R.dimen.dp_24), height = getIntDimen(R.dimen.dp_24)) {
                    alignParentRight()
                    bottomMargin = getCommonDimen()
                }
                tvName = textView {
                    id = R.id.tv_name
                    maxLines = 2
                    textColorResource = R.color.colorTextPrimary
                    textSizeDimen = R.dimen.dp_14
                }.lparams(width = matchParent) {
                    bottomMargin = getCommonDimen()
                    leftOf(R.id.iv_delete)
                }

                divider = view {
                    id = R.id.divider
                    backgroundColorResource = R.color.colorDivider
                }.lparams(width = matchParent, height = dip(1)) {
                    below(R.id.tv_name)
                }

                masterContainer = relativeLayout {
                    id = R.id.bottom_container
                    switcherVisibility = switchCompat {
                        id = R.id.switcher
                    }.lparams {
                        centerHorizontally()
                        leftMargin = getIntDimen(R.dimen.dp_4)
                        rightMargin = getIntDimen(R.dimen.dp_4)
                    }
                    textView {
                        gravity = Gravity.START
                        text = resources.getString(R.string.master)
                        textColorResource = R.color.colorTextSecondary
                        textSizeDimen = R.dimen.dp_12
                    }.lparams {
                        alignParentLeft()
                        centerVertically()
                        bottomMargin = getCommonDimen()
                        leftOf(R.id.switcher)
                    }
                    textView {
                        gravity = Gravity.END
                        text = resources.getString(R.string.game_players)
                        textColorResource = R.color.colorTextSecondary
                        textSizeDimen = R.dimen.dp_12
                    }.lparams {
                        alignParentRight()
                        centerVertically()
                        rightOf(R.id.switcher)
                    }
                }.lparams(width = matchParent) {
                    below(R.id.divider)
                }
            }.lparams(width = matchParent) {
                below(R.id.icon)
            }
        }
    }

    fun updateView(size: Int,
                   name: String,
                   isMaster: Boolean,
                   isChecked: Boolean,
                   imageProvider: ImageProvider,
                   deleteClickListener: OnClickListener,
                   switcherOnClickListener: OnClickListener) {
        layoutParams.width = size
        photoImageView.layoutParams.height = size
        tvName.text = name
        divider.visibility = if (isMaster) View.VISIBLE else View.GONE
        masterContainer.visibility = if (isMaster) View.VISIBLE else View.GONE
        deleteImageView.visibility = if (isMaster) View.VISIBLE else View.GONE
        deleteImageView.setOnClickListener(deleteClickListener)
        switcherVisibility.isChecked = isChecked
        switcherVisibility.setOnClickListener(switcherOnClickListener)

        if (this.imageProvider == null || this.imageProvider!!.getId() != imageProvider.getId()) {
            this.imageProvider = imageProvider
            photoImageView.setImageDrawable(null)
            subscribeImage()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        subscribeImage()
    }

    private fun subscribeImage() {
        disposable.dispose()
        imageProvider?.let {
            disposable = it.observeImage()
                    .subscribeWithErrorLogging { imageHolder ->
                        photoImageView.setImageBitmap(imageHolder.getBitmap())
                    }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposable.dispose()
    }
}