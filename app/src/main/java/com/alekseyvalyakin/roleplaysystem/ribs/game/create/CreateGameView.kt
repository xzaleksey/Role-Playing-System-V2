package com.alekseyvalyakin.roleplaysystem.ribs.game.create

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.view.Gravity
import android.widget.EditText
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.themedFloatingActionButton

/**
 * Top level view for {@link CreateGameBuilder.CreateGameScope}.
 */
class CreateGameView constructor(
        context: Context
) : _FrameLayout(context), CreateGameInteractor.CreateGamePresenter {

    private lateinit var stepTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var inputEditText: EditText
    private lateinit var fab: FloatingActionButton
    private lateinit var exampleText: TextView

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
                    setSanserifMediumTypeface()
                    textColorResource = R.color.colorWhite
                    setTextSizeFromRes(R.dimen.sp_20)
                }.lparams(width = wrapContent, height = wrapContent) {
                    topMargin = getIntDimen(R.dimen.dp_24)
                    below(stepTextView)
                }

                inputEditText = themedEditText(R.style.AppTheme_TextWhite) {
                    id = R.id.input_et
                    hint = "hint"
                }.lparams(width = matchParent, height = wrapContent) {
                    leftMargin= - getIntDimen(R.dimen.dp_4)
                    below(titleTextView)
                }

                exampleText = themedTextView(R.style.AppTheme_TextWhite) {
                    id = R.id.text
                    text = "example"
                    setTextSizeFromRes(R.dimen.sp_12)
                }.lparams(width = matchParent, height = wrapContent) {
                    below(inputEditText)
                }

                frameLayout {
                    fab = themedFloatingActionButton(R.style.AppTheme_TextWhite) {
                        imageResource = R.drawable.ic_arrow_right
                    }.lparams(width = wrapContent, height = wrapContent) {
                        gravity = Gravity.BOTTOM or Gravity.END
                    }
                }.lparams(width = matchParent, height = matchParent) {
                    topMargin = getIntDimen(R.dimen.dp_16)
                    below(exampleText)
                }

            }.lparams(width = matchParent, height = matchParent) {
                topMargin = getIntDimen(R.dimen.dp_32) + getStatusBarHeight()
                leftMargin = getIntDimen(R.dimen.dp_40)
                rightMargin = getIntDimen(R.dimen.dp_40)
                bottomMargin = getIntDimen(R.dimen.dp_40)
            }
        }
    }
}
