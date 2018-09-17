package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.fullsizephoto

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.photoView
import io.reactivex.disposables.Disposables
import org.jetbrains.anko.*
import uk.co.senab.photoview.PhotoView
import uk.co.senab.photoview.PhotoViewAttacher

/**
 * Top level view for {@link FullSizePhotoBuilder.FullSizePhotoScope}.
 */
class FullSizePhotoView(context: Context) : _RelativeLayout(context), FullSizePhotoInteractor.FullSizePhotoPresenter {

    private val photoContainer: PhotoView
    private val photoViewAttacher: PhotoViewAttacher
    private var disposable = Disposables.disposed()
    private var progress: ProgressBar
    private var btnRetry: Button
    private lateinit var fullSizePhotoViewModel: FullSizePhotoViewModel

    init {
        backgroundColor = Color.BLACK
        photoContainer = photoView {
            id = R.id.photo_container
        }.lparams(width = matchParent, height = matchParent) {
            alignParentLeft()
            alignParentTop()
        }

        photoViewAttacher = PhotoViewAttacher(photoContainer)

        btnRetry = themedButton(R.style.BrandButtonStyle) {
            id = R.id.btn_retry
            text = resources.getString(R.string.retry_error_button)
            visibility = View.GONE

        }.lparams {
            centerInParent()
        }
        progress = progressBar {
            id = R.id.progress_circular
            indeterminateDrawable.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY)
            isIndeterminate = true
            visibility = View.GONE
        }.lparams {
            centerInParent()
        }
        btnRetry.setOnClickListener { startLoadingPhoto(fullSizePhotoViewModel) }
    }

    override fun onDetachedFromWindow() {
        disposable.dispose()
        super.onDetachedFromWindow()
    }

    override fun updateViewModel(fullSizePhotoViewModel: FullSizePhotoViewModel) {
        this.fullSizePhotoViewModel = fullSizePhotoViewModel
        disposable.dispose()
        startLoadingPhoto(fullSizePhotoViewModel)
    }

    private fun startLoadingPhoto(fullSizePhotoViewModel: FullSizePhotoViewModel) {
        btnRetry.visibility = View.GONE
        disposable = fullSizePhotoViewModel.imageProvider.observeImage().subscribe({
            photoContainer.setImageBitmap(it.getBitmap())
            photoViewAttacher.update()
            progress.visibility = View.GONE
        }) {
            progress.visibility = View.GONE
            btnRetry.visibility = View.VISIBLE
        }
    }
}