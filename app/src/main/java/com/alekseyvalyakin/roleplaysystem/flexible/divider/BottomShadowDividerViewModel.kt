package com.alekseyvalyakin.roleplaysystem.flexible.divider

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem

data class BottomShadowDividerViewModel(
        val id: Int
) : AbstractFlexibleItem<DividerViewHolder>() {

    override fun createViewHolder(adapter: FlexibleAdapter<*>?, inflater: LayoutInflater?,
                                  parent: ViewGroup): DividerViewHolder {
        return DividerViewHolder(BottomFlexibleShadowView(parent.context))
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<*>?, holder: DividerViewHolder?, position: Int,
                                payloads: List<*>?) {
    }

    override fun getLayoutRes(): Int {
        return FlexibleLayoutTypes.BOTTOM_SHADOW_DIVIDER
    }
}
      