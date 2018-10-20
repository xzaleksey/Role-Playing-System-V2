package com.alekseyvalyakin.roleplaysystem.data.donate

import android.content.Context
import android.content.Intent
import android.net.Uri
import timber.log.Timber

class DonateInteractorImpl(
        private val context: Context
) : DonateInteractor {


    override fun donate() {
        val intent = Intent(Intent.ACTION_VIEW)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setData(parseLinkAndFixIfNeeded(YANDEX_LINK))

        if (hasHandler(context, intent)) {
            context.startActivity(intent)
        } else {
            Timber.e("No browser")
        }
    }

    private fun parseLinkAndFixIfNeeded(url: String): Uri {
        var uri = Uri.parse(url)
        if (uri.scheme.isNullOrEmpty()) {
            uri = uri.buildUpon()
                    .scheme(HTTP_SCHEME)
                    .build()
        }

        return uri
    }

    private fun hasHandler(context: Context, intent: Intent): Boolean {
        val handlers = context.packageManager.queryIntentActivities(intent, 0)
        return !handlers.isEmpty()
    }

    companion object {
        const val YANDEX_LINK = "https://money.yandex.ru/to/410017785296165/150"
        const val PAYPAL_LINK = "https://paypal.me/rpgassistant/150"
        const val HTTP_SCHEME = "http"
    }
}

interface DonateInteractor {
    fun donate()
}