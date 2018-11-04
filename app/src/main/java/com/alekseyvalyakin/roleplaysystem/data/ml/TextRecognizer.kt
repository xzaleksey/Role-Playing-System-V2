package com.alekseyvalyakin.roleplaysystem.data.ml

import android.content.Context
import android.net.Uri
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import io.reactivex.Single
import io.reactivex.SingleEmitter
import java.io.File

class TextRecognizerImpl(
        private val context: Context
) : TextRecognizer {

    private val textRecognizer = FirebaseVision.getInstance().onDeviceTextRecognizer

    override fun recognizeFile(file: File): Single<String> {
        return Single.create { emitter ->
            try {
                FirebaseVisionImage.fromFilePath(context, Uri.fromFile(file)).run {
                    textRecognizer.processImage(this)
                            .addOnSuccessListener { text ->
                                emitter.onSuccess(text.text)
                            }
                            .addOnFailureListener { t ->
                                throwException(emitter, t)
                            }
                }
            } catch (t: Throwable) {
                throwException(emitter, t)
            }
        }
    }

    private fun throwException(emitter: SingleEmitter<String>, t: Throwable) {
        if (!emitter.isDisposed) {
            emitter.onError(t)
        }
    }
}

interface TextRecognizer {
    fun recognizeFile(file: File): Single<String>
}