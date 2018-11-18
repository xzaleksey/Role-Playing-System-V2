package com.alekseyvalyakin.roleplaysystem.utils.animation;

import android.animation.ObjectAnimator;
import android.view.View;

import com.daimajia.androidanimations.library.BaseViewAnimator;

public class PulseAnimatorX1_5 extends BaseViewAnimator {
    @Override
    public void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target, "scaleY", 1, 1.5f, 1),
                ObjectAnimator.ofFloat(target, "scaleX", 1, 1.5f, 1)
        );
    }
}