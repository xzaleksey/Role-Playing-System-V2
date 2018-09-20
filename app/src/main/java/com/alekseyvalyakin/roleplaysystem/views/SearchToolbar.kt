package com.alekseyvalyakin.roleplaysystem.views

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.*
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView

class SearchToolbar constructor(
        context: Context
) : _FrameLayout(context) {

    private lateinit var bgImageView: ImageView
    private lateinit var clearIcon: ImageView
    private lateinit var leftIcon: ImageView
    private lateinit var rightIcon: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var searchEditText: EditText
    private lateinit var searchContainer: ViewGroup
    private var isSearchMode = false

    init {
        AnkoContext.createDelegate(this).apply {

            cardView {
                radius = 0f
                cardElevation = getFloatDimen(R.dimen.dp_20)
                minimumHeight = getIntDimen(R.dimen.dp_56)

                bgImageView = imageView {
                    id = R.id.toolbar_background_image_view
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    imageResource = R.drawable.top_background
                }.lparams(width = matchParent, height = 0)

                searchContainer = relativeLayout {
                    id = R.id.top_toolbar_container
                    backgroundResource = R.drawable.toolbar_search_background
                    clipChildren = false
                    topPadding = getIntDimen(R.dimen.dp_4)
                    bottomPadding = getIntDimen(R.dimen.dp_4)

                    rightIcon = imageView {
                        id = R.id.more
                        padding = getIntDimen(R.dimen.dp_8)
                        setBackgroundResource(getSelectableItemBorderless())
                        tintImage(R.color.blackColor54)
                        setImageDrawable(getCompatDrawable(R.drawable.ic_more_vert_black_24dp))
                    }.lparams(width = getIntDimen(R.dimen.dp_40), height = getIntDimen(R.dimen.dp_40)) {
                        alignParentRight()
                        centerVertically()
                    }

                    leftIcon = imageView {
                        id = R.id.action_icon
                        padding = getIntDimen(R.dimen.dp_8)
                        setBackgroundResource(getSelectableItemBorderless())
                        setImageDrawable(getCompatDrawable(R.drawable.ic_search_black_24dp))
                    }.lparams(width = getIntDimen(R.dimen.dp_40), height = getIntDimen(R.dimen.dp_40)) {
                        centerVertically()
                        leftMargin = getIntDimen(R.dimen.dp_8)
                        marginStart = getIntDimen(R.dimen.dp_8)
                    }

                    clearIcon = imageView {
                        id = R.id.clear_icon
                        padding = getIntDimen(R.dimen.dp_8)
                        setBackgroundResource(getSelectableItemBorderless())
                        tintImage(R.color.grey24Color)
                        visibility = View.GONE
                        setImageDrawable(getCompatDrawable(R.drawable.ic_close_black_24dp))
                    }.lparams(width = getIntDimen(R.dimen.dp_40), height = getIntDimen(R.dimen.dp_40)) {
                        centerVertically()
                        leftOf(R.id.more)
                        leftMargin = getIntDimen(R.dimen.dp_8)
                        marginStart = getIntDimen(R.dimen.dp_8)
                    }

                    tvTitle = textView {
                        id = R.id.tv_title
                        text = resources.getString(R.string.app_name)
                        textColor = getCompatColor(R.color.colorTextSecondary)
                        setTextSizeFromRes(R.dimen.dp_16)
                    }.lparams {
                        centerVertically()
                        leftMargin = getIntDimen(R.dimen.dp_16)
                        marginStart = getIntDimen(R.dimen.dp_16)
                        leftOf(R.id.more)
                        rightOf(R.id.action_icon)
                    }

                    searchEditText = editText {
                        id = R.id.search_view
                        backgroundColor = Color.TRANSPARENT
                        hint = resources.getString(R.string.search)
                        textColor = getCompatColor(R.color.colorTextPrimary)
                        setTextSizeFromRes(R.dimen.dp_16)
                        singleLine = true
                        imeOptions = EditorInfo.IME_ACTION_SEARCH
                        visibility = View.INVISIBLE
                        setOnEditorActionListener { v, actionId, event ->
                            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                                hideKeyboard()
                                return@setOnEditorActionListener true
                            }
                            false
                        }
                    }.lparams {
                        centerVertically()
                        leftMargin = getIntDimen(R.dimen.dp_16)
                        marginStart = getIntDimen(R.dimen.dp_16)
                        rightOf(R.id.action_icon)
                        leftOf(R.id.clear_icon)
                    }
                }.lparams(width = matchParent, height = wrapContent) {
                    val margin = getIntDimen(R.dimen.dp_8)
                    setMargins(margin, context.getStatusBarHeight(), margin, margin)
                }
            }.lparams(width = matchParent, height = wrapContent)
        }

        leftIcon.setOnClickListener {
            isSearchMode = !isSearchMode
            if (isSearchMode) {
                initSearchMode()
            } else {
                turnOffSearchMode()
            }
        }
        searchContainer.setOnClickListener {
            if (!isSearchMode) {
                isSearchMode = true
                initSearchMode()
            }
        }

        clearIcon.setOnClickListener {
            searchEditText.setText("")
        }
    }

    fun setTitle(text: CharSequence) {
        tvTitle.text = text
    }

    fun observeRightImageClick(): Observable<Any> {
        return RxView.clicks(rightIcon)
    }

    fun observeSearchInput(): Observable<CharSequence> {
        return RxTextView.textChanges(searchEditText).doOnNext { s ->
            if (s.isNullOrEmpty()) {
                clearIcon.visibility = View.GONE
            } else {
                clearIcon.visibility = View.VISIBLE
            }
        }
    }

    fun getPopupViewAnchor(): View {
        return rightIcon
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (measuredHeight != bgImageView.measuredHeight) {
            bgImageView.post {
                bgImageView.layoutParams.height = measuredHeight
                bgImageView.requestLayout()
            }
        }
    }

    private fun turnOffSearchMode() {
        searchEditText.setText("")
        tvTitle.visibility = View.VISIBLE
        searchEditText.visibility = View.INVISIBLE
        leftIcon.hideKeyboard()
        leftIcon.setImageDrawable(getCompatDrawable(R.drawable.ic_search_black_24dp))
    }

    private fun initSearchMode() {
        leftIcon.setImageDrawable(getCompatDrawable(R.drawable.ic_arrow_back))
        searchEditText.visibility = View.VISIBLE
        searchEditText.showSoftKeyboard()
        tvTitle.visibility = View.INVISIBLE
    }
}

