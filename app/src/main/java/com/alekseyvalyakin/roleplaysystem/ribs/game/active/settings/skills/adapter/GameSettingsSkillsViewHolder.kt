package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills.UserGameSkill
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills.GameSettingsSkillsPresenter
import com.jakewharton.rxrelay2.Relay

class GameSettingsSkillsViewHolder(
        private val gsView: GameSettingsSkillItemView
) : RecyclerView.ViewHolder(gsView) {

    fun update(viewModel: GameSettingsSkillsListViewModel, relay: Relay<GameSettingsSkillsPresenter.UiEvent>) {
        gsView.update(viewModel,
                View.OnClickListener {
                    relay.accept(
                            GameSettingsSkillsPresenter.UiEvent.ChangeSkill(viewModel))
                },
                View.OnClickListener {
                    relay.accept(
                            GameSettingsSkillsPresenter.UiEvent.SelectSkill(viewModel, adapterPosition))
                }, View.OnLongClickListener { _ ->
            if (viewModel.gameSkill is UserGameSkill) {
                MaterialDialog(gsView.context)
                        .title(R.string.delete)
                        .message(R.string.delete_race)
                        .positiveButton(android.R.string.ok, click = {
                            relay.accept(GameSettingsSkillsPresenter.UiEvent.DeleteSkill(viewModel))
                        })
                        .negativeButton(android.R.string.cancel)
                        .show()
                return@OnLongClickListener true
            }
            return@OnLongClickListener false
        })
    }
}