package com.eleks.tesla.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.eleks.tesla.teslalib.utils.SerializationUtil;
import com.eleks.tesla.events.ToHandHoldRequestEvent;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;

/**
 * Created by Ihor.Demedyuk on 04.03.2015.
 */
public class CommunicationService extends Service {

    public static final String LOG_TAG = CommunicationService.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private String mNodeId;
    private static final long CONNECTION_TIME_OUT_MS = 1000;

    @Override
    public void onCreate() {
        super.onCreate();
        initGoogleApiClient();
    }

    @Override
    public IBinder onBind(Intent intent) {
        EventBus.getDefault().register(this);
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        EventBus.getDefault().unregister(this);
        return super.onUnbind(intent);
    }

    private void initGoogleApiClient() {
        mGoogleApiClient = getGoogleApiClient(this);
        retrieveHandHoldDeviceNode();
    }

    private GoogleApiClient getGoogleApiClient(Context context) {
        return new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
    }

    private void retrieveHandHoldDeviceNode() {
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

                Log.v(LOG_TAG, "Node ID of phone: " + mNodeId);

                mGoogleApiClient.disconnect();
            }
        }).start();
    }

    public void onEventAsync(ToHandHoldRequestEvent event) {
        byte[] serializableCommand = null;

        if (event.getCommand() != null) {
            try {
                serializableCommand = SerializationUtil.serialize(event.getCommand());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        connectApiClient();
        Wearable.MessageApi.sendMessage(mGoogleApiClient, mNodeId, event.getPath(), serializableCommand).await();
        mGoogleApiClient.disconnect();
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
