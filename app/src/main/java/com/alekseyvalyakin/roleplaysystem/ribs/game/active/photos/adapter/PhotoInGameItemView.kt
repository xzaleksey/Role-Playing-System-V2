package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.adapter

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.base.image.ImageProvider
import com.alekseyvalyakin.roleplaysystem.utils.*
import io.reactivex.disposables.Disposables
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.switchCompat
import org.jetbrains.anko.cardview.v7._CardView

class PhotoInGameItemView(context: Context) : _CardView(context) {

    private lateinit var photoImageView: ImageView
    private lateinit var tvName: TextView
    private lateinit var masterContainer: RelativeLayout
    private var imageProvider: ImageProvider? = null
    private lateinit var divider: View
    private var disposable = Disposables.disposed()
    private lateinit var switcherVisibility: SwitchCompat
    private lateinit var bottomContainer: View
    private lateinit var anchorView: View

    init {
        id = R.id.card_view
        radius = getFloatDimen(R.dimen.dp_2)
        relativeLayout {
            anchorView = view {

            }.lparams(matchParent, 0) {
                alignParentLeft()
                alignParentTop()
            }
            photoImageView = imageView {
                id = R.id.icon
                scaleType = ImageView.ScaleType.CENTER_CROP
            }.lparams(width = matchParent, height = getIntDimen(R.dimen.dp_132))
            bottomContainer = relativeLayout {
                leftPadding = getCommonDimen()
                rightPadding = getCommonDimen()
                backgroundResource = getSelectableItemBackGround()
                topPadding = getCommonDimen()

                tvName = textView {
                    id = R.id.tv_name
                    maxLines = 2
                    textColorResource = R.color.colorTextPrimary
                    textSizeDimen = R.dimen.dp_14
                }.lparams(width = matchParent) {
                    bottomMargin = getCommonDimen()
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
                   switcherOnClickListener: OnClickListener,
                   photoClickListener: OnClickListener,
                   longClickListener: OnLongClickListener) {
        layoutParams.width = size
        photoImageView.layoutParams.height = size
        tvName.text = name
        divider.visibility = if (isMaster) View.VISIBLE else View.GONE
        masterContainer.visibility = if (isMaster) View.VISIBLE else View.GONE
        switcherVisibility.isChecked = isChecked
        switcherVisibility.setOnClickListener(switcherOnClickListener)
        photoImageView.setOnClickListener(photoClickListener)
        photoImageView.setOnLongClickListener(longClickListener)
        setOnLongClickListener(longClickListener)
        if (this.imageProvider == null || this.imageProvider!!.getId() != imageProvider.getId()) {
            this.imageProvider = imageProvider
            photoImageView.setImageDrawable(null)
            subscribeImage()
        }
    }

    fun getAnchorView(): View {
        return anchorView
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