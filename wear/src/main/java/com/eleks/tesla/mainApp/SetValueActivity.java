package com.eleks.tesla.mainApp;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.wearable.view.CircledImageView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eleks.tesla.teslalib.ApiPathConstants;
import com.eleks.tesla.R;
import com.eleks.tesla.events.ChargeStateLoadedEvent;
import com.eleks.tesla.events.ClimateStateLoadedEvent;
import com.eleks.tesla.events.ToHandHoldRequestEvent;
import com.eleks.tesla.mainApp.widget.RadialPicker;

import de.greenrobot.event.EventBus;

/**
 * Created by Ihor.Demedyuk on 24.02.2015.
 */
public class SetValueActivity extends CommunicationActivity implements RadialPicker.ValueChangeListener, View.OnClickListener {

    public static final String SELECTOR_TYPE = "selector_type";
    public static final int SELECTOR_TYPE_CONDITIONING = 1;
    public static final int SELECTOR_TYPE_CHARGING = 2;

    private ValueActivityState mState;

    private int mType;
    private int mValue;
    private String mValueFormatter;

    private TextView mValueTextView;
    private RadialPicker mRadialPicker;
    private View mToggleButton;
    private ImageView mToggleImage;
    private CircledImageView mCircledImageView;

    private int mButtonSideSize;
    private int mButtonMargin;
    protected int mImageSideSize;
    private boolean mIsToggleOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_value);

        mValueTextView = (TextView) findViewById(R.id.value_text);
        mRadialPicker = (RadialPicker) findViewById(R.id.radial_picker);

        mToggleButton = findViewById(R.id.segment_top);
        mToggleButton.setOnClickListener(this);
        mToggleImage = (ImageView) findViewById(R.id.top_image_1);
        mCircledImageView = (CircledImageView) findViewById(R.id.top_image_background);

        final RelativeLayout root = (RelativeLayout) findViewById(R.id.root);
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setToggleButtonPosition(root.getWidth());
                root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        mType = getIntent().getIntExtra(SELECTOR_TYPE, SELECTOR_TYPE_CONDITIONING);
        if (mType == SELECTOR_TYPE_CHARGING) {
            mState = new ChargeState();
            mValue = 75;
        } else {
            mState = new TermoState();
            mValue = 20;
        }

        mCircledImageView.setCircleColor(getResources().getColor(mState.getToggleColorId()));
        mToggleImage.setImageResource(mIsToggleOn ? mState.getOnToggleImageResource()
                : mState.getOffToggleImageResource());
        mRadialPicker.setMinValue(mState.getMinValue());
        mRadialPicker.setMaxValue(mState.getMaxValue());
        mRadialPicker.setColors(mState.getGradientColors(this));
        mValueFormatter = mState.getTextFormatter();

        mRadialPicker.setValuechangeListener(this);
        mRadialPicker.setValue(mValue);
        onValueChanged(mValue);
    }

    protected void setToggleButtonPosition(int width) {
        mButtonSideSize = width / 4;
        mImageSideSize = width / 6;
        mButtonMargin = (int) (((float) width) / 32 * 3);

        if (mCircledImageView != null) {
            mCircledImageView.setCircleRadius(mButtonSideSize / 2);
        }

        if (mToggleImage != null) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mToggleImage.getLayoutParams();
            params.width = mImageSideSize;
            params.height = mImageSideSize;
            mToggleImage.setLayoutParams(params);
        }

        if (mToggleButton != null) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mToggleButton.getLayoutParams();
            params.width = mButtonSideSize;
            params.height = mButtonSideSize;
            params.setMargins(0, mButtonMargin, 0, 0);
            mToggleButton.setLayoutParams(params);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onValueChanged(int value) {
        mValueTextView.setText(String.format(mValueFormatter, value));
    }

    public void onEvent(ChargeStateLoadedEvent event) {
        if (mType != SELECTOR_TYPE_CHARGING) {
            return;
        }
        mValue = event.getChargeState().getMaxRangeChargeCounter();
        onValueChanged(mValue);
    }

    public void onEvent(ClimateStateLoadedEvent event) {
        if (mType == SELECTOR_TYPE_CONDITIONING) {
            mValue = (int) event.getClimateState().getDriverTempSetting();
        } else if (mType == SELECTOR_TYPE_CONDITIONING) {
            mValue = (int) event.getClimateState().getPassengerTempSetting();
        } else {
            return;
        }
        onValueChanged(mValue);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.segment_top) {
            performToggleClick();
        }
    }

    private void performToggleClick() {
        mIsToggleOn = !mIsToggleOn;

        Object event;
        if (mType == SELECTOR_TYPE_CHARGING) {
            AnimationUtils.performToggleAnimation(mToggleImage, mImageSideSize,
                    mIsToggleOn ? R.mipmap.charge_on : R.mipmap.charge_off);
            event = new ToHandHoldRequestEvent(mIsToggleOn ?
                    ApiPathConstants.WEAR_ACTION_CHARGING_START
                    : ApiPathConstants.WEAR_ACTION_CHARGING_STOP);
        } else {
            AnimationUtils.performToggleAnimation(mToggleImage, mImageSideSize,
                    mIsToggleOn ? R.mipmap.hvac_on : R.mipmap.hvac_off);
            event = new ToHandHoldRequestEvent(mIsToggleOn ?
                    ApiPathConstants.WEAR_ACTION_AUTO_CONDITIONING_START
                    : ApiPathConstants.WEAR_ACTION_AUTO_CONDITIONING_STOP);
        }

        EventBus.getDefault().post(event);
    }

    private interface ValueActivityState {
        public int getMaxValue();

        public int getMinValue();

        public int getToggleColorId();

        public int[] getGradientColors(Context ctx);

        public int getOnToggleImageResource();

        public int getOffToggleImageResource();

        public String getTextFormatter();
    }

    private static class TermoState implements ValueActivityState {
        private static final String TEMP_VALUE_FORMATER = "%d °C";
        private static final int MAX_VALUE = 30;
        private static final int MIX_VALUE = 16;

        @Override
        public int getMaxValue() {
            return MAX_VALUE;
        }

        @Override
        public int getMinValue() {
            return MIX_VALUE;
        }

        @Override
        public int getToggleColorId() {
            return R.color.category_condition;
        }

        @Override
        public int[] getGradientColors(Context ctx) {
            Resources res = ctx.getResources();
            int blueColor = res.getColor(R.color.gradient_blue);
            int greenColor = res.getColor(R.color.gradient_green);
            int redColor = res.getColor(R.color.gradient_red);
            return new int[]{blueColor, greenColor, redColor};
        }

        @Override
        public int getOnToggleImageResource() {
            return R.mipmap.hvac_on;
        }

        @Override
        public int getOffToggleImageResource() {
            return R.mipmap.hvac_off;
        }

        @Override
        public String getTextFormatter() {
            return TEMP_VALUE_FORMATER;
        }
    }

    private static class ChargeState implements ValueActivityState {
        private static final String BATTARY_CHARGE_FORMATTER = "%d%%";
        private static final int MAX_VALUE = 100;
        private static final int MIN_VALUE = 50;

        @Override
        public int getMaxValue() {
            return MAX_VALUE;
        }

        @Override
        public int getMinValue() {
            return MIN_VALUE;
        }

        @Override
        public int getToggleColorId() {
            return R.color.category_charge;
        }

        @Override
        public int[] getGradientColors(Context ctx) {
            Resources res = ctx.getResources();
            int blueColor = res.getColor(R.color.gradient_blue);
            int greenColor = res.getColor(R.color.gradient_green);
            int redColor = res.getColor(R.color.gradient_red);
            return new int[]{redColor, blueColor, greenColor};
        }

        @Override
        public int getOnToggleImageResource() {
            return R.mipmap.charge_on;
        }

        @Override
        public int getOffToggleImageResource() {
            return R.mipmap.charge_off;
        }

        @Override
        public String getTextFormatter() {
            return BATTARY_CHARGE_FORMATTER;
        }
    }
}
