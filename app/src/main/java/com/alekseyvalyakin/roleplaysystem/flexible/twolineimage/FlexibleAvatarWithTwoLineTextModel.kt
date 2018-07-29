package com.alekseyvalyakin.roleplaysystem.flexible.twolineimage

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.base.image.ImageProvider
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils.EMPTY_STRING
import com.alekseyvalyakin.roleplaysystem.views.TwoLineImageView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import java.io.Serializable

class FlexibleAvatarWithTwoLineTextModel(
        val primaryText: String = EMPTY_STRING,
        val secondaryText: String = EMPTY_STRING,
        val imageProvider: ImageProvider,
        val id: String,
        val isShowArrowRight: Boolean = false
) : AbstractFlexibleItem<FlexibleAvatarTwoLineTextViewHolder>() {

    override fun createViewHolder(adapter: FlexibleAdapter<*>, inflater: LayoutInflater, parent: ViewGroup): FlexibleAvatarTwoLineTextViewHolder {
        return FlexibleAvatarTwoLineTextViewHolder(TwoLineImageView(parent.context), adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<*>?, holder: FlexibleAvatarTwoLineTextViewHolder?, position: Int, payloads: List<*>?) {
        holder!!.bind(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FlexibleAvatarWithTwoLineTextModel

        if (primaryText != other.primaryText) return false
        if (secondaryText != other.secondaryText) return false
        if (imageProvider.getId() != other.imageProvider.getId()) return false
        if (id != other.id) return false
        if (isShowArrowRight != other.isShowArrowRight) return false

        return true
    }

    override fun hashCode(): Int {
        var result = primaryText.hashCode()
        result = 31 * result + secondaryText.hashCode()
        result = 31 * result + imageProvider.getId().hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + isShowArrowRight.hashCode()
        return result
    }

}
      