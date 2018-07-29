package com.alekseyvalyakin.roleplaysystem.flexible.twolineimage

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils.EMPTY_STRING
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables

class FlexibleAvatarTwoLineTextViewHolder(itemView: View, mAdapter: FlexibleAdapter<*>) : FlexibleViewHolder(itemView, mAdapter) {
    private val ivAvatar: ImageView = itemView.findViewById(R.id.avatar)
    private val ivRightArrow: ImageView = itemView.findViewById(R.id.arrow_right)
    private val tvPrimaryLine: TextView = itemView.findViewById(R.id.primary_line)
    private val tvSecondaryLine: TextView = itemView.findViewById(R.id.secondary_line)
    private var disposable: Disposable = Disposables.disposed()
    private var lastImageId: String = EMPTY_STRING

    fun bind(avatarWithTwoLineTextModel: FlexibleAvatarWithTwoLineTextModel) {
        ivRightArrow.visibility = if (avatarWithTwoLineTextModel.isShowArrowRight) View.VISIBLE else View.GONE
        tvPrimaryLine.text = avatarWithTwoLineTextModel.primaryText
        tvSecondaryLine.text = avatarWithTwoLineTextModel.secondaryText
        val imageProvider = avatarWithTwoLineTextModel.imageProvider

        if (imageProvider.getId() != lastImageId) {
            disposable.dispose()
            disposable = imageProvider.observeImage()
                    .subscribeWithErrorLogging { i ->
                        ivAvatar.setImageBitmap(i.getBitmap())
                    }
        }
    }
}
      