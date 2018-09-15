package com.alekseyvalyakin.roleplaysystem.utils.image

import android.app.Activity
import com.alekseyvalyakin.roleplaysystem.app.MainActivity
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.jakewharton.rxrelay2.PublishRelay
import com.kbeanie.multipicker.api.ImagePicker
import com.kbeanie.multipicker.api.Picker
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback
import com.kbeanie.multipicker.api.entity.ChosenImage
import com.uber.rib.core.lifecycle.ActivityCallbackEvent
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable


class LocalImageProviderImpl(
        val activity: MainActivity
) : LocalImageProvider {

    private val callbacks = activity.callbacks().share()!!
    private var imagePicker = ImagePicker(activity)
    private val relay = PublishRelay.create<ImagesResult>()
    private val internalCacheLocation = 200
    private val imagePickerCallback = object : ImagePickerCallback {
        override fun onImagesChosen(images: MutableList<ChosenImage>) {
            relay.accept(ImagesResult.Success(images))
        }

        override fun onError(error: String) {
            relay.accept(ImagesResult.Error(error))
        }
    }

    init {
        imagePicker.setImagePickerCallback(imagePickerCallback)
        imagePicker.shouldGenerateMetadata(false)
        imagePicker.shouldGenerateThumbnails(false)
        imagePicker.setCacheLocation(internalCacheLocation)
    }

    override fun subscribe(): Disposable {
        return callbacks
                .filter { it.type == ActivityCallbackEvent.Type.ACTIVITY_RESULT }
                .cast(ActivityCallbackEvent.ActivityResult::class.java)
                .filter { it.resultCode == Activity.RESULT_OK }
                .subscribeWithErrorLogging { activityCallbackEvent ->
                    if (activityCallbackEvent.requestCode == Picker.PICK_IMAGE_DEVICE) {
                        imagePicker.submit(activityCallbackEvent.data)
                    }
                }
    }

    override fun pickImage() {
        imagePicker.pickImage()
    }

    override fun observeImage(): Flowable<ImagesResult> {
        return relay.toFlowable(BackpressureStrategy.LATEST)
    }

}

sealed class ImagesResult {
    class Success(val images: MutableList<ChosenImage>) : ImagesResult()

    class Error(val error: String) : ImagesResult()
}

interface LocalImageProvider {
    fun pickImage()
    fun observeImage(): Flowable<ImagesResult>
    fun subscribe(): Disposable
}