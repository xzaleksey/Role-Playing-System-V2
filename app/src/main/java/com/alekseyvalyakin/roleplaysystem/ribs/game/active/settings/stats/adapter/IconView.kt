package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.adapter

import android.content.Context
import android.view.Gravity
import android.widget.ImageView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getDoubleCommonDimen
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import com.alekseyvalyakin.roleplaysystem.utils.getSelectableItemBackGround
import org.jetbrains.anko._FrameLayout
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.imageView
import org.jetbrains.anko.padding

class IconView(context: Context) : _FrameLayout(context) {
    private var ivIcon: ImageView

    init {
        backgroundResource = getSelectableItemBackGround()
        padding = getDoubleCommonDimen()
        ivIcon = imageView {

        }.lparams(getIntDimen(R.dimen.dp_40), getIntDimen(R.dimen.dp_40)) {
            gravity = Gravity.CENTER
        }
    }

    fun update(iconViewModel: IconViewModel) {
        ivIcon.setImageDrawable(iconViewModel.drawable)
    }
}