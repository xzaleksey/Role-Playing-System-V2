package com.alekseyvalyakin.roleplaysystem.ribs.game.create

import android.content.Context
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getCompatColor
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import com.alekseyvalyakin.roleplaysystem.utils.getStatusBarHeight
import com.alekseyvalyakin.roleplaysystem.utils.setTextSizeFromRes
import org.jetbrains.anko.*

/**
 * Top level view for {@link CreateGameBuilder.CreateGameScope}.
 */
class CreateGameView constructor(
        context: Context
) : _ScrollView(context), CreateGameInteractor.CreateGamePresenter {

    private lateinit var stepTextView: TextView
    private lateinit var titleTextView: TextView

    init {
        backgroundColor = getCompatColor(R.color.colorPrimaryDark)
        AnkoContext.createDelegate(this).apply {
            relativeLayout {

                stepTextView = textView {
                    text = "test"
                    id = R.id.step_text
                    textColorResource = R.color.colorWhite
                    setTextSizeFromRes(R.dimen.sp_16)
                }.lparams(width = wrapContent, height = wrapContent)

                titleTextView = textView {
                    text = "title"
                    id = R.id.title
                    textColorResource = R.color.colorWhite
                    setTextSizeFromRes(R.dimen.sp_20)
                }.lparams(width = wrapContent, height = wrapContent) {
                    topMargin = getIntDimen(R.dimen.dp_24)
                    below(stepTextView)
                }

            }.lparams(width = matchParent, height = wrapContent) {
                topMargin = getIntDimen(R.dimen.dp_32) + getStatusBarHeight()
                leftMargin = getIntDimen(R.dimen.dp_40)
                rightMargin = getIntDimen(R.dimen.dp_40)
            }

        }
    }
}
