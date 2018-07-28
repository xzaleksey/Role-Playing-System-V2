package com.alekseyvalyakin.roleplaysystem.flexible

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R

class SubHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvTitle: TextView = itemView.findViewById(R.id.text)
    private val divider: View= itemView.findViewById(R.id.divider)
    private val topDivider: View = itemView.findViewById(R.id.top_divider)

    fun bind(subHeaderViewModel: SubHeaderViewModel) {
        tvTitle.text = subHeaderViewModel.title
        tvTitle.setPadding(subHeaderViewModel.paddingLeft, tvTitle.paddingTop, tvTitle.paddingRight, tvTitle.paddingBottom)

        if (subHeaderViewModel.color == 0) {
            tvTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary))
        } else {
            tvTitle.setTextColor(subHeaderViewModel.color)
        }
        if (subHeaderViewModel.isDrawBottomDivider) {
            divider.visibility = View.VISIBLE
        } else {
            divider.visibility = View.GONE
        }

        if (subHeaderViewModel.isDrawTopDivider) {
            topDivider.visibility = View.VISIBLE
        } else {
            topDivider.visibility = View.GONE
        }
    }
}
      