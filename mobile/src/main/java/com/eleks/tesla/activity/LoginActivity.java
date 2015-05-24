package com.eleks.tesla.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.eleks.tesla.R;
import com.eleks.tesla.api.Config;
import com.eleks.tesla.api.TeslaApi;
import com.eleks.tesla.api.auth.AuthCredentials;
import com.eleks.tesla.utils.PreferencesManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends BaseActivity {

    private GoogleApiClient mGoogleApiClient;
    private String mNodeId;

    @InjectView(R.id.signIn)
    Button signInBtn;
    @InjectView(R.id.signOut)
    Button signOutBtn;
    @InjectView(R.id.username)
    TextView tvUserName;
    @InjectView(R.id.password)
    TextView tvPassword;
    @InjectView(R.id.signInForm)
    View signInForm;
    @InjectView(R.id.progressContainer)
    View progressContainer;
    @InjectView(R.id.signOutForm)
    View signOutForm;

    private AsyncTask loginTask = new AsyncTask<Object, Object, Object>() {
        @Override
        protected Object doInBackground(Object... voids) {
            try {
                TeslaApi api = TeslaApi.getInstance();
                Log.d(Config.TAG, "Performing login operation...");
                JSONObject loginResult = api.login(tvUserName.getText().toString(), tvPassword.getText().toString());
                String accessToken = loginResult.getString("access_token");
                Log.d(Config.TAG, "Access Token: " + accessToken);
                PreferencesManager.putAccessToken(LoginActivity.this, accessToken);

                AuthCredentials credentials = new AuthCredentials("ua.nazar@gmail.com", accessToken);

                try {
                    JSONObject carsResult = api.getVehicles(credentials);
                    JSONArray vehicles = (JSONArray) carsResult.get("response");
                    JSONObject firstVehicle = (JSONObject) vehicles.get(0);
                    long carId = firstVehicle.getLong("id");
                    PreferencesManager.putCarId(LoginActivity.this, carId);
                } catch (Exception e) {

                }
            } catch (Exception e) {
                return e;
            }
            return new Object();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (o instanceof Exception) {
                Toast.makeText(LoginActivity.this, ((Exception) o).getMessage(), Toast.LENGTH_SHORT).show();
                showLoginForm();
            } else {
                showSignOutForm();
            }
        }
    };


    private class TestAsync extends AsyncTask<Object, Object, Object> {
        @Override
        protected Object doInBackground(Object... voids) {
            try {

                TeslaApi api = TeslaApi.getInstance();
                //Log.d(Config.TAG, "Performing login operation...");
                String accessToken = PreferencesManager.getAccessToken(LoginActivity.this);
                AuthCredentials credentials = new AuthCredentials("ua.nazar@gmail.com", accessToken);

                try {
                    JSONObject locationResult = api.getDriveState(PreferencesManager.getCarId(LoginActivity.this), credentials);
                    Log.d(Config.TAG, "Performing login operation...");
                    JSONObject response = locationResult.getJSONObject("response");
                    // we can save car location
                    double lat = response.getDouble("latitude");
                    double lon = response.getDouble("longitude");
                    //Log.d(Config.TAG, "Lat: ");
                    //PreferencesManager.putCarId(LoginActivity.this, carId);
                } catch (Exception e) {

                }

                //long vId = 6560460414667285209L;
                //api.flashLights(vId, credentials);

                //login(tvUserName.getText().toString(), tvPassword.getText().toString());
                //String accessToken = loginResult.getString("access_token");
                //Log.d(Config.TAG, "Access Token: " + accessToken);
                //PreferencesManager.putAccessToken(LoginActivity.this, accessToken);
                //sendMassage(ApiPathConstants.MOBILE_SHOW_NOTIFICATION_BAD_WEATHER);
            } catch (Exception e) {
                return e;
            }
            return new Object();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGoogleApiClient();
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userNameValue = tvUserName.getText().toString().trim();
                final String passwordValue = tvPassword.getText().toString().trim();

                if (TextUtils.isEmpty(userNameValue) || TextUtils.isEmpty(passwordValue)) {
                    Toast.makeText(LoginActivity.this, "Empty values are not allowed", Toast.LENGTH_SHORT).show();
                    return;
                }

                PreferencesManager.putUserName(LoginActivity.this, userNameValue);

                loginTask.execute();
            }
        });
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferencesManager.clear(LoginActivity.this);
                showLoginForm();
                //new TestAsync().execute();
            }
        });

        showLoginForm();

        if (PreferencesManager.isLoggedIn(this)) {
            showSignOutForm();
        }
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
                    mGoogleApiClient.blockingConnect(1000, TimeUnit.MILLISECONDS);

                NodeApi.GetConnectedNodesResult result =
                        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();

                List<Node> nodes = result.getNodes();

                if (nodes.size() > 0)
                    mNodeId = nodes.get(0).getId();

                Log.v("TAG", "Node ID of watch: " + mNodeId);

                mGoogleApiClient.disconnect();
            }
        }).start();
    }

    // TODO not used now
    private void showProgres() {
        signInForm.setVisibility(View.INVISIBLE);
        progressContainer.setVisibility(View.VISIBLE);
        signOutForm.setVisibility(View.INVISIBLE);
    }

    private void showLoginForm() {
        signInForm.setVisibility(View.VISIBLE);
        progressContainer.setVisibility(View.INVISIBLE);
        signOutForm.setVisibility(View.INVISIBLE);
    }

    private void showSignOutForm() {
        signInForm.setVisibility(View.INVISIBLE);
        progressContainer.setVisibility(View.INVISIBLE);
        signOutForm.setVisibility(View.VISIBLE);
    }

    private void sendMassage(final String command) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                connectApiClient();
                Wearable.MessageApi.sendMessage(mGoogleApiClient, mNodeId, command, null).await();
                mGoogleApiClient.disconnect();
            }
        }).start();
    }

    private void connectApiClient() {
        if (isClientDisconnected()) {
            mGoogleApiClient.blockingConnect(1000, TimeUnit.MILLISECONDS);
        }
    }

    private boolean isClientDisconnected() {
        return mGoogleApiClient != null && !(mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting());
    }
}
