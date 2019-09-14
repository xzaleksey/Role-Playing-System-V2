package com.alekseyvalyakin.roleplaysystem.ribs.dialogs

import android.app.Activity
import com.afollestad.materialdialogs.MaterialDialog
import org.jetbrains.anko.toast

class DialogDelegateImpl(
        private val activity: Activity
) : DialogDelegate {

    override fun showDialog(
            title: String,
            text: String,
            positiveButtonText: String?,
            negativeButtonText: String?,
            positiveAction: () -> Unit,
            negativeAction: () -> Unit) {
        MaterialDialog(activity)
                .title(text = title)
                .message(text = text)
                .positiveButton(
                        text = positiveButtonText ?: activity.getString(android.R.string.ok),
                        click = { positiveAction() }
                )
                .negativeButton(
                        text = negativeButtonText ?: activity.getString(android.R.string.cancel),
                        click = { negativeAction() })
                .show { }

    }

    override fun showToast(title: String) {
        activity.toast(title)
    }

}