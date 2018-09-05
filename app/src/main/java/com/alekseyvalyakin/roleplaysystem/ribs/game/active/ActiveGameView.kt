package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import android.content.Context
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.widget.FrameLayout
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.model.ActiveGameViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import org.jetbrains.anko.*
import org.jetbrains.anko.design._CoordinatorLayout
import org.jetbrains.anko.design.bottomNavigationView

/**
 * Top level view for {@link ActiveGameBuilder.ActiveGameScope}.
 */
class ActiveGameView constructor(
        context: Context
) : _CoordinatorLayout(context), ActiveGamePresenter {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var container: FrameLayout

    private val relay = PublishRelay.create<ActiveGamePresenter.Event>()

    init {
        relativeLayout {
            bottomNavigationView = bottomNavigationView {
                backgroundColorResource = R.color.colorPrimary
                itemIconTintList = ContextCompat.getColorStateList(context, R.color.bottom_menu_icon_color)
                id = R.id.bottom_navigation_id
            }.lparams(width = matchParent, height = wrapContent) {
                alignParentBottom()
            }

            container = frameLayout {

            }.lparams(width = matchParent, height = matchParent) {
                above(bottomNavigationView)
            }
        }
        bottomNavigationView.setOnNavigationItemSelectedListener {
            relay.accept(ActiveGamePresenter.Event.Navigate(it.itemId))
            true
        }
    }

    override fun showModel(viewModel: ActiveGameViewModel) {
        bottomNavigationView.menu.clear()
        val bottomPanelMenu = viewModel.bottomPanelMenu
        bottomPanelMenu.items.forEach {
            val id = it.id.id
            bottomNavigationView.menu.add(Menu.NONE, id, 0, it.text).icon = it.imageHolder.getDrawable()
        }

        bottomNavigationView.selectedItemId = bottomPanelMenu.items[bottomPanelMenu.selectedIndex].id.id
    }

    override fun observeUiEvents(): Observable<ActiveGamePresenter.Event> {
        return relay
    }

    fun getContentContainer(): FrameLayout {
        return container
    }
}
