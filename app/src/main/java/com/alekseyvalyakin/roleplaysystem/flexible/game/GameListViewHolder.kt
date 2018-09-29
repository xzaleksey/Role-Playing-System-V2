package com.alekseyvalyakin.roleplaysystem.flexible.game

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder

class GameListViewHolder(itemView: View, mAdapter: FlexibleAdapter<*>) : FlexibleViewHolder(itemView, mAdapter) {

    private var tvPrimaryLine: TextView = itemView.findViewById(R.id.primary_line)
    private var tvSecondaryLine: TextView = itemView.findViewById(R.id.secondary_line)
    private var icon: ImageView = itemView.findViewById(R.id.icon)
    private var iconRight: ImageView = itemView.findViewById(R.id.iv_right)

    fun bind(gameListViewModel: GameListViewModel) {
        tvPrimaryLine.text = gameListViewModel.title
        tvSecondaryLine.text = gameListViewModel.description
        if (gameListViewModel.isShowMasterIcon) {
            icon.visibility = View.VISIBLE
        } else {
            icon.visibility = View.GONE
        }
        if (gameListViewModel.isGameLocked) {
            iconRight.setImageResource(R.drawable.ic_lock_outline_black_24px)
        } else {
            iconRight.setImageResource(R.drawable.ic_arrow_right)
        }
    }
}