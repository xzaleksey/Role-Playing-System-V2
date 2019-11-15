package com.alekseyvalyakin.roleplaysystem.ribs.dialogs

interface DialogDelegate {
    fun showDialog(
            title: String,
            text: String,
            positiveButtonText: String? = null,
            negativeButtonText: String? = null,
            positiveAction: () -> Unit = {},
            negativeAction: () -> Unit = {}
    )

    fun showToast(title: String)
}