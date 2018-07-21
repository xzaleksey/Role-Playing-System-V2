package com.alekseyvalyakin.roleplaysystem.ribs.auth

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.Gravity.CENTER
import android.widget.EditText
import android.widget.LinearLayout
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getCompatColor
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import com.alekseyvalyakin.roleplaysystem.utils.googleSignInButton
import com.alekseyvalyakin.roleplaysystem.utils.setTextSizeFromRes
import org.jetbrains.anko.*
import org.jetbrains.anko.design.textInputEditText
import org.jetbrains.anko.design.textInputLayout

/**
 * Top level view for {@link AuthBuilder.AuthScope}.
 */
class AuthView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle), AuthInteractor.AuthPresenter {

    private lateinit var login: EditText
    private lateinit var password: EditText

    init {
        AnkoContext.createDelegate(this).apply {
            backgroundColor = context.getCompatColor(R.color.colorBlack)
            verticalGravity = CENTER
            val padding = context.getIntDimen(R.dimen.dp_16)
            bottomPadding = padding
            leftPadding = padding
            rightPadding = padding
            topPadding = padding

            themedScrollView(R.style.AppTheme_TextWhite, {
                id = Ids.login_form
                orientation = LinearLayout.VERTICAL

                linearLayout {
                    id = Ids.email_login_form
                    orientation = LinearLayout.VERTICAL

                    textView(R.string.app_name) {
                        gravity = Gravity.CENTER_HORIZONTAL
                        setTextSizeFromRes(R.dimen.sp_18)
                        textColorResource = R.color.colorWhite
                    }.lparams(width = wrapContent, height = wrapContent) {
                        bottomMargin = getIntDimen(R.dimen.dp_40)
                    }

                    textInputLayout {
                        id = Ids.email_input_layout
                        isErrorEnabled = true

                        autoCompleteTextView {
                            hint = context.getString(R.string.prompt_email)
                            id = Ids.email
                            imeOptions = 5
                            inputType = 33
                            maxLines = 1
                        }
                    }.lparams(width = matchParent, height = wrapContent)
                    textInputLayout {
                        hint = context.getString(R.string.prompt_password)
                        id = Ids.password_input_layout
                        isErrorEnabled = true

                        textInputEditText {
                            id = Ids.password
                            setImeActionLabel(context.getString(R.string.action_sign_in_short), Ids.sign_in_button)
                            imeOptions = 0
                            inputType = 129
                            maxLines = 1
                        }
                    }.lparams(width = matchParent, height = wrapContent)
                    button(R.string.action_sign_in) {
                        id = Ids.sign_in_button
                        backgroundResource = R.drawable.black_btn_selector
                        textResource = R.string.action_sign_in
                    }.lparams(width = matchParent, height = wrapContent)

                    button(R.string.action_sign_up) {
                        id = Ids.sign_up_button
                        backgroundResource = R.drawable.black_btn_selector
                        textResource = R.string.action_sign_up
                    }.lparams(width = matchParent, height = wrapContent)

                    googleSignInButton({
                        id = Ids.auth_button
                        gravity = Gravity.CENTER_HORIZONTAL
                    }).lparams(width = wrapContent, height = wrapContent) {
                        topMargin = context.getIntDimen(R.dimen.dp_16)
                    }

                    button(R.string.forgot_password) {
                        textResource = R.string.forgot_password
                        id = Ids.forgot_password
                        background = null
                    }.lparams(width = wrapContent, height = wrapContent) {
                        gravity = Gravity.CENTER_HORIZONTAL
                        topMargin = getIntDimen(R.dimen.dp_20)
                    }

                }
            })
        }


    }

    private object Ids {
        val auth_button = 1
        val email = 2
        val email_input_layout = 3
        val email_login_form = 4
        val forgot_password = 5
        val login_form = 6
        val password = 8
        val password_input_layout = 9
        val sign_in_button = 10
        val sign_up_button = 11
    }
}
