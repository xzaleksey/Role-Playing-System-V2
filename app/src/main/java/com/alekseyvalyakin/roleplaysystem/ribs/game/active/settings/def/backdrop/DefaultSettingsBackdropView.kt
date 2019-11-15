package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.backdrop

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.customListAdapter
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.IconViewModel
import com.alekseyvalyakin.roleplaysystem.views.backdrop.BackDropView
import com.alekseyvalyakin.roleplaysystem.views.backdrop.BaseViewContainer
import com.alekseyvalyakin.roleplaysystem.views.backdrop.back.BackViewContainer
import com.alekseyvalyakin.roleplaysystem.views.backdrop.back.DefaultBackView
import com.alekseyvalyakin.roleplaysystem.views.backdrop.front.DefaultFrontView
import com.alekseyvalyakin.roleplaysystem.views.backdrop.front.FrontViewContainer
import com.alekseyvalyakin.roleplaysystem.views.toolbar.CustomToolbarView
import eu.davidea.flexibleadapter.FlexibleAdapter
import org.jetbrains.anko.backgroundColorResource

/**
 * Top level view for {@link GameSettingsStatBuilder.GameSettingsStatScope}.
 */
abstract class DefaultSettingsBackdropView<T : CustomToolbarView,
        B : DefaultBackView,
        F : DefaultFrontView> constructor(
        context: Context,
        topContainer: BaseViewContainer<T>,
        backContainer: BackViewContainer<B>,
        frontViewContainer: FrontViewContainer<F>
) : BackDropView<T, B, F>(context,
        topContainer,
        backContainer,
        frontViewContainer
), DefaultBackDropView {

    init {
        backgroundColorResource = R.color.colorPrimary
    }

    override fun updateStartEndScrollPositions(adapterPosition: Int) {
        frontViewContainer.view.updateStartEndPositions(adapterPosition)
    }

    override fun scrollToPosition(position: Int) {
        frontViewContainer.view.scrollToAdapterPosition(position)
    }

    override fun chooseIcon(callback: (IconViewModel) -> Unit, items: List<IconViewModel>) {
        val materialDialog = MaterialDialog(context)
        materialDialog
                .title(R.string.choose_icon)
                .customListAdapter(FlexibleAdapter(items).apply {
                    this.mItemClickListener = FlexibleAdapter.OnItemClickListener {
                        callback(items[it])
                        materialDialog.dismiss()
                        true
                    }
                })
                .show()
    }
}
