package com.alekseyvalyakin.roleplaysystem.views.backdrop

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.*
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

class DefaultFrontView(context: Context) : _RelativeLayout(context) {

    private lateinit var rightIcon: ImageView

    init {
        backgroundColorResource = R.color.colorWhite
        topPadding = getIntDimen(R.dimen.dp_12)

        relativeLayout {
            id = R.id.top_container

            rightIcon = imageView {
                id = R.id.right_icon
                imageResource = R.drawable.ic_add_black_24dp
                backgroundResource = getSelectableItemBorderless()

            }.lparams(getIntDimen(R.dimen.dp_24), getIntDimen(R.dimen.dp_24)) {
                rightMargin = getDoubleCommonDimen()
                leftMargin = getCommonDimen()
                alignParentEnd()
            }

            textView {
                id = R.id.tv_title
                textSizeDimen = R.dimen.dp_16
                text = "Title"
            }.lparams(width = matchParent) {
                leftMargin = getDoubleCommonDimen()
                startOf(rightIcon)
            }

            view {
                id = R.id.divider
                background = context.getDividerDrawable()
            }.lparams(width = matchParent, height = getIntDimen(R.dimen.dp_1)) {
                below(R.id.tv_title)
                topMargin = getIntDimen(R.dimen.dp_12)
            }

        }.lparams(matchParent, wrapContent)

        recyclerView {
            layoutManager = LinearLayoutManager(context)
            backgroundColorResource = R.color.blackColor54
        }.lparams(width = matchParent, height = matchParent) {
            below(R.id.top_container)
        }
    }
}