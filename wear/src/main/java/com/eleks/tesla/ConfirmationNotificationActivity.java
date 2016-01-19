package com.eleks.tesla;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.DelayedConfirmationView;
import android.view.View;
import android.widget.TextView;

import com.eleks.tesla.events.ToHandHoldRequestEvent;

import de.greenrobot.event.EventBus;

import static com.eleks.tesla.teslalib.ApiPathConstants.WEAR_ACTION_CHARGING_START;
import static com.eleks.tesla.teslalib.ApiPathConstants.WEAR_ACTION_DOOR_LOCK;
import static com.eleks.tesla.teslalib.ApiPathConstants.WEAR_CLOSE_SUNROOF;

/**
 * Created by maryan.melnychuk on 16.03.2015.
 */
public class ConfirmationNotificationActivity extends Activity implements DelayedConfirmationView.DelayedConfirmationListener {
    public static final String ACTION_CLOSE_SUNROOF = "com.eleks.tesla.CLOSE_SUNROOF";
    public static final String ACTION_LOCK_DOOR = "com.eleks.tesla.LOCK_DOOR";
    public static final String ACTION_START_CHARGING = "com.eleks.tesla.START_CHARGING";

    public static final int ACTION_MODE_CLOSE_SUNROOF = 1;
    public static final int ACTION_MODE_LOCK_DOOR = 2;
    public static final int ACTION_MODE_START_CHARGING = 3;

    private int mActionMode;

    private DelayedConfirmationView mDelayedConfirmation;
    private TextView mConfirmationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            String action = getIntent().getAction();
            if (ACTION_CLOSE_SUNROOF.equals(action)) {
                mActionMode = ACTION_MODE_CLOSE_SUNROOF;
            } else if (ACTION_LOCK_DOOR.equals(action)) {
                mActionMode = ACTION_MODE_LOCK_DOOR;
            } else if (ACTION_START_CHARGING.equals(action)) {
                mActionMode = ACTION_MODE_START_CHARGING;
            }
        }
        setContentView(R.layout.activity_notification_confirmation);
        mDelayedConfirmation = (DelayedConfirmationView) findViewById(R.id.delayed_confirm);
        mConfirmationText = (TextView) findViewById(R.id.confirmation_text);
        initView();
    }

    private void initView() {
        mDelayedConfirmation.setListener(this);
        mDelayedConfirmation.setTotalTimeMs(3000);
        mDelayedConfirmation.start();
        switch (mActionMode) {
            case ACTION_MODE_CLOSE_SUNROOF:
                mConfirmationText.setText(R.string.notification_bad_weather_confirmation);
                break;
            case ACTION_MODE_LOCK_DOOR:
                mConfirmationText.setText(R.string.notification_car_unlocked_confirmation);
                break;
            case ACTION_MODE_START_CHARGING:
                mConfirmationText.setText(R.string.notification_unexpected_charge_stop_confirmation);
                break;
        }
    }

    @Override
    public void onTimerFinished(View view) {
        Intent intent = new Intent(this, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION);
        switch (mActionMode) {
            case ACTION_MODE_CLOSE_SUNROOF:
                EventBus.getDefault().post(new ToHandHoldRequestEvent(WEAR_CLOSE_SUNROOF));
                intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, getString(R.string.notification_bad_weather_confirmation_done));
                break;
            case ACTION_MODE_LOCK_DOOR:
                EventBus.getDefault().post(new ToHandHoldRequestEvent(WEAR_ACTION_DOOR_LOCK));
                intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, getString(R.string.notification_car_unlocked_confirmation_done));
                break;
            case ACTION_MODE_START_CHARGING:
                EventBus.getDefault().post(new ToHandHoldRequestEvent(WEAR_ACTION_CHARGING_START));
                intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, getString(R.string.notification_unexpected_charge_stop_confirmation_done));
                break;
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onTimerSelected(View view) {
        finish();
    }
}
