package com.eleks.tesla.mainApp.fragments;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.eleks.tesla.teslalib.models.ChargeState;
import com.eleks.tesla.R;
import com.eleks.tesla.events.ChargeStateLoadedEvent;
import com.eleks.tesla.events.ClimateStateLoadedEvent;
import com.eleks.tesla.events.ToHandHoldRequestEvent;
import com.eleks.tesla.events.VehicleStateLoadedEvent;
import com.eleks.tesla.mainApp.SetValueActivity;

import de.greenrobot.event.EventBus;

import static com.eleks.tesla.teslalib.ApiPathConstants.*;

/**
 * Created by maryan.melnychuk on 18.02.2015.
 */
public class ActionTeslaFragment extends BaseTeslaFragment implements View.OnClickListener {
    private static final int START_ANIMATION_TIME = 135;
    private static final float START_ANIMATION_OUT_SCALE_START = 1;
    private static final float START_ANIMATION_OUT_SCALE_END = 0.71f;
    private static final int MIDDLE_ANIMATION_TIME = 365;
    private static final float MIDDLE_ANIMATION_OUT_SCALE_START = 0.71f;
    private static final float MIDDLE_ANIMATION_OUT_SCALE_END = 1.19f;
    private static final float MIDDLE_ANIMATION_IN_SCALE_START = 0.68f;
    private static final float MIDDLE_ANIMATION_IN_SCALE_END = 1.28f;
    private static final float MIDDLE_ANIMATION_OUT_ALPHA_START = 1f;
    private static final float MIDDLE_ANIMATION_OUT_ALPHA_END = 0f;
    private static final float MIDDLE_ANIMATION_IN_ALPHA_START = 0f;
    private static final float MIDDLE_ANIMATION_IN_ALPHA_END = 0.45f;
    private static final int END_ANIMATION_TIME = 112;
    private static final float END_ANIMATION_IN_SCALE_START = 1.28f;
    private static final float END_ANIMATION_IN_SCALE_END = 1f;
    private static final float END_ANIMATION_IN_ALPHA_START = 0.45f;
    private static final float END_ANIMATION_IN_ALPHA_END = 1f;

    private int mActionId;

    private ImageView mActionImage1, mActionImage2;
    private TextView mActionLabel1, mActionLabel2, mActionButtonText;

    private int mNormalImageSize = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
//        if (args != null) {
//            mActionId = args.getInt(TeslaAppAdapter.ARG_FRAGMENT_ID, TeslaAppAdapter.SCREENS_HORN);
//        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_action, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActionImage1 = (ImageView) view.findViewById(R.id.action_image_1);
        mActionImage2 = (ImageView) view.findViewById(R.id.action_image_2);
        mActionLabel1 = (TextView) view.findViewById(R.id.action_label_1);
        mActionLabel2 = (TextView) view.findViewById(R.id.action_label_2);
        mActionButtonText = (TextView) view.findViewById(R.id.action_button_text);
        view.setOnClickListener(this);
        setViews();
    }

    private void setViews() {
        switch (mActionId) {
//            case TeslaAppAdapter.SCREENS_HORN:
//                setHornViews();
//                break;
//            case TeslaAppAdapter.SCREENS_DOOR_LOCK:
//                setDoorViews();
//                break;
//            case TeslaAppAdapter.SCREENS_FLASHLIGHT:
//                setFlashlightViews();
//                break;
//            case TeslaAppAdapter.SCREENS_CHARGING_START:
//                setChargingViews();
//                break;
//            case TeslaAppAdapter.SCREENS_AUTO_CONDITIONING:
//                setAutoConditioningViews();
//                break;
//            case TeslaAppAdapter.SCREENS_SET_TEMP_DRIVER:
//                setSetDriverTempViews();
//                break;
//            case TeslaAppAdapter.SCREENS_SET_TEMP_PASSENGER:
//                setSetPassengerTempViews();
//                break;
//            case TeslaAppAdapter.SCREENS_SET_MAX_CHARGING:
//                setSetMaxChargingViews();
//                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (mActionId) {
//            case TeslaAppAdapter.SCREENS_HORN:
//                performHornClick();
//                break;
//            case TeslaAppAdapter.SCREENS_DOOR_LOCK:
//                performDoorClick();
//                break;
//            case TeslaAppAdapter.SCREENS_FLASHLIGHT:
//                performFlashlightClick();
//                break;
//            case TeslaAppAdapter.SCREENS_CHARGING_START:
//                performChargingClick();
//                break;
//            case TeslaAppAdapter.SCREENS_AUTO_CONDITIONING:
//                performAutoConditioningClick();
//                break;
//            case TeslaAppAdapter.SCREENS_SET_TEMP_DRIVER:
//            case TeslaAppAdapter.SCREENS_SET_TEMP_PASSENGER:
//            case TeslaAppAdapter.SCREENS_SET_MAX_CHARGING:
//                openSetValueActivity();
//                break;
        }
    }

    private void performSwitchAnimation(boolean inOrder) {
        mNormalImageSize = mActionImage1.getHeight();
        if (inOrder) {
            performSwitchAnimation(mActionImage1, mActionImage2, mActionLabel1, mActionLabel2);
        } else {
            performSwitchAnimation(mActionImage2, mActionImage1, mActionLabel2, mActionLabel1);
        }
    }

    private void performSwitchAnimation(View viewOut, View viewIn, View textOut, View textIn) {
        ValueAnimator startAnim = ValueAnimator.ofInt(mNormalImageSize, getSizeFromCoef(START_ANIMATION_OUT_SCALE_END));
        startAnim.addUpdateListener(new ScaleAnimateListener(viewOut));
        startAnim.setDuration(START_ANIMATION_TIME);

        AnimatorSet middleAnim = new AnimatorSet();
        ValueAnimator middleOutScale = ValueAnimator.ofInt(getSizeFromCoef(MIDDLE_ANIMATION_OUT_SCALE_START), getSizeFromCoef(MIDDLE_ANIMATION_OUT_SCALE_END));
        middleOutScale.addUpdateListener(new ScaleAnimateListener(viewOut));
        ValueAnimator middleInScale = ValueAnimator.ofInt(getSizeFromCoef(MIDDLE_ANIMATION_IN_SCALE_START), getSizeFromCoef(MIDDLE_ANIMATION_IN_SCALE_END));
        middleInScale.addUpdateListener(new ScaleAnimateListener(viewIn));
        ValueAnimator middleOutAlpha = ValueAnimator.ofFloat(MIDDLE_ANIMATION_OUT_ALPHA_START, MIDDLE_ANIMATION_OUT_ALPHA_END);
        middleOutAlpha.addUpdateListener(new AlphaAnimateListener(viewOut));
        ValueAnimator middleInAlpha = ValueAnimator.ofFloat(MIDDLE_ANIMATION_IN_ALPHA_START, MIDDLE_ANIMATION_IN_ALPHA_END);
        middleInAlpha.addUpdateListener(new AlphaAnimateListener(viewIn));
        ValueAnimator middleOutTextAlpha = ValueAnimator.ofFloat(1f, 0f);
        middleOutTextAlpha.addUpdateListener(new AlphaAnimateListener(textOut));
        ValueAnimator middleInTextAlpha = ValueAnimator.ofFloat(0f, 1f);
        middleInTextAlpha.addUpdateListener(new AlphaAnimateListener(textIn));
        middleAnim.playTogether(middleOutScale, middleInScale, middleOutAlpha, middleInAlpha, middleOutTextAlpha, middleInTextAlpha);
        middleAnim.setDuration(MIDDLE_ANIMATION_TIME);

        AnimatorSet endAnim = new AnimatorSet();
        ValueAnimator endInScale = ValueAnimator.ofInt(getSizeFromCoef(END_ANIMATION_IN_SCALE_START), getSizeFromCoef(END_ANIMATION_IN_SCALE_END));
        endInScale.addUpdateListener(new ScaleAnimateListener(viewIn));
        ValueAnimator endInAlpha = ValueAnimator.ofFloat(END_ANIMATION_IN_ALPHA_START, END_ANIMATION_IN_ALPHA_END);
        endInAlpha.addUpdateListener(new AlphaAnimateListener(viewIn));
        endAnim.playTogether(endInScale, endInAlpha);
        endAnim.setDuration(END_ANIMATION_TIME);

        AnimatorSet mainAnim = new AnimatorSet();
        mainAnim.playSequentially(startAnim, middleAnim, endAnim);
        mainAnim.setInterpolator(new LinearInterpolator());
        mainAnim.start();
    }

    private void performActionAnimation(View view) {
        ValueAnimator startAnim = ValueAnimator.ofInt(mNormalImageSize, getSizeFromCoef(START_ANIMATION_OUT_SCALE_END));
        startAnim.addUpdateListener(new ScaleAnimateListener(view));
        startAnim.setDuration(START_ANIMATION_TIME);

        AnimatorSet middleAnim = new AnimatorSet();
        ValueAnimator middleOutScale = ValueAnimator.ofInt(getSizeFromCoef(MIDDLE_ANIMATION_OUT_SCALE_START), getSizeFromCoef(MIDDLE_ANIMATION_OUT_SCALE_END));
        middleOutScale.addUpdateListener(new ScaleAnimateListener(view));
        ValueAnimator middleInAlpha = ValueAnimator.ofFloat(MIDDLE_ANIMATION_IN_ALPHA_START, MIDDLE_ANIMATION_IN_ALPHA_END);
        middleInAlpha.addUpdateListener(new AlphaAnimateListener(view));
        middleAnim.playTogether(middleOutScale, middleInAlpha);
        middleAnim.setDuration(MIDDLE_ANIMATION_TIME);

        AnimatorSet endAnim = new AnimatorSet();
        ValueAnimator endInScale = ValueAnimator.ofInt(getSizeFromCoef(END_ANIMATION_IN_SCALE_START), getSizeFromCoef(END_ANIMATION_IN_SCALE_END));
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

    private int getSizeFromCoef(float coef) {
        return Math.round(mNormalImageSize * coef);
    }

    private void setViewSize(View view, int size) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.width = size;
        params.height = size;
        view.setLayoutParams(params);
        view.requestLayout();
    }

    private class ScaleAnimateListener implements ValueAnimator.AnimatorUpdateListener {
        private View mView;

        private ScaleAnimateListener(View view) {
            mView = view;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            setViewSize(mView, (int) animation.getAnimatedValue());
        }
    }

    private class AlphaAnimateListener implements ValueAnimator.AnimatorUpdateListener {
        private View mView;

        private AlphaAnimateListener(View view) {
            mView = view;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            mView.setAlpha((float) animation.getAnimatedValue());
        }
    }

    private void openSetValueActivity() {
        performActionAnimation(mActionButtonText);
        Intent intent = new Intent(getActivity(), SetValueActivity.class);
        intent.putExtra(SetValueActivity.SELECTOR_TYPE, mActionId);
        startActivity(intent);
    }

    private void performAutoConditioningClick() {
        ClimateStateLoadedEvent event = EventBus.getDefault().getStickyEvent(ClimateStateLoadedEvent.class);
        if (event != null) {
            if (event.getClimateState().isIsAutoConditioningOn()) {
                EventBus.getDefault().post(new ToHandHoldRequestEvent(WEAR_ACTION_AUTO_CONDITIONING_STOP));
                event.getClimateState().setIsAutoConditioningOn(false);
                performSwitchAnimation(true);
            } else {
                EventBus.getDefault().post(new ToHandHoldRequestEvent(WEAR_ACTION_AUTO_CONDITIONING_START));
                event.getClimateState().setIsAutoConditioningOn(true);
                performSwitchAnimation(false);
            }
        }
    }

    private void performChargingClick() {
        ChargeStateLoadedEvent event = EventBus.getDefault().getStickyEvent(ChargeStateLoadedEvent.class);
        if (event != null) {
            if (ChargeState.STATE_CHARGING.equals(event.getChargeState().getChargingState())) {
                EventBus.getDefault().post(new ToHandHoldRequestEvent(WEAR_ACTION_CHARGING_STOP));
                event.getChargeState().setChargingState(ChargeState.STATE_COMPLETE);
                performSwitchAnimation(true);
            } else {
                EventBus.getDefault().post(new ToHandHoldRequestEvent(WEAR_ACTION_CHARGING_START));
                event.getChargeState().setChargingState(ChargeState.STATE_CHARGING);
                performSwitchAnimation(false);
            }
        }
    }

    private void performFlashlightClick() {
        EventBus.getDefault().post(new ToHandHoldRequestEvent(WEAR_ACTION_FLASHLIGHTS));
        performActionAnimation(mActionImage1);
    }

    private void performDoorClick() {
        VehicleStateLoadedEvent event = EventBus.getDefault().getStickyEvent(VehicleStateLoadedEvent.class);
        if (event != null) {
            if (event.getVehicleState().isLocked()) {
                EventBus.getDefault().post(new ToHandHoldRequestEvent(WEAR_ACTION_DOOR_UNLOCK));
                event.getVehicleState().setLocked(false);
                performSwitchAnimation(true);
            } else {
                EventBus.getDefault().post(new ToHandHoldRequestEvent(WEAR_ACTION_DOOR_LOCK));
                event.getVehicleState().setLocked(true);
                performSwitchAnimation(false);
            }
        }
    }

    private void performHornClick() {
        EventBus.getDefault().post(new ToHandHoldRequestEvent(WEAR_ACTION_HORN));
        performActionAnimation(mActionImage1);
    }

    private void setHornViews() {
//        mActionImage1.setImageResource(R.drawable.action_horn);
        mActionLabel1.setText(getString(R.string.action_horn));
    }

    private void setDoorViews() {
        VehicleStateLoadedEvent event = EventBus.getDefault().getStickyEvent(VehicleStateLoadedEvent.class);
        if (event != null) {
            if (event.getVehicleState().isLocked()) {
                setFirstViewActive();
            } else {
                setSecondViewsActive();
            }
        } else {
            setSecondViewsActive();
        }
//        mActionImage1.setImageResource(R.drawable.action_car_lock);
        mActionLabel2.setText(getString(R.string.action_door_unlock));
//        mActionImage2.setImageResource(R.drawable.action_car_unlock);
        mActionLabel1.setText(getString(R.string.action_door_lock));
    }

    private void setFlashlightViews() {
        mActionImage1.setImageResource(R.drawable.action_lights);
        mActionLabel1.setText(getString(R.string.action_flash_lights));
    }

    private void setChargingViews() {
        ChargeStateLoadedEvent event = EventBus.getDefault().getStickyEvent(ChargeStateLoadedEvent.class);
        if (event != null) {
            if (ChargeState.STATE_CHARGING.equals(event.getChargeState().getChargingState())) {
                setFirstViewActive();
            } else {
                setSecondViewsActive();
            }
        } else {
            setSecondViewsActive();
        }
//        mActionImage2.setImageResource(R.drawable.action_charge_stop);
        mActionLabel2.setText(getString(R.string.action_charging_stop));
//        mActionImage1.setImageResource(R.drawable.action_charge_start);
        mActionLabel1.setText(getString(R.string.action_charging_start));
    }

    private void setFirstViewActive() {
        mActionImage1.setAlpha(1f);
        mActionLabel1.setAlpha(1f);
        mActionImage2.setAlpha(0f);
        mActionLabel2.setAlpha(0f);
    }

    private void setSecondViewsActive() {
        mActionImage1.setAlpha(1f);
        mActionLabel1.setAlpha(1f);
        mActionImage2.setAlpha(0f);
        mActionLabel2.setAlpha(0f);
    }

    private void setAutoConditioningViews() {
        ClimateStateLoadedEvent event = EventBus.getDefault().getStickyEvent(ClimateStateLoadedEvent.class);
        if (event != null) {
            if (event.getClimateState().isIsAutoConditioningOn()) {
                setFirstViewActive();
            } else {
                setSecondViewsActive();
            }
        } else {
            setFirstViewActive();
        }
//        mActionImage1.setImageResource(R.drawable.action_ac_start);
        mActionLabel1.setText(getString(R.string.action_auto_conditioning_start));
//        mActionImage2.setImageResource(R.drawable.action_ac_stop);
        mActionLabel2.setText(getString(R.string.action_auto_conditioning_stop));
    }

    private void setSetMaxChargingViews() {
        ChargeStateLoadedEvent event = EventBus.getDefault().getStickyEvent(ChargeStateLoadedEvent.class);
        if (event != null) {
            mActionButtonText.setText(String.valueOf(event.getChargeState().getMaxRangeChargeCounter()) + "%");
        }
        mActionLabel1.setText(getString(R.string.set_charging_limit));
    }

    private void setSetPassengerTempViews() {
        ClimateStateLoadedEvent event = EventBus.getDefault().getStickyEvent(ClimateStateLoadedEvent.class);
        if (event != null) {
            mActionButtonText.setText(String.valueOf(event.getClimateState().getPassengerTempSetting()) + "°C");
        }
        mActionLabel1.setText(getString(R.string.set_passenger_temp));
    }

    private void setSetDriverTempViews() {
        ClimateStateLoadedEvent event = EventBus.getDefault().getStickyEvent(ClimateStateLoadedEvent.class);
        if (event != null) {
            mActionButtonText.setText(String.valueOf(event.getClimateState().getDriverTempSetting()) + "°C");
        }
        mActionLabel1.setText(getString(R.string.set_driver_temp));
    }


}
