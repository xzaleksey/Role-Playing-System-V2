package com.alekseyvalyakin.roleplaysystem.base.image

import java.io.File

data class ImageData(
        val url: String,
        val localFile: File? = null
)