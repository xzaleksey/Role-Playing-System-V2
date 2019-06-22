package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.races.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.races.UserGameRace
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.GameSettingsDefaultItemView
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.races.GameSettingsRacePresenter
import com.jakewharton.rxrelay2.Relay

class GameSettingsRacesViewHolder(
        private val gsView: GameSettingsDefaultItemView
) : RecyclerView.ViewHolder(gsView) {

    fun update(viewModel: GameSettingsRaceListViewModel, relay: Relay<GameSettingsRacePresenter.UiEvent>) {
        gsView.update(viewModel,
                View.OnClickListener {
                    relay.accept(
                            GameSettingsRacePresenter.UiEvent.ChangeRace(viewModel))
                },
                View.OnClickListener {
                    relay.accept(
                            GameSettingsRacePresenter.UiEvent.SelectRace(viewModel, adapterPosition))
                }, View.OnLongClickListener { _ ->
            if (viewModel.gameRace is UserGameRace) {
                MaterialDialog(gsView.context)
                        .title(R.string.delete)
                        .message(R.string.delete_race)
                        .positiveButton(android.R.string.ok, click = {
                            relay.accept(GameSettingsRacePresenter.UiEvent.DeleteRace(viewModel))
                        })
                        .negativeButton(android.R.string.cancel)
                        .show()
                return@OnLongClickListener true
            }
            return@OnLongClickListener false
        })
    }
}