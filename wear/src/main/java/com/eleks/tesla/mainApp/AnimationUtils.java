package com.eleks.tesla.mainApp;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Ihor.Demedyuk on 06.03.2015.
 */
public final class AnimationUtils {

    private static final int START_ANIMATION_TIME = 67;
    private static final int MIDDLE_ANIMATION_TIME = 182;
    private static final int END_ANIMATION_TIME = 56;

    private static final float START_ANIMATION_OUT_SCALE_END = 0.71f;
    private static final float MIDDLE_ANIMATION_OUT_SCALE_START = 0.71f;
    private static final float MIDDLE_ANIMATION_OUT_SCALE_END = 1.19f;
    private static final float MIDDLE_ANIMATION_IN_ALPHA_START = 0f;
    private static final float MIDDLE_ANIMATION_IN_ALPHA_END = 0.45f;
    private static final float END_ANIMATION_IN_SCALE_START = 1.28f;
    private static final float END_ANIMATION_IN_SCALE_END = 1f;
    private static final float END_ANIMATION_IN_ALPHA_START = 0.45f;
    private static final float END_ANIMATION_IN_ALPHA_END = 1f;
    public static final int IMAGE_RESOURCE_UNDEFINED = -1;

    public static void performActionAnimation(View view, int viewSideSize) {
        performToggleAnimation(view, viewSideSize, IMAGE_RESOURCE_UNDEFINED);
    }

    private static int getImageSizeFromCoef(float coef, int viewSideSize) {
        return Math.round(viewSideSize * coef);
    }

    private static class ScaleAnimateListener implements ValueAnimator.AnimatorUpdateListener {
        private View mView;

        private ScaleAnimateListener(View view) {
            mView = view;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            setViewSize(mView, (int) animation.getAnimatedValue());
        }
    }

    private static class AlphaAnimateListener implements ValueAnimator.AnimatorUpdateListener {
        private View mView;

        private AlphaAnimateListener(View view) {
            mView = view;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mView.setAlpha((float) animation.getAnimatedValue());
        }
    }

    private static void setViewSize(View view, int size) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.width = size;
        params.height = size;
        view.setLayoutParams(params);
        view.requestLayout();
    }

    public static void performToggleAnimation(final View view, int viewSideSize, final int imageResource) {
        ValueAnimator startAnim = ValueAnimator.ofInt(viewSideSize, getImageSizeFromCoef(START_ANIMATION_OUT_SCALE_END, viewSideSize));
        startAnim.addUpdateListener(new ScaleAnimateListener(view));
        startAnim.setDuration(START_ANIMATION_TIME);

        AnimatorSet middleAnim = new AnimatorSet();
        ValueAnimator middleOutScale = ValueAnimator.ofInt(getImageSizeFromCoef(MIDDLE_ANIMATION_OUT_SCALE_START, viewSideSize),
                getImageSizeFromCoef(MIDDLE_ANIMATION_OUT_SCALE_END, viewSideSize));
        middleOutScale.addUpdateListener(new ScaleAnimateListener(view));
        ValueAnimator middleInAlpha = ValueAnimator.ofFloat(MIDDLE_ANIMATION_IN_ALPHA_START, MIDDLE_ANIMATION_IN_ALPHA_END);
        middleInAlpha.addUpdateListener(new AlphaAnimateListener(view));
        middleAnim.playTogether(middleOutScale, middleInAlpha);
        middleAnim.setDuration(MIDDLE_ANIMATION_TIME);

        if (IMAGE_RESOURCE_UNDEFINED != imageResource && view instanceof ImageView) {
            middleInAlpha.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    ((ImageView) view).setImageResource(imageResource);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        }

        AnimatorSet endAnim = new AnimatorSet();
        ValueAnimator endInScale = ValueAnimator.ofInt(getImageSizeFromCoef(END_ANIMATION_IN_SCALE_START, viewSideSize),
                getImageSizeFromCoef(END_ANIMATION_IN_SCALE_END, viewSideSize));
        endInScale.addUpdateListener(new ScaleAnimateListener(view));
        ValueAnimator endInAlpha = ValueAnimator.ofFloat(END_ANIMATION_IN_ALPHA_START, END_ANIMATION_IN_ALPHA_END);
        endInAlpha.addUpdateListener(new AlphaAnimateListener(view));
        endAnim.playTogether(endInScale, endInAlpha);
        endAnim.setDuration(END_ANIMATION_TIME);

        AnimatorSet mainAnim = new AnimatorSet();
        mainAnim.playSequentially(startAnim, middleAnim, endAnim);
        mainAnim.setInterpolator(new LinearInterpolator());
        mainAnim.start();
    }
}
