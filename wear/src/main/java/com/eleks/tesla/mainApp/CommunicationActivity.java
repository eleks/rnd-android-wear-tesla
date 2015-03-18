package com.eleks.tesla.mainApp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.eleks.tesla.service.CommunicationService;

/**
 * Created by Ihor.Demedyuk on 04.03.2015.
 */
public class CommunicationActivity extends Activity {
    public static final String LOG_TAG = SetValueActivity.class.getSimpleName();

    protected ServiceConnection mServerConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.d(LOG_TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(LOG_TAG, "onServiceDisconnected");
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        bindCommunicationService();
    }

    public void bindCommunicationService() {
        Intent i = new Intent(this , CommunicationService.class);
        this.bindService(i, mServerConn, BIND_AUTO_CREATE);
        this.startService(i);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindeCommunicationService();
    }

    public void unbindeCommunicationService() {
        this.stopService(new Intent(this, CommunicationService.class));
        this.unbindService(mServerConn);
    }
}
