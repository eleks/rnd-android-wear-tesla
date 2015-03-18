package com.eleks.tesla.service;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.eleks.tesla.teslalib.models.ChargeState;
import com.eleks.tesla.teslalib.models.ClimateState;
import com.eleks.tesla.teslalib.models.DriveState;
import com.eleks.tesla.teslalib.models.VehicleState;
import com.eleks.tesla.teslalib.utils.SerializationUtil;
import com.eleks.tesla.ConfirmationNotificationActivity;
import com.eleks.tesla.R;
import com.eleks.tesla.events.ChargeStateLoadedEvent;
import com.eleks.tesla.events.ClimateStateLoadedEvent;
import com.eleks.tesla.events.DriveStateLoadedEvent;
import com.eleks.tesla.events.LocationMapLoadedEvent;
import com.eleks.tesla.events.VehicleStateLoadedEvent;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.IOException;

import de.greenrobot.event.EventBus;

import static com.eleks.tesla.teslalib.ApiPathConstants.*;

/**
 * Created by Ihor.Demedyuk on 13.02.2015.
 */
public class WearListenerService extends WearableListenerService {
    private final String LOG_TAG = WearListenerService.class.getSimpleName();

    private String mPhoneNodeId;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        mPhoneNodeId = messageEvent.getSourceNodeId();
        Log.v(LOG_TAG, "Node ID of phone: " + mPhoneNodeId);

        try {
            if (messageEvent.getPath().equals(MOBILE_UPDATE_CHARGE_STATE)) {
                postChargeStateFromMsg(messageEvent);
            } else if (MOBILE_UPDATE_CLIMATE_STATE.equals(messageEvent.getPath())) {
                postClimateStateFromMsg(messageEvent);
            } else if (MOBILE_UPDATE_DRIVE_STATE.equals(messageEvent.getPath())) {
                postDriveStateFromMsg(messageEvent);
            } else if (MOBILE_UPDATE_VEHICLE_STATE.equals(messageEvent.getPath())) {
                postVehicleStateFromMsg(messageEvent);
            } else if (MOBILE_UPDATE_LOCATION_MAP.equals(messageEvent.getPath())){
                postLocationMapFromMsg(messageEvent);
            } else if (MOBILE_SHOW_NOTIFICATION_BAD_WEATHER.equals(messageEvent.getPath())){
                showBabWeatherNotification();
            } else if (MOBILE_SHOW_NOTIFICATION_UNEXPECTED_STOP_CHARGING.equals(messageEvent.getPath())){
                showUnexpectedStopChargingNotification();
            } else if (MOBILE_SHOW_NOTIFICATION_CAR_UNLOCKED.equals(messageEvent.getPath())){
                showCarUnlockedNotification();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showCarUnlockedNotification() {
        int notificationId = 002;

        Intent viewIntent = new Intent(this, ConfirmationNotificationActivity.class);
        viewIntent.setAction(ConfirmationNotificationActivity.ACTION_LOCK_DOOR);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                        .setHintHideIcon(true)
                        .setBackground(BitmapFactory.decodeResource(getResources(), R.mipmap.notification_car_unlocked));

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .extend(wearableExtender)
                        .setSmallIcon(R.mipmap.notification_small_car_unlock)
                        .setContentTitle(getString(R.string.notification_car_unlocked_title))
                        .addAction(R.mipmap.notif_unlock, getString(R.string.notification_car_unlocked_text), viewPendingIntent);

        sendNotification(notificationId, notificationBuilder);
    }

    private void showUnexpectedStopChargingNotification() {
        int notificationId = 003;

        Intent viewIntent = new Intent(this, ConfirmationNotificationActivity.class);
        viewIntent.setAction(ConfirmationNotificationActivity.ACTION_START_CHARGING);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                        .setHintHideIcon(true)
                        .setBackground(BitmapFactory.decodeResource(getResources(), R.mipmap.notification_charge_interraped));

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .extend(wearableExtender)
                        .setSmallIcon(R.mipmap.notification_small_charge_stopped)
                        .setContentTitle(getString(R.string.notification_unexpected_charge_stop_title))
                        .addAction(R.mipmap.notif_charge, getString(R.string.notification_unexpected_charge_stop_text), viewPendingIntent);

        sendNotification(notificationId, notificationBuilder);
    }

    private void showBabWeatherNotification() {
        int notificationId = 001;

        Intent viewIntent = new Intent(this, ConfirmationNotificationActivity.class);
        viewIntent.setAction(ConfirmationNotificationActivity.ACTION_CLOSE_SUNROOF);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                        .setHintHideIcon(true)
                        .setBackground(BitmapFactory.decodeResource(getResources(), R.mipmap.notification_bad_weather));

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .extend(wearableExtender)
                        .setSmallIcon(R.mipmap.notification_small_car_unlock)
                        .setContentTitle(getString(R.string.notification_bad_weather_title))
                        .addAction(R.mipmap.notif_unlock, getString(R.string.notification_bad_weather_text), viewPendingIntent);

        sendNotification(notificationId, notificationBuilder);
    }

    private void sendNotification(int notificationId, NotificationCompat.Builder notificationBuilder){
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    private void postLocationMapFromMsg(MessageEvent messageEvent) {
        Bitmap mapImage = BitmapFactory.decodeByteArray(messageEvent.getData(), 0, messageEvent.getData().length);
        EventBus.getDefault().postSticky(new LocationMapLoadedEvent(mapImage));
    }

    private void postChargeStateFromMsg(MessageEvent messageEvent) throws IOException, ClassNotFoundException {
        ChargeState state = (ChargeState) SerializationUtil.deserialize(messageEvent.getData());
        EventBus.getDefault().postSticky(new ChargeStateLoadedEvent(state));
    }

    private void postClimateStateFromMsg(MessageEvent messageEvent) throws IOException, ClassNotFoundException {
        ClimateState state = (ClimateState) SerializationUtil.deserialize(messageEvent.getData());
        EventBus.getDefault().postSticky(new ClimateStateLoadedEvent(state));
    }

    private void postDriveStateFromMsg(MessageEvent messageEvent) throws IOException, ClassNotFoundException {
        DriveState state = (DriveState) SerializationUtil.deserialize(messageEvent.getData());
        EventBus.getDefault().postSticky(new DriveStateLoadedEvent(state));
    }

    private void postVehicleStateFromMsg(MessageEvent messageEvent) throws IOException, ClassNotFoundException {
        VehicleState state = (VehicleState) SerializationUtil.deserialize(messageEvent.getData());
        EventBus.getDefault().postSticky(new VehicleStateLoadedEvent(state));
    }
}
