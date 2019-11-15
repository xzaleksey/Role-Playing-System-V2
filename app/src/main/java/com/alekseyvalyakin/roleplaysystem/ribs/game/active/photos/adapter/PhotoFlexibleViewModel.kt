package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alekseyvalyakin.roleplaysystem.base.image.ImageData
import com.alekseyvalyakin.roleplaysystem.base.image.ImageProvider
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import org.jetbrains.anko.wrapContent

data class PhotoFlexibleViewModel(
        val id: String,
        val imageProvider: ImageProvider,
        val name: String,
        val canChange: Boolean,
        val visible: Boolean,
        val size: Int,
        val imageData: ImageData
) : AbstractFlexibleItem<PhotoInGameItemViewHolder>() {

    override fun createViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>, inflater: LayoutInflater, parent: ViewGroup): PhotoInGameItemViewHolder {
        val photoInGameItemView = PhotoInGameItemView(parent.context)
        photoInGameItemView.layoutParams = RecyclerView.LayoutParams(wrapContent, wrapContent)
        return PhotoInGameItemViewHolder(photoInGameItemView, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>?, viewHolder: PhotoInGameItemViewHolder, position: Int, payloads: MutableList<Any?>?) {
        viewHolder.bind(this, (adapter as PhotoInGameAdapter).relay)
    }

    override fun getLayoutRes(): Int {
        return FlexibleLayoutTypes.PHOTO_IN_GAME
    }
}