package com.eleks.tesla.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.util.Log;
import android.view.SurfaceHolder;

import com.eleks.tesla.teslalib.ApiPathConstants;
import com.eleks.tesla.teslalib.models.CarState;
import com.eleks.tesla.teslalib.models.ChargeState;
import com.eleks.tesla.teslalib.models.VehicleState;
import com.eleks.tesla.R;
import com.eleks.tesla.events.ChargeStateLoadedEvent;
import com.eleks.tesla.events.VehicleStateLoadedEvent;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;

/**
 * Created by Ihor.Demedyuk on 10.02.2015.
 */
public class TeslaWatchFaceService extends CanvasWatchFaceService {
    public final String TAG = TeslaWatchFaceService.class.getSimpleName();

    private Engine mEngine;

    @Override
    public Engine onCreateEngine() {
        mEngine = new Engine();
        return mEngine;
    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        mEngine = null;
        super.onDestroy();
    }

    public void onEvent(ChargeStateLoadedEvent chargeStateLoadedEvent) {
        ChargeState chargeState = chargeStateLoadedEvent.getChargeState();
        mEngine.getCarState().updateCharge(chargeState);
        mEngine.invalidate();
    }

    public void onEvent(VehicleStateLoadedEvent chargeStateLoadedEvent) {
        VehicleState vehicleState = chargeStateLoadedEvent.getVehicleState();
        mEngine.getCarState().updateVehicleState(vehicleState);
        mEngine.invalidate();
    }

    /* implement service callback methods */
    private class Engine extends CanvasWatchFaceService.Engine {

        public static final String TIME_TEXT_EXAMPLE = "20:20";
        public static final String RANGE_TEXT_EXAMPLE = "390 Mi";
        public static final String TIME_FORMATTER = "%H:%M";
        public final String TAG = Engine.class.getSimpleName();

        public static final int MSG_UPDATE_TIME = 12;
        public static final int MSG_LOAD_CAR_STATE = 13;
        public static final int INTERACTIVE_UPDATE_RATE_MS = 1000;
        private static final int CONNECTION_TIME_OUT_MS = 1000;

        public static final int LOAD_CAR_STATE_DELAY_MS = 10 * 1000;
        private GoogleApiClient mGoogleApiClient;
        private String mNodeId;

        public static final String MAX_RANGE = "TYPICAL RANGE";
        public static final String MILES = " Mi";

        /* a time object */
        private Time mTime;

        /* device features */
        private boolean mLowBitAmbient;
        private boolean mBurnInProtection;

        /* graphic objects */
        private Paint mBgPaint;
        private Paint mTimePaint;
        private Paint mMaxRangePaint;
        private Paint mRangeValuePaint;
        private Paint mDotsPaint;

        private Bitmap mLockedScaledBitmap;
        private Bitmap mUnLockedScaledBitmap;
        private Bitmap mChargingScaledBitmap;
        private Bitmap mLockedBitmap;
        private Bitmap mUnLockedBitmap;
        private Bitmap mChargingBitmap;

        private CarState mCarState = new CarState();  //TODO set Default values
        private AsyncTask<Void, Void, Void> mLoadCarStateTask;

        boolean mRegisteredTimeZoneReceiver;
        private int mTimeFontSize = 0;
        private int mMaxRangeFontSize = 0;
        private int mRangeValueFontSize = 0;

        public void setCarState(CarState mCarState) {
            this.mCarState = mCarState;
        }

        public CarState getCarState() {
            return mCarState;
        }

        final Handler mUpdateTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case MSG_UPDATE_TIME:
                        invalidate();
                        if (shouldTimerBeRunning()) {
                            long timeMs = System.currentTimeMillis();
                            long delayMs = INTERACTIVE_UPDATE_RATE_MS
                                    - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
                            mUpdateTimeHandler
                                    .sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
                        }
                        break;
                }
            }
        };

        final Handler mLoadCarStateHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case MSG_LOAD_CAR_STATE:
                        cancelLoadCarStateTask();
                        mLoadCarStateTask = new LoadCarStateTask();
                        mLoadCarStateTask.execute();
                        break;
                }
            }
        };

        private class LoadCarStateTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... params) {
                fetchCarState();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (isVisible()) {
                    mLoadCarStateHandler.sendEmptyMessageDelayed(
                            MSG_LOAD_CAR_STATE, LOAD_CAR_STATE_DELAY_MS);
                }
            }
        }

        private void fetchCarState() {
            if (mNodeId != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        connectApiClient();

                        Wearable.MessageApi.sendMessage(mGoogleApiClient, mNodeId, ApiPathConstants.WEAR_GET_CAR_CONFIG, null).await();
                        mGoogleApiClient.disconnect();
                    }
                }).start();
            }
        }


        private void connectApiClient() {
            if (isClientDisconnected()) {
                mGoogleApiClient.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
            }
        }

        private boolean isClientDisconnected() {
            return mGoogleApiClient != null && !(mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting());
        }

        private void initGoogleApiClient() {
            mGoogleApiClient = getGoogleApiClient(TeslaWatchFaceService.this);
            retrieveDeviceNode();
        }

        private GoogleApiClient getGoogleApiClient(Context context) {
            return new GoogleApiClient.Builder(context)
                    .addApi(Wearable.API)
                    .build();
        }

        private void retrieveDeviceNode() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    connectApiClient();

                    NodeApi.GetConnectedNodesResult result =
                            Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();

                    List<Node> nodes = result.getNodes();

                    if (nodes.size() > 0) {
                        mNodeId = nodes.get(0).getId();
                    }

                    Log.v(TAG, "Node ID of phone: " + mNodeId);

                    mGoogleApiClient.disconnect();
                }
            }).start();
        }

        /* receiver to update the time zone */
        final BroadcastReceiver mTimeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mTime.clear(intent.getStringExtra("time-zone"));
                mTime.setToNow();
            }
        };

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            mBgPaint = new Paint();
            mBgPaint.setARGB(255, 0, 0, 0);

            mTimePaint = new Paint();
            mTimePaint.setARGB(255, 255, 255, 255);
            mTimePaint.setAntiAlias(true);
            Typeface tf = Typeface.createFromAsset(getAssets(), "RobotoCondensed-Light.ttf");
            mTimePaint.setTypeface(tf);

            mMaxRangePaint = new Paint();
            mMaxRangePaint.setARGB(120, 255, 255, 255);
            mMaxRangePaint.setAntiAlias(true);

            mRangeValuePaint = new Paint();
            mRangeValuePaint.setARGB(190, 255, 255, 255);
            mRangeValuePaint.setAntiAlias(true);
            mRangeValuePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

            mDotsPaint = new Paint();
            mDotsPaint.setAntiAlias(true);

            mTime = new Time();

            Resources resources = TeslaWatchFaceService.this.getResources();

            Drawable chargingDrawable = resources.getDrawable(R.drawable.charging);
            mChargingBitmap = ((BitmapDrawable) chargingDrawable).getBitmap();

            Drawable mLockedBitmapDrawable = resources.getDrawable(R.drawable.locked_small);
            mLockedBitmap = ((BitmapDrawable) mLockedBitmapDrawable).getBitmap();

            Drawable unlockedDrawable = resources.getDrawable(R.drawable.unlocked_small);
            mUnLockedBitmap = ((BitmapDrawable) unlockedDrawable).getBitmap();

            configureStyle();

            initGoogleApiClient();
        }

        private void configureStyle() {
            setWatchFaceStyle(new WatchFaceStyle.Builder(TeslaWatchFaceService.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    .setBackgroundVisibility(WatchFaceStyle
                            .BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .build());
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);

            mLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
            mBurnInProtection = properties.getBoolean(PROPERTY_BURN_IN_PROTECTION,
                    false);
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);

            //TODO change Paints antiAlias
            if (mLowBitAmbient) {
                boolean antiAlias = !inAmbientMode;
//                mHourPaint.setAntiAlias(antiAlias);
//                mMinutePaint.setAntiAlias(antiAlias);
//                mSecondPaint.setAntiAlias(antiAlias);
            }
            invalidate();
            updateTimer();
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            // Update the time
            mTime.setToNow();

            int width = bounds.width();
            int height = bounds.height();

            float centerX = width / 2f;
            float centerY = height / 2f;

            // draw a bg
            canvas.drawRect(0, 0, width, height, mBgPaint);

            // draw labels
            if (mTimeFontSize == 0) {
                initTimeFontSize(bounds);
            }
            Rect textRect = new Rect();
            mTimePaint.getTextBounds("20", 0, 1, textRect);
            int timeYPos = (int) ((canvas.getHeight() / 2) + (textRect.height() * 0.75f));
            int timeXPos = (int) ((width - mTimePaint.measureText(TIME_TEXT_EXAMPLE)) / 2);
            String time = mTime.format(TIME_FORMATTER);
            canvas.drawText(time, timeXPos, timeYPos, mTimePaint);


            if (mMaxRangeFontSize == 0) {
                initMaxRangeFontSize(bounds);
            }
            int maxRangeXPos = (int) ((width - mMaxRangePaint.measureText(MAX_RANGE)) / 2);
            int maxRangeYPos = (int) ((canvas.getHeight() * 0.8f) - ((mMaxRangePaint.descent() + mMaxRangePaint.ascent()) / 2));
            canvas.drawText(MAX_RANGE, maxRangeXPos, maxRangeYPos, mMaxRangePaint);

            if (mRangeValueFontSize == 0) {
                initRangeValueFontSize(bounds);
            }
            String rangeText = Math.round(mCarState.getDistance()) + MILES;
            int rangeValueXPos = (int) ((width - mRangeValuePaint.measureText(rangeText)) / 2);
            int rangeValueYPos = (int) ((canvas.getHeight() * 0.88) - ((mRangeValuePaint.descent() + mRangeValuePaint.ascent()) / 2));
            canvas.drawText(rangeText, rangeValueXPos, rangeValueYPos, mRangeValuePaint);

            // draw dots
            int dotsLinesCount = mCarState.getBatteryCharge(); // 100% notif_charge = 100 dots
            float startRot = (float) (Math.PI + Math.PI / 6f);
            float endRot = (float) ((3 * Math.PI) - (Math.PI / 6f));
            float deltaRot = (endRot - startRot) / 101;             // 100 max dots count

            // set dots Color
            if (mCarState.getBatteryCharge() <= 15) {
                mDotsPaint.setARGB(255, 249, 67, 58);  // red
            } else if (mCarState.getBatteryCharge() <= 60) {
                mDotsPaint.setARGB(255, 50, 201, 218); // blue
            } else {
                mDotsPaint.setARGB(255, 58, 249, 103); // green
            }

            // draw dots bg
            float dotRadius = width / 100f;
            float clockRadius = centerX - 3 * dotRadius;
            mDotsPaint.setAlpha(60);
            for (int k = 0; k < 10; k++) {
                for (int i = 1; i <= 100; i++) {
                    float cx = (float) (centerX + clockRadius * Math.sin(startRot + deltaRot * i));
                    float cy = (float) (centerY + clockRadius * -1 * Math.cos(startRot + deltaRot * i));
                    canvas.drawCircle(cx, cy, dotRadius * 0.8f, mDotsPaint);
                }

                dotRadius = dotRadius * 0.9f;
                clockRadius = clockRadius - 3 * dotRadius;
            }

            // draw first and last dots lines

            dotRadius = width / 100f;
            clockRadius = centerX - 3 * dotRadius;
            mDotsPaint.setAlpha(80);
            for (int k = 0; k < 10; k++) {
                for (int i = 1; i <= dotsLinesCount; i = i + dotsLinesCount - 1) {
                    float cx = (float) (centerX + clockRadius * Math.sin(startRot + deltaRot * i));
                    float cy = (float) (centerY + clockRadius * -1 * Math.cos(startRot + deltaRot * i));
                    canvas.drawCircle(cx, cy, dotRadius * 0.8f, mDotsPaint);
                }

                dotRadius = dotRadius * 0.9f;
                clockRadius = clockRadius - 3 * dotRadius;

                int alpha = 80 - (k * 10);
                if (alpha > 0) {
                    mDotsPaint.setAlpha(alpha);
                } else {
                    mDotsPaint.setAlpha(0);
                }
            }


            // draw dots
            dotRadius = width / 100f;
            clockRadius = centerX - 3 * dotRadius;
            mDotsPaint.setAlpha(255);
            for (int k = 0; k < 10; k++) {
                int i;
                for (i = 2; i < dotsLinesCount; i++) {
                    float cx = (float) (centerX + clockRadius * Math.sin(startRot + deltaRot * i));
                    float cy = (float) (centerY + clockRadius * -1 * Math.cos(startRot + deltaRot * i));
                    canvas.drawCircle(cx, cy, dotRadius * 0.8f, mDotsPaint);
                }

                dotRadius = dotRadius * 0.9f;
                clockRadius = clockRadius - 3 * dotRadius;

                int alpha = 195 - (k * 30);
                if (alpha > 0) {
                    mDotsPaint.setAlpha(alpha);
                } else {
                    mDotsPaint.setAlpha(0);
                }
            }

            //draw lock and notif_charge states icons
            int iconSideSize = width / 12;

            if (mLockedScaledBitmap == null
                    || mLockedScaledBitmap.getWidth() != iconSideSize
                    || mLockedScaledBitmap.getHeight() != iconSideSize) {
                mLockedScaledBitmap = Bitmap.createScaledBitmap(mLockedBitmap,
                        iconSideSize, iconSideSize, true);
            }

            if (mUnLockedScaledBitmap == null
                    || mUnLockedScaledBitmap.getWidth() != iconSideSize
                    || mUnLockedScaledBitmap.getHeight() != iconSideSize) {
                mUnLockedScaledBitmap = Bitmap.createScaledBitmap(mUnLockedBitmap,
                        iconSideSize, iconSideSize, true);
            }

            if (mChargingScaledBitmap == null
                    || mChargingScaledBitmap.getWidth() != iconSideSize
                    || mChargingScaledBitmap.getHeight() != iconSideSize) {
                mChargingScaledBitmap = Bitmap.createScaledBitmap(mChargingBitmap,
                        iconSideSize, iconSideSize, true);
            }

            Bitmap lockStateBitmap = mCarState.isLocked() ? mLockedScaledBitmap : mUnLockedScaledBitmap;
            if (mCarState.isIsCharging()) {
                canvas.drawBitmap(lockStateBitmap, width / 2, height / 4, null);
                canvas.drawBitmap(mChargingScaledBitmap, (width / 2) - iconSideSize, height / 4, null);
            } else {
                canvas.drawBitmap(lockStateBitmap, (width - iconSideSize) / 2, height / 4, null);
            }

        }

        private void initTimeFontSize(Rect bounds) {
            float maxWidth = bounds.width() / 2f;
            do {
                mTimePaint.setTextSize(++mTimeFontSize);
            } while (mTimePaint.measureText(TIME_TEXT_EXAMPLE) < maxWidth);
        }

        private void initMaxRangeFontSize(Rect bounds) {
            float maxWidth = bounds.width() / 3.2f;
            do {
                mMaxRangePaint.setTextSize(++mMaxRangeFontSize);
            } while (mMaxRangePaint.measureText(MAX_RANGE) < maxWidth);
        }

        private void initRangeValueFontSize(Rect bounds) {
            float maxWidth = bounds.width() / 4.5f;
            do {
                mRangeValuePaint.setTextSize(++mRangeValueFontSize);
            } while (mRangeValuePaint.measureText(RANGE_TEXT_EXAMPLE) < maxWidth);
        }


        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (visible) {
                registerReceiver();

                // Update time zone in case it changed while we weren't visible.
                mTime.clear(TimeZone.getDefault().getID());
                mTime.setToNow();

                mLoadCarStateHandler.sendEmptyMessage(MSG_LOAD_CAR_STATE);
            } else {
                unregisterReceiver();

                mLoadCarStateHandler.removeMessages(MSG_LOAD_CAR_STATE);
                cancelLoadCarStateTask();
            }

            // Whether the timer should be running depends on whether we're visible and
            // whether we're in ambient mode), so we may need to start or stop the timer
            updateTimer();
        }

        private void cancelLoadCarStateTask() {
            if (mLoadCarStateTask != null) {
                mLoadCarStateTask.cancel(true);
            }
        }

        private void updateTimer() {
            mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (shouldTimerBeRunning()) {
                mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }

        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        private void registerReceiver() {
            if (mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = true;
            IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            TeslaWatchFaceService.this.registerReceiver(mTimeZoneReceiver, filter);
        }

        private void unregisterReceiver() {
            if (!mRegisteredTimeZoneReceiver) {
                return;
            }
            mRegisteredTimeZoneReceiver = false;
            TeslaWatchFaceService.this.unregisterReceiver(mTimeZoneReceiver);
        }
    }
}
