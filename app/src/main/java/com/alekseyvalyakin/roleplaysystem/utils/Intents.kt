package com.alekseyvalyakin.roleplaysystem.utils

import android.content.Context
import android.content.Intent
import android.support.v4.content.FileProvider
import com.alekseyvalyakin.roleplaysystem.R
import java.io.File


fun Context.openFolder(file: File) {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    val packageName = applicationContext.packageName
    val uri = FileProvider.getUriForFile(this, "$packageName.provider", file)
    intent.setDataAndType(uri, "*/*")
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    startActivity(Intent.createChooser(intent, getString(R.string.open_folder)))
}