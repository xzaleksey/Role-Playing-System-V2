package com.alekseyvalyakin.roleplaysystem.utils

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import androidx.core.content.FileProvider
import java.io.File


fun Context.shareFile(file: File) {
    val intent = Intent(Intent.ACTION_SEND)
    val packageName = applicationContext.packageName
    val uri = FileProvider.getUriForFile(this, packageName, file)
    intent.type = "*/*"
    grantUriPermission(packageName, uri, FLAG_GRANT_READ_URI_PERMISSION)
    intent.putExtra(Intent.EXTRA_STREAM, uri)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    startActivity(Intent.createChooser(intent, "Share File"))
}