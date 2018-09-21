package com.alekseyvalyakin.roleplaysystem.views.backdrop

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.*
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

open class DefaultFrontView(context: Context) : _RelativeLayout(context) {

    protected lateinit var rightIcon: ImageView
    protected var recyclerView: RecyclerView
    protected lateinit var tvTitle: TextView
    protected var topContainer: ViewGroup
    protected lateinit var flexibleAdapter: FlexibleAdapter<IFlexible<*>>

    init {
        backgroundColorResource = R.color.colorWhite
        topPadding = getIntDimen(R.dimen.dp_12)

        topContainer = relativeLayout {
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

            tvTitle = textView {
                id = R.id.tv_title
                textSizeDimen = R.dimen.dp_16
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

        recyclerView = recyclerView {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            layoutManager = LinearLayoutManager(context)
        }.lparams(width = matchParent, height = matchParent) {
            below(R.id.top_container)
        }
    }

    fun update(model: Model) {
        val headerModel = model.headerModel
        flexibleAdapter.updateDataSet(model.items, true)
        if (headerModel != null) {
            topContainer.visibility = View.VISIBLE
            tvTitle.text = headerModel.title
            rightIcon.setOnClickListener { headerModel.clickListener() }
            rightIcon.setImageDrawable(headerModel.icon)
        } else {
            topContainer.visibility = View.GONE
        }
    }

    fun setAdapter(adapter: FlexibleAdapter<IFlexible<*>>) {
        flexibleAdapter = adapter
        recyclerView.adapter = adapter
    }

    data class Model(
            val headerModel: HeaderModel?,
            val items: List<IFlexible<*>>
    )

    data class HeaderModel(
            val title: String,
            val icon: Drawable?,
            val clickListener: () -> Unit
    )
}