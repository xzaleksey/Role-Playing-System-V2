package com.alekseyvalyakin.roleplaysystem.views.recyclerview

import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import com.alekseyvalyakin.roleplaysystem.utils.checkFabShow

class HideFablListener(private val fab: FloatingActionButton) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dy > 0) {
            // Scroll Down
            if (fab.isShown) {
                fab.hide()
            }
        } else if (dy < 0) {
            // Scroll Up
            if (!fab.isShown) {
                fab.show()
            }
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        recyclerView.checkFabShow(fab)
        super.onScrollStateChanged(recyclerView, newState)
    }
}
      