package com.eleks.tesla.mainApp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WatchViewStub;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.eleks.tesla.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends CommunicationActivity implements View.OnClickListener {

    public static final int ANIMATION_DURATION = 500;
    public static final float MAX_SCALE = 1f;
    public static final float MIN_SCALE = 0f;
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final int MAX_BUTTONS_COUNT = 4;

    private View[] mButtons;  //  right top left bottom
    protected ImageView[] mImages;  //  right top left bottom
    protected CircledImageView[] mCircles;  //  right top left bottom
    protected ImageView[] mRipples;
    private MarginSetter[] mMarginSetter;

    private int mScreenWidth;
    private int mButtonSideSize;
    private int mButtonMargin;
    protected int mImageSideSize;

    private AnimatorSet mOpenAnimatorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                initFields();
                setUpUI();
                setWidgetsPosition(stub.getMeasuredWidth());
                playStartAnimation();
            }
        });
    }

    protected void setUpUI() {
        mCircles[0].setCircleColor(getResources().getColor(R.color.category_charge));
        mRipples[0].setImageResource(R.drawable.fake_ripple_charge);
        mImages[0].setImageResource(R.drawable.category_charge);

        mCircles[1].setCircleColor(getResources().getColor(R.color.category_location));
        mRipples[1].setImageResource(R.drawable.fake_ripple_location);
        mImages[1].setImageResource(R.drawable.category_location);

        mCircles[2].setCircleColor(getResources().getColor(R.color.category_condition));
        mRipples[2].setImageResource(R.drawable.fake_ripple_condition);
        mImages[2].setImageResource(R.drawable.category_condition);

        mCircles[3].setCircleColor(getResources().getColor(R.color.category_drive));
        mRipples[3].setImageResource(R.drawable.fake_ripple_drive);
        mImages[3].setImageResource(R.drawable.category_drive);
    }

    protected void initFields() {
        mRipples = new ImageView[MAX_BUTTONS_COUNT];
        mRipples[0] = (ImageView) findViewById(R.id.ripple_right);
        mRipples[1] = (ImageView) findViewById(R.id.ripple_top);
        mRipples[2] = (ImageView) findViewById(R.id.ripple_left);
        mRipples[3] = (ImageView) findViewById(R.id.ripple_bottom);

        mButtons = new View[MAX_BUTTONS_COUNT];
        mButtons[0] = findViewById(R.id.segment_right);
        mButtons[0].setOnClickListener(this);
        mButtons[0].setOnTouchListener(new RippleTouchListener(mRipples[0]));

        mButtons[1] = findViewById(R.id.segment_top);
        mButtons[1].setOnClickListener(this);
        mButtons[1].setOnTouchListener(new RippleTouchListener(mRipples[1]));

        mButtons[2] = findViewById(R.id.segment_left);
        mButtons[2].setOnClickListener(this);
        mButtons[2].setOnTouchListener(new RippleTouchListener(mRipples[2]));

        mButtons[3] = findViewById(R.id.segment_bottom);
        mButtons[3].setOnClickListener(this);
        mButtons[3].setOnTouchListener(new RippleTouchListener(mRipples[3]));

        mImages = new ImageView[MAX_BUTTONS_COUNT];
        mImages[0] = (ImageView) findViewById(R.id.right_image_1);
        mImages[1] = (ImageView) findViewById(R.id.top_image_1);
        mImages[2] = (ImageView) findViewById(R.id.left_image_1);
        mImages[3] = (ImageView) findViewById(R.id.bottom_image_1);

        mCircles = new CircledImageView[MAX_BUTTONS_COUNT];
        mCircles[0] = (CircledImageView) findViewById(R.id.right_image_background);
        mCircles[1] = (CircledImageView) findViewById(R.id.top_image_background);
        mCircles[2] = (CircledImageView) findViewById(R.id.left_image_background);
        mCircles[3] = (CircledImageView) findViewById(R.id.bottom_image_background);

        mMarginSetter = new MarginSetter[]{
                new MarginSetter() {
                    @Override
                    public void setMargin(RelativeLayout.LayoutParams params, int value) {
                        params.setMargins(0, 0, value, 0);
                    }
                },
                new MarginSetter() {
                    @Override
                    public void setMargin(RelativeLayout.LayoutParams params, int value) {
                        params.setMargins(0, value, 0, 0);
                    }
                },
                new MarginSetter() {
                    @Override
                    public void setMargin(RelativeLayout.LayoutParams params, int value) {
                        params.setMargins(value, 0, 0, 0);
                    }
                },
                new MarginSetter() {
                    @Override
                    public void setMargin(RelativeLayout.LayoutParams params, int value) {
                        params.setMargins(0, 0, 0, value);
                    }
                }
        };
    }

    protected void setWidgetsPosition(int width) {
        mScreenWidth = width;

        mButtonSideSize = width / 4;
        mImageSideSize = width / 6;
        mButtonMargin = (int) (((float) width) / 32 * 3);

        for (int i = 0; i < MAX_BUTTONS_COUNT; i++) {
            if (mCircles[i] != null) {
                mCircles[i].setCircleRadius(mButtonSideSize / 2);
            }

            if (mImages[i] != null) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mImages[i].getLayoutParams();
                params.width = mImageSideSize;
                params.height = mImageSideSize;
                mImages[i].setLayoutParams(params);
            }

            if (mButtons[i] != null) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mButtons[i].getLayoutParams();
                params.width = mButtonSideSize;
                params.height = mButtonSideSize;
                mMarginSetter[i].setMargin(params, mButtonMargin);
                mButtons[i].setLayoutParams(params);
                mRipples[i].setLayoutParams(params);

                mButtons[i].setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mOpenAnimatorSet != null && !mOpenAnimatorSet.isRunning()) {
            mOpenAnimatorSet.start();
            restoreViews();
        }
    }

    @Override
    public void onClick(View v) {

        AnimatorListenerAdapter animatorListenerAdapter = null;

        switch (v.getId()) {
            case R.id.segment_top:
                animatorListenerAdapter = new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        startActivity(new Intent(MainActivity.this, LocationActivity.class));
                        overridePendingTransition(R.animator.zoom_enetr, R.animator.empty_animation);

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideViews();
                            }
                        }, 1000);
                    }
                };
                playCloseAnimationToLocation(animatorListenerAdapter);
                break;
            case R.id.segment_left:
                animatorListenerAdapter = new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        openSetValueActivity(SetValueActivity.SELECTOR_TYPE_CONDITIONING);
                    }
                };
                playCloseAnimation(animatorListenerAdapter);
                break;
            case R.id.segment_right:
                animatorListenerAdapter = new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        openSetValueActivity(SetValueActivity.SELECTOR_TYPE_CHARGING);
                    }
                };
                playCloseAnimation(animatorListenerAdapter);
                break;
            case R.id.segment_bottom:
                animatorListenerAdapter = new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        Intent intent = new Intent(MainActivity.this, DriveActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideViews();
                            }
                        }, 1000);
                    }
                };

                playCloseAnimationToDrive(animatorListenerAdapter);
                break;
        }
    }

    protected void playStartAnimation() {
        List<Animator> animations = new ArrayList<>();

        ValueAnimator startSlideAnim = ValueAnimator.ofFloat(0, 1);
        startSlideAnim.setDuration(ANIMATION_DURATION);
        startSlideAnim.setInterpolator(new OvershootInterpolator());

        startSlideAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float animatedValue = (float) animation.getAnimatedValue();
                float reversAnimatorValue = 1f - animatedValue;
                float maxCurve = mScreenWidth / 5;

                if (mButtons[0] != null) {
                    mButtons[0].setTranslationX(-reversAnimatorValue * ((mScreenWidth - mButtonSideSize) / 2 - mButtonMargin));
                    mButtons[0].setTranslationY((float) (-maxCurve * Math.sin(Math.PI * animatedValue)));
                }

                if (mButtons[1] != null) {
                    mButtons[1].setTranslationX((float) (-maxCurve * Math.sin(Math.PI * animatedValue)));
                    mButtons[1].setTranslationY(reversAnimatorValue * ((mScreenWidth - mButtonSideSize) / 2 - mButtonMargin));
                }

                if (mButtons[2] != null) {
                    mButtons[2].setTranslationX(reversAnimatorValue * ((mScreenWidth - mButtonSideSize) / 2 - mButtonMargin));
                    mButtons[2].setTranslationY((float) (maxCurve * Math.sin(Math.PI * animatedValue)));
                }

                if (mButtons[3] != null) {
                    mButtons[3].setTranslationX((float) (maxCurve * Math.sin(Math.PI * animatedValue)));
                    mButtons[3].setTranslationY(-reversAnimatorValue * ((mScreenWidth - mButtonSideSize) / 2 - mButtonMargin));
                }

            }
        });
        animations.add(startSlideAnim);

        for (int i = 0; i < MAX_BUTTONS_COUNT; i++) {
            if (mButtons[i] == null) {
                continue;
            }

            ObjectAnimator scaleDownXImage = ObjectAnimator.ofFloat(mButtons[i], "scaleX", MIN_SCALE, MAX_SCALE);
            scaleDownXImage.setDuration(ANIMATION_DURATION);
            scaleDownXImage.setInterpolator(new OvershootInterpolator());
            animations.add(scaleDownXImage);

            ObjectAnimator scaleDownYImage = ObjectAnimator.ofFloat(mButtons[i], "scaleY", MIN_SCALE, MAX_SCALE);
            scaleDownYImage.setDuration(ANIMATION_DURATION);
            scaleDownYImage.setInterpolator(new OvershootInterpolator());
            animations.add(scaleDownYImage);

        }

        mOpenAnimatorSet = new AnimatorSet();
        mOpenAnimatorSet.playTogether(animations);
        mOpenAnimatorSet.start();
    }

    private void restoreViews() {
        if (mButtons[3] != null) {
            mImages[3].setAlpha(1f);
            mImages[3].setScaleX(1f);
            mImages[3].setScaleY(1f);
            mCircles[3].setAlpha(1f);
        }

        if (mButtons[1] != null) {
            mImages[1].setAlpha(1f);
            mImages[1].setScaleX(1f);
            mImages[1].setScaleY(1f);
            mCircles[1].setAlpha(1f);
        }
    }

    private void hideViews() {
        if (mButtons[3] != null) {
            mImages[3].setAlpha(0f);
            mCircles[3].setAlpha(0f);
        }

        if (mButtons[1] != null) {
            mImages[1].setAlpha(0f);
            mCircles[1].setAlpha(0f);
        }
    }

    protected void playCloseAnimation(AnimatorListenerAdapter animatorListenerAdapter) {
        List<Animator> animations = new ArrayList<>();

        ValueAnimator closeSlideAnim = createCloseSlideAnimator(mButtons);
        animations.add(closeSlideAnim);

        addScaleDownAnimations(animations, mButtons);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animations);
        animatorSet.addListener(animatorListenerAdapter);
        animatorSet.start();
    }

    private void addScaleDownAnimations(List<Animator> animations, View[] buttons) {
        for (int i = 0; i < MAX_BUTTONS_COUNT; i++) {
            if (buttons[i] == null) {
                continue;
            }
            addScaleDownAnimation(animations, buttons[i]);
        }
    }

    private void addScaleDownAnimation(List<Animator> animations, View button) {
        ObjectAnimator scaleDownXImage = ObjectAnimator.ofFloat(button, "scaleX", MAX_SCALE, MIN_SCALE);
        scaleDownXImage.setDuration(ANIMATION_DURATION);
        scaleDownXImage.setInterpolator(new AccelerateInterpolator());
        animations.add(scaleDownXImage);

        ObjectAnimator scaleDownYImage = ObjectAnimator.ofFloat(button, "scaleY", MAX_SCALE, MIN_SCALE);
        scaleDownYImage.setDuration(ANIMATION_DURATION);
        scaleDownYImage.setInterpolator(new AccelerateInterpolator());
        animations.add(scaleDownYImage);
    }

    private ValueAnimator createCloseSlideAnimator(final View[] buttons) {
        ValueAnimator startSlideAnim = ValueAnimator.ofFloat(0, 1);
        startSlideAnim.setDuration(ANIMATION_DURATION);
        startSlideAnim.setInterpolator(new AccelerateInterpolator());

        startSlideAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float animatedValue = (float) animation.getAnimatedValue();
                float maxCurve = mScreenWidth / 5;

                if (buttons[0] != null) {
                    buttons[0].setTranslationX(-animatedValue * ((mScreenWidth - mButtonSideSize) / 2 - mButtonMargin));
                    buttons[0].setTranslationY((float) (maxCurve * Math.sin(Math.PI * animatedValue)));
                }

                if (buttons[1] != null) {
                    buttons[1].setTranslationX((float) (maxCurve * Math.sin(Math.PI * animatedValue)));
                    buttons[1].setTranslationY(animatedValue * ((mScreenWidth - mButtonSideSize) / 2 - mButtonMargin));
                }

                if (buttons[2] != null) {
                    buttons[2].setTranslationX(animatedValue * ((mScreenWidth - mButtonSideSize) / 2 - mButtonMargin));
                    buttons[2].setTranslationY((float) (-maxCurve * Math.sin(Math.PI * animatedValue)));
                }

                if (buttons[3] != null) {
                    buttons[3].setTranslationX((float) (-maxCurve * Math.sin(Math.PI * animatedValue)));
                    buttons[3].setTranslationY(-animatedValue * ((mScreenWidth - mButtonSideSize) / 2 - mButtonMargin));
                }

            }
        });
        return startSlideAnim;
    }

    protected void playCloseAnimationToDrive(AnimatorListenerAdapter animatorListenerAdapter) {
        List<Animator> animations = new ArrayList<>();

        ValueAnimator closeSlideAnim = createCloseSlideAnimator(mButtons);
        animations.add(closeSlideAnim);

        View[] buttonsToAnimate = Arrays.copyOf(mButtons, mButtons.length);
        buttonsToAnimate[3] = null;
        addScaleDownAnimations(animations, buttonsToAnimate);

        ObjectAnimator circleAlphaAnim = ObjectAnimator.ofFloat(mCircles[3], "alpha", 1f, 0f);
        circleAlphaAnim.setDuration(ANIMATION_DURATION);
        circleAlphaAnim.setInterpolator(new AccelerateInterpolator());
        animations.add(circleAlphaAnim);

        ObjectAnimator imageAlphaAnim = ObjectAnimator.ofFloat(mImages[3], "alpha", 1f, 0.5f);
        imageAlphaAnim.setDuration(ANIMATION_DURATION);
        imageAlphaAnim.setInterpolator(new AccelerateInterpolator());
        animations.add(imageAlphaAnim);

        ObjectAnimator scaleDownXImage = ObjectAnimator.ofFloat(mImages[3], "scaleX", MAX_SCALE, 0.8f);
        scaleDownXImage.setDuration(ANIMATION_DURATION);
        scaleDownXImage.setInterpolator(new AccelerateInterpolator());
        animations.add(scaleDownXImage);

        ObjectAnimator scaleDownYImage = ObjectAnimator.ofFloat(mImages[3], "scaleY", MAX_SCALE, 0.8f);
        scaleDownYImage.setDuration(ANIMATION_DURATION);
        scaleDownYImage.setInterpolator(new AccelerateInterpolator());
        animations.add(scaleDownYImage);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animations);
        animatorSet.addListener(animatorListenerAdapter);
        animatorSet.start();
    }

    protected void playCloseAnimationToLocation(AnimatorListenerAdapter animatorListenerAdapter) {
        List<Animator> animations = new ArrayList<>();

        ValueAnimator closeSlideAnim = createCloseSlideAnimator(mButtons);
        animations.add(closeSlideAnim);

        View[] buttonsToAnimate = Arrays.copyOf(mButtons, mButtons.length);
        buttonsToAnimate[1] = null;
        addScaleDownAnimations(animations, buttonsToAnimate);

        ObjectAnimator circleAlphaAnim = ObjectAnimator.ofFloat(mCircles[1], "alpha", 1f, 0f);
        circleAlphaAnim.setDuration(ANIMATION_DURATION);
        circleAlphaAnim.setInterpolator(new AccelerateInterpolator());
        animations.add(circleAlphaAnim);

        ObjectAnimator imageAlphaAnim = ObjectAnimator.ofFloat(mImages[1], "alpha", 1f, 0f);
        imageAlphaAnim.setDuration(ANIMATION_DURATION);
        imageAlphaAnim.setInterpolator(new AccelerateInterpolator());
        animations.add(imageAlphaAnim);

        ObjectAnimator scaleDownXImage = ObjectAnimator.ofFloat(mImages[1], "scaleX", MAX_SCALE, 0.8f);
        scaleDownXImage.setDuration(ANIMATION_DURATION);
        scaleDownXImage.setInterpolator(new AccelerateInterpolator());
        animations.add(scaleDownXImage);

        ObjectAnimator scaleDownYImage = ObjectAnimator.ofFloat(mImages[1], "scaleY", MAX_SCALE, 0.8f);
        scaleDownYImage.setDuration(ANIMATION_DURATION);
        scaleDownYImage.setInterpolator(new AccelerateInterpolator());
        animations.add(scaleDownYImage);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animations);
        animatorSet.addListener(animatorListenerAdapter);
        animatorSet.start();
    }

    private void openSetValueActivity(int actionSelector) {
        Intent intent = new Intent(this, SetValueActivity.class);
        intent.putExtra(SetValueActivity.SELECTOR_TYPE, actionSelector);
        startActivity(intent);
        overridePendingTransition(R.animator.zoom_enetr, R.animator.empty_animation);
    }

    protected void startRippleAnimation(final View view) {
        AnimatorSet rippleAnim = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 10f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 10f);
        rippleAnim.playTogether(scaleX, scaleY);
        rippleAnim.setInterpolator(new AccelerateInterpolator());
        rippleAnim.setDuration(Math.round(ANIMATION_DURATION * 0.7f));
        rippleAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setScaleX(1f);
                view.setScaleY(1f);
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        rippleAnim.start();
    }

    protected class RippleTouchListener implements View.OnTouchListener {
        private View view;

        public RippleTouchListener(View view) {
            this.view = view;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                startRippleAnimation(view);
            }
            return false;
        }
    }

    interface MarginSetter {
        public void setMargin(RelativeLayout.LayoutParams params, int value);
    }
}
