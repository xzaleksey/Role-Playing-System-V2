package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import android.content.Context
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.ContextCompat
import android.view.Menu
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.model.ActiveGameViewModel
import org.jetbrains.anko.*
import org.jetbrains.anko.design._CoordinatorLayout
import org.jetbrains.anko.design.bottomNavigationView

/**
 * Top level view for {@link ActiveGameBuilder.ActiveGameScope}.
 */
class ActiveGameView constructor(
        context: Context
) : _CoordinatorLayout(context), ActiveGameInteractor.ActiveGamePresenter {

    private lateinit var bottomNavigationView: BottomNavigationView

    init {
        relativeLayout {
            bottomNavigationView = bottomNavigationView {
                backgroundColorResource = R.color.colorPrimary
                itemIconTintList = ContextCompat.getColorStateList(context, R.color.bottom_menu_icon_color)
            }.lparams(width = matchParent, height = wrapContent) {
                alignParentBottom()
            }
        }
    }

    override fun showModel(viewModel: ActiveGameViewModel) {
        bottomNavigationView.menu.clear()
        val bottomPanelMenu = viewModel.bottomPanelMenu
        bottomPanelMenu.items.forEach {
            bottomNavigationView.menu.add(Menu.NONE, it.id, 0, it.text).icon = it.imageHolder.getDrawable()
        }
        bottomNavigationView.selectedItemId = bottomPanelMenu.items[bottomPanelMenu.selectedIndex].id
    }
}
