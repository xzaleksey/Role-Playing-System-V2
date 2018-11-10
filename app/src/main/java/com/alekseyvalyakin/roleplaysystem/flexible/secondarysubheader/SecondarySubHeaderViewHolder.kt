package com.alekseyvalyakin.roleplaysystem.flexible.secondarysubheader

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R

class SecondarySubHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvTitle: TextView = itemView.findViewById(R.id.text)
    private val divider: View = itemView.findViewById(R.id.divider)
    private val topDivider: View = itemView.findViewById(R.id.top_divider)

    fun bind(secondarySubHeaderViewModel: SecondarySubHeaderViewModel) {
        tvTitle.text = secondarySubHeaderViewModel.title
        (tvTitle.layoutParams as ViewGroup.MarginLayoutParams).leftMargin = secondarySubHeaderViewModel.marginLeft

        if (secondarySubHeaderViewModel.color == 0) {
            tvTitle.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorTextSecondary))
        } else {
            tvTitle.setTextColor(secondarySubHeaderViewModel.color)
        }
        if (secondarySubHeaderViewModel.isDrawBottomDivider) {
            divider.visibility = View.VISIBLE
        } else {
            divider.visibility = View.GONE
        }

        if (secondarySubHeaderViewModel.isDrawTopDivider) {
            topDivider.visibility = View.VISIBLE
        } else {
            topDivider.visibility = View.GONE
        }
    }
}
      