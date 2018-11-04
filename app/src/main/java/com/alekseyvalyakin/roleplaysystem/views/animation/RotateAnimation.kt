package com.alekseyvalyakin.roleplaysystem.views.animation

import android.animation.ObjectAnimator
import android.view.View
import com.daimajia.androidanimations.library.BaseViewAnimator

class RotateRightAnimator : BaseViewAnimator() {
    public override fun prepare(target: View) {
        animatorAgent.playTogether(
                ObjectAnimator.ofFloat(target, "rotation", 0f, 45f)
        )
    }
}

class RotateLeftAnimator : BaseViewAnimator() {
    public override fun prepare(target: View) {
        animatorAgent.playTogether(
                ObjectAnimator.ofFloat(target, "rotation", 45f, 0f)
        )
    }
}