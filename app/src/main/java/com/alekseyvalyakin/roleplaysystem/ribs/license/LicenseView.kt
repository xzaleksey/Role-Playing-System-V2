package com.alekseyvalyakin.roleplaysystem.ribs.license

import android.content.Context
import android.text.Html
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getDoubleCommonDimen
import com.alekseyvalyakin.roleplaysystem.utils.getStatusBarHeight
import com.alekseyvalyakin.roleplaysystem.utils.getString
import org.jetbrains.anko._RelativeLayout
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.below
import org.jetbrains.anko.margin
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.scrollView
import org.jetbrains.anko.textView
import org.jetbrains.anko.view
import org.jetbrains.anko.wrapContent

class LicenseView constructor(context: Context) : _RelativeLayout(context), LicensePresenter {


    init {
        view {
            id = R.id.status_bar
            backgroundColorResource = R.color.colorPrimaryDark
        }.lparams(width = matchParent, height = getStatusBarHeight())

        scrollView {
            textView {
                text = Html.fromHtml(getString(R.string.dnd_license))
            }.lparams(matchParent, wrapContent) {
                margin = getDoubleCommonDimen()
            }
        }.lparams(matchParent, matchParent) {
            below(R.id.status_bar)
        }

    }
}
