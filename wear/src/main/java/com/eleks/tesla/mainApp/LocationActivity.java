package com.eleks.tesla.mainApp;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.eleks.tesla.teslalib.ApiPathConstants;
import com.eleks.tesla.R;
import com.eleks.tesla.events.LocationMapLoadedEvent;
import com.eleks.tesla.events.ToHandHoldRequestEvent;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by maryan.melnychuk on 05.03.2015.
 */
public class LocationActivity extends Activity {
    public static final int ANIMATION_DURATION = 800;
    private ImageView mMapImageView;
    private ImageView mPinImageView;
    protected int mImageSideSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mMapImageView = (ImageView) findViewById(R.id.map_image);
        mPinImageView = (ImageView) findViewById(R.id.pin_image);

        EventBus.getDefault().post(new ToHandHoldRequestEvent(ApiPathConstants.WEAR_GET_LOCATION_MAP));
        EventBus.getDefault().registerSticky(this);

        final ViewGroup root = (ViewGroup) findViewById(R.id.root);
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setUpPinImage(root.getWidth());
                root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void setUpPinImage(int width) {
        mImageSideSize = (int) (width / 6 * 0.8f);

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mPinImageView.getLayoutParams();
        lp.width = mImageSideSize;
        lp.height = mImageSideSize;
        mPinImageView.setLayoutParams(lp);

        AnimatorSet animatorSet = new AnimatorSet();
        List<Animator> animations = new ArrayList<>(2);

        ValueAnimator pinSlideUpAnim = ValueAnimator.ofFloat(0, 1);
        pinSlideUpAnim.setDuration(ANIMATION_DURATION);
        pinSlideUpAnim.setInterpolator(new OvershootInterpolator());

        pinSlideUpAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                mPinImageView.setTranslationY(-mImageSideSize / 2 * animatedValue);
            }
        });
        animations.add(pinSlideUpAnim);

        ObjectAnimator imageAlphaAnim = ObjectAnimator.ofFloat(mPinImageView, "alpha", 0f, 1f);
        imageAlphaAnim.setDuration(ANIMATION_DURATION / 2);
        imageAlphaAnim.setInterpolator(new AccelerateInterpolator());
        animations.add(imageAlphaAnim);

        animatorSet.playTogether(animations);
        animatorSet.start();
    }

    public void onEventMainThread(LocationMapLoadedEvent event) {
        mMapImageView.setImageBitmap(event.getMapImage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
