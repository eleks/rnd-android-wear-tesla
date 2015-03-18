package com.eleks.tesla;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.eleks.tesla.teslalib.ApiPathConstants;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private final String TAG = "MainActivity";

    private GoogleApiClient mGoogleApiClient;
    private String mNodeId;
    private static final long CONNECTION_TIME_OUT_MS = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGoogleApiClient();
        setContentView(R.layout.activity_main);
        findViewById(R.id.send_bad_weather_notification).setOnClickListener(this);
        findViewById(R.id.send_unexpected_charge_stop_notification).setOnClickListener(this);
        findViewById(R.id.send_car_unlocked_notification).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String command = null;
        switch (v.getId()){
            case R.id.send_bad_weather_notification:
                command = ApiPathConstants.MOBILE_SHOW_NOTIFICATION_BAD_WEATHER;
                break;
            case R.id.send_unexpected_charge_stop_notification:
                command = ApiPathConstants.MOBILE_SHOW_NOTIFICATION_UNEXPECTED_STOP_CHARGING;
                break;
            case R.id.send_car_unlocked_notification:
                command = ApiPathConstants.MOBILE_SHOW_NOTIFICATION_CAR_UNLOCKED;
                break;
        }
        sendMassage(command);
    }

    private void sendMassage(final String command){
        new Thread(new Runnable() {
            @Override
            public void run() {
                connectApiClient();
                Wearable.MessageApi.sendMessage(mGoogleApiClient, mNodeId, command, null).await();
                mGoogleApiClient.disconnect();
            }
        }).start();
    }

    private void initGoogleApiClient() {
        mGoogleApiClient = getGoogleApiClient(this);
        retrieveWearableDeviceNode();
    }

    private GoogleApiClient getGoogleApiClient(Context context) {
        return new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
    }

    private void retrieveWearableDeviceNode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mGoogleApiClient != null && !(mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting()))
                    mGoogleApiClient.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);

                NodeApi.GetConnectedNodesResult result =
                        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();

                List<Node> nodes = result.getNodes();

                if (nodes.size() > 0)
                    mNodeId = nodes.get(0).getId();

                Log.v(TAG, "Node ID of watch: " + mNodeId);

                mGoogleApiClient.disconnect();
            }
        }).start();
    }

    private void connectApiClient() {
        if (isClientDisconnected()) {
            mGoogleApiClient.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
        }
    }

    private boolean isClientDisconnected() {
        return mGoogleApiClient != null && !(mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting());
    }
}
