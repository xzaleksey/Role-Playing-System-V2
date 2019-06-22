package com.alekseyvalyakin.roleplaysystem.ribs.auth

import android.content.Context
import android.text.InputType
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.google.android.gms.common.SignInButton
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.*
import org.jetbrains.anko.design.textInputEditText
import org.jetbrains.anko.design.textInputLayout


/**
 * Top level view for {@link AuthBuilder.AuthScope}.
 */
class AuthView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : CoordinatorLayout(context, attrs, defStyle), AuthInteractor.AuthPresenter {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var forgotPasswordBtn: Button
    private lateinit var googleAuthBtn: SignInButton
    private lateinit var loginBtn: Button
    private lateinit var signUpBtn: Button
    private lateinit var passwordInputLayout: TextInputLayout
    private var scrollView: ScrollView

    private lateinit var emailInputLayout: TextInputLayout

    private val errorInvalidEmail = getString(R.string.error_invalid_email)
    private val errorEmptyField = getString(R.string.error_empty_field)
    private val errorMinSymbols = getString(R.string.error_min_symbols)
    private val relay = PublishRelay.create<AuthInteractor.AuthPresenter.Events>()

    init {
        AnkoContext.createDelegate(this).apply {
            backgroundColor = context.getCompatColor(R.color.colorBlack)
            val padding = context.getIntDimen(R.dimen.dp_16)
            bottomPadding = padding
            leftPadding = padding
            rightPadding = padding
            topPadding = padding

            scrollView = themedScrollView(R.style.AppTheme_TextWhite) {
                id = R.id.login_form

                linearLayout {
                    id = R.id.email_login_form
                    orientation = LinearLayout.VERTICAL

                    textView(R.string.app_name) {
                        gravity = Gravity.CENTER_HORIZONTAL
                        setTextSizeFromRes(R.dimen.dp_18)
                        textColorResource = R.color.colorWhite
                    }.lparams(width = wrapContent, height = wrapContent) {
                        bottomMargin = getIntDimen(R.dimen.dp_40)
                    }

                    emailInputLayout = textInputLayout {
                        id = R.id.email_input_layout
                        isErrorEnabled = true

                        email = autoCompleteTextView {
                            hint = getString(R.string.prompt_email)
                            id = R.id.email
                            imeOptions = EditorInfo.IME_ACTION_NEXT
                            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                            maxLines = 1
                        }
                    }.lparams(width = matchParent, height = wrapContent)
                    passwordInputLayout = textInputLayout {
                        hint = getString(R.string.prompt_password)
                        id = R.id.password_input_layout
                        isErrorEnabled = true

                        password = textInputEditText {
                            id = R.id.password
                            setImeActionLabel(getString(R.string.action_sign_in_short), R.id.sign_in_button)
                            imeOptions = EditorInfo.IME_ACTION_DONE
                            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                            maxLines = 1
                        }
                    }.lparams(width = matchParent, height = wrapContent)
                    loginBtn = button(R.string.action_sign_in) {
                        id = R.id.sign_in_button
                        backgroundResource = R.drawable.black_btn_selector
                        textResource = R.string.action_sign_in
                    }.lparams(width = matchParent, height = wrapContent)

                    signUpBtn = button(R.string.action_sign_up) {
                        id = R.id.sign_up_button
                        backgroundResource = R.drawable.black_btn_selector
                        textResource = R.string.action_sign_up
                    }.lparams(width = matchParent, height = wrapContent)

                    googleAuthBtn = googleSignInButton {
                        id = R.id.auth_btn
                        gravity = Gravity.CENTER_HORIZONTAL
                    }.lparams(width = wrapContent, height = wrapContent) {
                        topMargin = context.getIntDimen(R.dimen.dp_16)
                    }

                    forgotPasswordBtn = button(R.string.forgot_password) {
                        textResource = R.string.forgot_password
                        id = R.id.forgot_password
                        background = null
                    }.lparams(width = wrapContent, height = wrapContent) {
                        gravity = Gravity.CENTER_HORIZONTAL
                        topMargin = getIntDimen(R.dimen.dp_20)
                    }
                }
            }
            scrollView.layoutParams = CoordinatorLayout.LayoutParams(matchParent, wrapContent).apply {
                this.gravity = Gravity.CENTER
            }
        }
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initSubscriptions()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        compositeDisposable.clear()
    }

    private fun initSubscriptions() {
        compositeDisposable.add(RxTextView.textChanges(email).skip(1).subscribe { charSequence ->
            when {
                TextUtils.isEmpty(charSequence) -> emailInputLayout.error = String.format(errorEmptyField, getString(R.string.email))
                ValidationUtils.isValidEmail(charSequence) -> emailInputLayout.error = StringUtils.EMPTY_STRING
                else -> emailInputLayout.error = errorInvalidEmail
            }
        })
        compositeDisposable.add(RxTextView.textChanges(password).skip(1).subscribe { charSequence ->
            when {
                TextUtils.isEmpty(charSequence) -> passwordInputLayout.error = String.format(errorEmptyField, getString(R.string.password))
                ValidationUtils.isValidLength(charSequence) -> passwordInputLayout.error = StringUtils.EMPTY_STRING
                else -> passwordInputLayout.error = String.format(errorMinSymbols, getString(R.string.password))
            }
        })
    }

    override fun observeUiEvents(): Observable<AuthInteractor.AuthPresenter.Events> {
        return Observable.merge(listOf(RxView.clicks(loginBtn).map { AuthInteractor.AuthPresenter.Events.Login(getEmail(), getPassword()) },
                RxView.clicks(googleAuthBtn).map { AuthInteractor.AuthPresenter.Events.GoogleSignIn },
                RxView.clicks(signUpBtn).map { AuthInteractor.AuthPresenter.Events.SignUp(getEmail(), getPassword()) },
                RxView.clicks(forgotPasswordBtn).map { AuthInteractor.AuthPresenter.Events.ForgotPassword },
                signInAction(),
                relay))
    }

    private fun getPassword() = password.text.toString()

    override fun getEmail() = email.text.toString()

    override fun restoreEmail(email: String) {
        this.email.setText(email)
        this.email.setSelection(email.length)
    }

    private fun signInAction(): Observable<AuthInteractor.AuthPresenter.Events> {
        return RxTextView.editorActionEvents(password) {
            var handled = false
            if (it.actionId() == EditorInfo.IME_ACTION_DONE) {
                handled = true
            }
            return@editorActionEvents handled
        }.map { AuthInteractor.AuthPresenter.Events.Login(getEmail(), getPassword()) }
    }

    override fun showError(localizedMessage: String) {
        showSnack(localizedMessage)
    }

    override fun hideKeyboard() {
        hideKeyboard(0L)
    }

    override fun showResetPasswordDialog() {
        MaterialDialog(context)
                .title(R.string.reset_password)
                .message(R.string.reset_password_text)
                .positiveButton(android.R.string.ok, null, {
                    relay.accept(AuthInteractor.AuthPresenter.Events.ResetPassword(getEmail()))
                })
                .negativeButton(android.R.string.cancel)
                .show()
    }
}
