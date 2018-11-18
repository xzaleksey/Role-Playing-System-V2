package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.alekseyvalyakin.roleplaysystem.utils.animation.PulseAnimatorX1_5
import com.daimajia.androidanimations.library.YoYo
import com.daimajia.androidanimations.library.YoYo.INFINITE
import org.jetbrains.anko.*

class AudioItemView(context: Context) : _FrameLayout(context) {

    private lateinit var tvText: TextView
    private lateinit var tvSecondary: TextView
    private lateinit var ivIcon: ImageView
    private var yoYo: YoYo.YoYoString? = null

    init {
        backgroundResource = R.drawable.audio_item_view_background

        relativeLayout {

            leftPadding = getDoubleCommonDimen()
            rightPadding = getDoubleCommonDimen()
            topPadding = getIntDimen(R.dimen.dp_10)
            bottomPadding = getIntDimen(R.dimen.dp_10)
            backgroundResource = getSelectableItemBackGround()

            ivIcon = imageView {
                imageResource = R.drawable.ic_play
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                id = R.id.icon
            }.lparams(getIntDimen(R.dimen.dp_24), getIntDimen(R.dimen.dp_24)) {
                alignParentEnd()
                centerVertically()
            }

            tvText = textView {
                id = R.id.tv_text
                textColorResource = R.color.colorTextPrimary
                body1Style()
            }.lparams(width = matchParent) {
                alignParentLeft()
                leftOf(R.id.icon)
            }

            tvSecondary = textView {
                textColorResource = R.color.colorTextSecondary
                captionStyle()
            }.lparams(width = matchParent) {
                alignParentLeft()
                leftOf(R.id.icon)
                below(R.id.tv_text)
            }
        }.lparams(matchParent)
    }


    fun update(audioItemViewModel: AudioItemViewModel, onClickListener: OnClickListener) {
        tvText.text = audioItemViewModel.text
        tvSecondary.text = audioItemViewModel.secondaryText
        val selected = audioItemViewModel.selected
        isSelected = selected
        setOnClickListener(onClickListener)
        ivIcon.imageResource = if (audioItemViewModel.isPlaying) R.drawable.ic_audio_selected else R.drawable.ic_play
        if (audioItemViewModel.isPlaying) {
            ivIcon.post {
                yoYo = YoYo.with(PulseAnimatorX1_5())
                        .duration(500L)
                        .repeat(INFINITE)
                        .playOn(ivIcon)
            }

        } else {
            yoYo?.stop(true)
        }
    }

}