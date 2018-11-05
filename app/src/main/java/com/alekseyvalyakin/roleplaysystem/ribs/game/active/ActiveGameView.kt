package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import android.animation.LayoutTransition
import android.content.Context
import android.content.res.ColorStateList
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.View
import android.widget.FrameLayout
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.base.model.NavigationId
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.model.ActiveGameViewModel
import com.alekseyvalyakin.roleplaysystem.utils.getCompatColor
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
    private var fullScreenContainer: FrameLayout

    private val relay = PublishRelay.create<ActiveGamePresenter.Event>()

    init {
        relativeLayout {
            bottomNavigationView = bottomNavigationView {
                backgroundColorResource = R.color.colorPrimary
                itemTextColor = ColorStateList.valueOf(getCompatColor(R.color.colorWhite))
                itemIconTintList = ContextCompat.getColorStateList(context, R.color.bottom_menu_icon_color)
                id = R.id.bottom_navigation_id
            }.lparams(width = matchParent, height = wrapContent) {
                alignParentBottom()
            }
            layoutTransition = LayoutTransition()

            container = frameLayout {

            }.lparams(width = matchParent, height = matchParent) {
                above(bottomNavigationView)
            }
        }
        bottomNavigationView.setOnNavigationItemSelectedListener {
            relay.accept(ActiveGamePresenter.Event.Navigate(it.itemId))
            true
        }

        fullScreenContainer = frameLayout {

        }.lparams(width = matchParent, height = matchParent) {
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

    override fun updateNavigationId(navigationId: NavigationId) {
        bottomNavigationView.selectedItemId = navigationId.id
    }

    override fun observeUiEvents(): Observable<ActiveGamePresenter.Event> {
        return relay
    }

    fun getContentContainer(): FrameLayout {
        return container
    }

    fun getFullScreenContainer(): FrameLayout {
        return fullScreenContainer
    }

    override fun hideBottomBar() {
        bottomNavigationView.visibility = View.GONE
    }

    override fun showBottomBar() {
        bottomNavigationView.visibility = View.VISIBLE
    }

}
