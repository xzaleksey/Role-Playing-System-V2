package com.alekseyvalyakin.roleplaysystem.ribs.features.adapter

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getDoubleCommonDimen
import com.alekseyvalyakin.roleplaysystem.utils.getSelectableItemBackGround
import org.jetbrains.anko.*

class FeatureItemView(context: Context) : _LinearLayout(context) {
    private var tvTitle: TextView
    private var tvDescription: TextView
    private var tvVotesCount: TextView
    private var btnVote: Button

    init {
        orientation = VERTICAL
        backgroundResource = getSelectableItemBackGround()
        bottomPadding = dimen(R.dimen.dp_12)
        topPadding = dimen(R.dimen.dp_12)
        leftPadding = getDoubleCommonDimen()
        rightPadding = getDoubleCommonDimen()

        tvTitle = textView {

        }.lparams(matchParent, wrapContent)

        tvDescription = textView {

        }.lparams(matchParent, wrapContent)

        tvVotesCount = textView {

        }.lparams(matchParent, wrapContent)

        btnVote = button {
            visibility = View.GONE
            text = "Vote"
        }
    }

    fun update(featureFlexibleViewModel: FeatureFlexibleViewModel, onClickListener: OnClickListener) {
        tvTitle.text = featureFlexibleViewModel.getName()
        tvDescription.text = featureFlexibleViewModel.getDescription()
        tvVotesCount.text = featureFlexibleViewModel.getVotesCount().toString()
        btnVote.setOnClickListener(onClickListener)
        btnVote.visibility = if (featureFlexibleViewModel.canVote()) View.VISIBLE else View.GONE
    }
}