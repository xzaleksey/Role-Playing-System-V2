package com.alekseyvalyakin.roleplaysystem.flexible.game

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder

class GameListViewHolder(itemView: View, mAdapter: FlexibleAdapter<*>) : FlexibleViewHolder(itemView, mAdapter) {

    private var tvPrimaryLine: TextView? = null
    private var tvSecondaryLine: TextView? = null
    private var icon: ImageView? = null
    private var iconRight: ImageView? = null

    fun bind(gameListViewModel: GameListViewModel) {
        tvPrimaryLine!!.text = gameListViewModel.title
        tvSecondaryLine!!.text = gameListViewModel.description
        if (gameListViewModel.isShowMasterIcon) {
            icon!!.visibility = View.VISIBLE
        } else {
            icon!!.visibility = View.GONE
        }
        if (gameListViewModel.isGameLocked) {
            iconRight!!.setImageResource(R.drawable.ic_lock_outline_black_24px)
        } else {
            iconRight!!.setImageResource(R.drawable.ic_keyboard_arrow_right)
        }
    }
}