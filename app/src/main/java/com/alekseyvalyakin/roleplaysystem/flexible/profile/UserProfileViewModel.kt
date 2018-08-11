package com.alekseyvalyakin.roleplaysystem.flexible.profile

import com.alekseyvalyakin.roleplaysystem.base.image.ImageProvider
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes
import com.alekseyvalyakin.roleplaysystem.flexible.twolineimage.FlexibleAvatarWithTwoLineTextModel
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils.EMPTY_STRING

class UserProfileViewModel(
        primaryText: String = EMPTY_STRING,
        secondaryText: String = EMPTY_STRING,
        imageProvider: ImageProvider,
        id: String,
        isShowArrowRight: Boolean = false
) : FlexibleAvatarWithTwoLineTextModel(primaryText,
        secondaryText,
        imageProvider,
        id,
        isShowArrowRight) {


    override fun getLayoutRes(): Int {
        return FlexibleLayoutTypes.USER_PROFILE
    }

}
      