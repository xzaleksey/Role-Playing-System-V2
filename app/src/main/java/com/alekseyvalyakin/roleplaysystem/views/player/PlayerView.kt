package com.alekseyvalyakin.roleplaysystem.views.player

import android.content.Context
import android.view.Gravity
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.data.sound.AudioState
import com.alekseyvalyakin.roleplaysystem.utils.*
import org.jetbrains.anko.*


class PlayerView(context: Context) : _FrameLayout(context) {

    private lateinit var tvText: TextView
    private lateinit var tvSecondary: TextView
    private lateinit var ivIconLeft: ImageView
    private lateinit var ivIconRight: ImageView
    private var seekBar: SeekBar
    private var progressChangedListener: ProgressChangedListener? = null
    private var latestState: AudioState? = null
    private val seekBarListener: SeekBar.OnSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            if (fromUser) {
                progressChangedListener?.onProgressChanged(progress)
                latestState?.run {
                    updateDurationUntilEnd(this.copy(currentProgress = progress))
                }
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }

    }

    init {

        relativeLayout {
            backgroundColorResource = R.color.colorPrimaryDark
            leftPadding = getIntDimen(R.dimen.dp_4)
            rightPadding = getIntDimen(R.dimen.dp_4)

            ivIconLeft = imageView {
                backgroundResource = getSelectableItemBorderless()
                padding = getIntDimen(R.dimen.dp_8)
                tintImageRes(R.color.white54)
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                imageResource = R.drawable.ic_more_vertical
                id = R.id.left_icon
            }.lparams(getIntDimen(R.dimen.dp_40), getIntDimen(R.dimen.dp_40)) {
                alignParentStart()
                centerVertically()
            }

            ivIconRight = imageView {
                backgroundResource = getSelectableItemBorderless()
                padding = getIntDimen(R.dimen.dp_8)
                imageResource = R.drawable.ic_player_view_play
                tintImageRes(R.color.white7)
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                id = R.id.right_icon
            }.lparams(getIntDimen(R.dimen.dp_40), getIntDimen(R.dimen.dp_40)) {
                alignParentEnd()
                centerVertically()
            }

            verticalLayout {
                tvText = textView {
                    id = R.id.tv_text
                    textColorResource = R.color.colorWhite
                    body1Style()
                }.lparams(width = wrapContent) {
                    gravity = Gravity.CENTER_HORIZONTAL
                }

                tvSecondary = textView {
                    textColorResource = R.color.white7
                    captionStyle()
                }.lparams(width = wrapContent) {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
            }.lparams {
                leftOf(R.id.right_icon)
                rightOf(R.id.left_icon)
                below(R.id.tv_text)
                centerInParent()
            }

        }.lparams(matchParent, getIntDimen(R.dimen.dp_56)) {
            topMargin = getIntDimen(R.dimen.dp_12)
        }

        seekBar = seekBar {
            leftPadding = 0
            rightPadding = 0
            topPadding = getIntDimen(R.dimen.dp_8)
            bottomPadding = getIntDimen(R.dimen.dp_8)
            progressDrawable = getCompatDrawable(R.drawable.player_progress_drawable)
            thumb = null
            setOnSeekBarChangeListener(seekBarListener)
        }.lparams(matchParent, getIntDimen(R.dimen.dp_20))
    }

    fun setLeftClickListener(leftOnClickListener: OnClickListener) {
        ivIconLeft.setOnClickListener(leftOnClickListener)
    }

    fun setRightClickListener(rightOnClickListener: OnClickListener) {
        ivIconRight.setOnClickListener(rightOnClickListener)
    }

    fun setProgressListener(progressChangedListener: ProgressChangedListener) {
        this.progressChangedListener = progressChangedListener
    }

    fun update(audioState: AudioState) {
        latestState = audioState
        tvText.text = audioState.fileNameWithoutExtension()
        updateDurationUntilEnd(audioState)
        seekBar.progress = audioState.currentProgress

        if (audioState.isPlaying) {
            ivIconRight.imageResource = R.drawable.ic_player_pause
        } else {
            ivIconRight.imageResource = R.drawable.ic_player_view_play
        }
    }

    private fun updateDurationUntilEnd(audioState: AudioState) {
        tvSecondary.text = audioState.getDurationUntilEnd()
    }

    interface ProgressChangedListener {
        fun onProgressChanged(progress: Int)
    }

}

