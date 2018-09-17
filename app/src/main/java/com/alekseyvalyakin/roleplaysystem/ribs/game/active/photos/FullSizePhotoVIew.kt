package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import android.content.Context
import android.graphics.Color
import android.view.View
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.photoView
import org.jetbrains.anko.*

class FullSizePhotoVIew(context: Context) : _RelativeLayout(context) {

    init {
        backgroundColor = Color.BLACK
        photoView {
            id = R.id.photo_container
        }.lparams(width = matchParent, height = matchParent) {
            alignParentLeft()
            alignParentTop()
        }
        themedButton(R.style.BrandButtonStyle) {
            id = R.id.btn_retry
            text = resources.getString(R.string.retry_error_button)
            visibility = View.GONE

        }.lparams {
            centerInParent()
        }
        progressBar {
            id = R.id.progress_circular
            indeterminateDrawable.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY)
            isIndeterminate = true
            visibility = View.GONE
        }.lparams {
            centerInParent()
        }
    }
}