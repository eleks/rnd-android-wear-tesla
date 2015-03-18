package com.eleks.tesla;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.eleks.tesla.api.ApiAdapter;
import com.eleks.tesla.teslalib.commands.SetChargeLimitCommand;
import com.eleks.tesla.teslalib.commands.SetTempCommand;
import com.eleks.tesla.teslalib.models.ClimateState;
import com.eleks.tesla.teslalib.models.DriveState;
import com.eleks.tesla.teslalib.models.Result;
import com.eleks.tesla.teslalib.utils.SerializationUtil;
import com.eleks.tesla.teslalib.models.ChargeState;
import com.eleks.tesla.teslalib.models.VehicleState;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.eleks.tesla.teslalib.ApiPathConstants.*;

/**
 * Created by Ihor.Demedyuk on 11.02.2015.
 */
public class MobileListenerService extends WearableListenerService {
    public final String TAG = MobileListenerService.class.getSimpleName();

    public static final String CAR_ID = "321";
    private final String LOG_TAG = MobileListenerService.class.getSimpleName();

    private static final long CONNECTION_TIME_OUT_MS = 1000;

    private GoogleApiClient mGoogleApiClient;
    private String mNodeId;
    private ApiAdapter mApiService;

    @Override
    public void onCreate() {
        super.onCreate();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://private-anon-660366a25-timdorr.apiary-mock.com")
                .build();

        mApiService = restAdapter.create(ApiAdapter.class);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        mNodeId = messageEvent.getSourceNodeId();
            Log.v(LOG_TAG, "Node ID of watch: " + mNodeId);

            if (messageEvent.getPath().equals(WEAR_GET_CAR_CONFIG)) {
                getVehicleState();
                getChargeState();
            } else if (messageEvent.getPath().equals(WEAR_GET_CHARGE_STATE)) {
                getChargeState();
            } else if (messageEvent.getPath().equals(WEAR_GET_CLIMATE_STATE)) {
                getClimateState();
            } else if (messageEvent.getPath().equals(WEAR_GET_DRIVE_STATE)) {
                getDriveState();
            } else if (messageEvent.getPath().equals(WEAR_GET_VEHICLE_STATE)) {
                getVehicleState();
            } else if (messageEvent.getPath().equals(WEAR_GET_LOCATION_MAP)){
                getLocationMap();
            } else if (messageEvent.getPath().equals(WEAR_ACTION_HORN)) {
                performActionHorn();
            } else if (messageEvent.getPath().equals(WEAR_ACTION_DOOR_LOCK)) {
                performActionDoorLock();
            } else if (messageEvent.getPath().equals(WEAR_ACTION_DOOR_UNLOCK)) {
                performActionDoorUnlook();
            } else if (messageEvent.getPath().equals(WEAR_ACTION_FLASHLIGHTS)) {
                performActionFlashlights();
            } else if (messageEvent.getPath().equals(WEAR_ACTION_CHARGING_START)) {
                performActionChargingStart();
            } else if (messageEvent.getPath().equals(WEAR_ACTION_CHARGING_STOP)) {
                performActionChargingStop();
            } else if (messageEvent.getPath().equals(WEAR_ACTION_AUTO_CONDITIONING_START)) {
                performActionAutoConditionStart();
            } else if (messageEvent.getPath().equals(WEAR_ACTION_AUTO_CONDITIONING_STOP)) {
                performActionAutoConditionStop();
            } else if (messageEvent.getPath().equals(WEAR_SET_TEMPS)) {
                if (messageEvent.getData().length > 0) {
                    SetTempCommand command = (SetTempCommand) getDeserializedCarState(messageEvent.getData());
                    performSetTemps(command);
                }
            } else if (messageEvent.getPath().equals(WEAR_SET_MAX_CHARGING)) {
                if (messageEvent.getData().length > 0) {
                    SetChargeLimitCommand command = (SetChargeLimitCommand) getDeserializedCarState(messageEvent.getData());
                    performSetMaxCharging(command);
                }
            } else if(messageEvent.getPath().equals(WEAR_SET_MAX_CHARGING)) {
                performCloseSunRoof();
            }
        }

    private void performCloseSunRoof(){
        mApiService.commandSunRoofControl(CAR_ID, "close", new Callback<Result>() {
            @Override
            public void success(Result result, Response response) {
                if (result != null) {
                    Log.v(TAG, "Sunroof closed - " + result.isResult() + " reason - " + result.getReason());
                    Toast.makeText(MobileListenerService.this, "Sunroof closed...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                logNetworkingError(error);
            }
        });
    }

    private void performActionChargingStart() {
        mApiService.commandChargeStart(CAR_ID, new Callback<Result>() {
            @Override
            public void success(Result result, Response response) {
                if (result != null) {
                    Log.v(TAG, "ChargingStart - " + result.isResult() + " reason - " + result.getReason());
                    Toast.makeText(MobileListenerService.this, "Charging started...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                logNetworkingError(error);
            }
        });
    }

    private void performActionChargingStop() {
        mApiService.commandChargeStart(CAR_ID, new Callback<Result>() {
            @Override
            public void success(Result result, Response response) {
                if (result != null) {
                    Log.v(TAG, "ChargingStop - " + result.isResult() + " reason - " + result.getReason());
                    Toast.makeText(MobileListenerService.this, "Charging stopped...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                logNetworkingError(error);
            }
        });
    }

    private void performSetMaxCharging(SetChargeLimitCommand command) {
        mApiService.commandSetChargeLimit(CAR_ID, command.getChargeLimit(), new Callback<Result>() {
            @Override
            public void success(Result result, Response response) {
                if (result != null) {
                    Log.v(TAG, "SetChargeLimit - " + result.isResult() + " reason - " + result.getReason());
                    Toast.makeText(MobileListenerService.this, "Max charging set...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                logNetworkingError(error);
            }
        });
    }

    private void performSetTemps(SetTempCommand command) {
        mApiService.commandSetTemps(CAR_ID, command.getDriverTemp(), command.getPassengerTemp(), new Callback<Result>() {
            @Override
            public void success(Result result, Response response) {
                if (result != null) {
                    Log.v(TAG, "SetTemps - " + result.isResult() + " reason - " + result.getReason());
                    Toast.makeText(MobileListenerService.this, "Temp set...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                logNetworkingError(error);
            }
        });
    }

    private void performActionAutoConditionStop() {
        mApiService.commandAutoConditioningStop(CAR_ID, new Callback<Result>() {
            @Override
            public void success(Result result, Response response) {
                if (result != null) {
                    Log.v(TAG, "AutoConditioningStop - " + result.isResult() + " reason - " + result.getReason());
                    Toast.makeText(MobileListenerService.this, "Auto Condition stopped...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                logNetworkingError(error);
            }
        });
    }

    private void performActionAutoConditionStart() {
        mApiService.commandAutoConditioningStart(CAR_ID, new Callback<Result>() {
            @Override
            public void success(Result result, Response response) {
                if (result != null) {
                    Log.v(TAG, "ActionAutoConditionStart - " + result.isResult() + " reason - " + result.getReason());
                    Toast.makeText(MobileListenerService.this, "Auto Condition started...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                logNetworkingError(error);
            }
        });
    }

    private void performActionFlashlights() {
        mApiService.commandFlashLights(CAR_ID, new Callback<Result>() {
            @Override
            public void success(Result result, Response response) {
                if (result != null) {
                    Log.v(TAG, "ActionFlashlights - " + result.isResult() + " reason - " + result.getReason());
                    Toast.makeText(MobileListenerService.this, "Flash lighting...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                logNetworkingError(error);
            }
        });
    }

    private void performActionDoorUnlook() {
        mApiService.commandDoorUnlock(CAR_ID, new Callback<Result>() {
            @Override
            public void success(Result result, Response response) {
                if (result != null) {
                    Log.v(TAG, "ActionDoorUnlook - " + result.isResult() + " reason - " + result.getReason());
                    Toast.makeText(MobileListenerService.this, "Doors unlocked...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                logNetworkingError(error);
            }
        });
    }

    private void performActionDoorLock() {
        mApiService.commandDoorLock(CAR_ID, new Callback<Result>() {
            @Override
            public void success(Result result, Response response) {
                if (result != null) {
                    Log.v(TAG, "ActionDoorLock - " + result.isResult() + " reason - " + result.getReason());
                    Toast.makeText(MobileListenerService.this, "Doors locked...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                logNetworkingError(error);
            }
        });
    }

    private void performActionHorn() {
        mApiService.commandHonkHorn(CAR_ID, new Callback<Result>() {
            @Override
            public void success(Result result, Response response) {
                if (result != null) {
                    Log.v(TAG, "ActionHorn - " + result.isResult() + " reason - " + result.getReason());
                    Toast.makeText(MobileListenerService.this, "Honking...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                logNetworkingError(error);
            }
        });
    }

    private void getChargeState() {
        mApiService.getChargeStatus(CAR_ID, new Callback<ChargeState>() {
            @Override
            public void success(ChargeState chargeState, Response response) {
                updateState(chargeState, MOBILE_UPDATE_CHARGE_STATE);
            }

            @Override
            public void failure(RetrofitError error) {
                logNetworkingError(error);
            }
        });
    }

    private void getClimateState() {
        mApiService.getClimateState(CAR_ID, new Callback<ClimateState>() {
            @Override
            public void success(ClimateState climateState, Response response) {
                updateState(climateState, MOBILE_UPDATE_CLIMATE_STATE);
            }

            @Override
            public void failure(RetrofitError error) {
                logNetworkingError(error);
            }
        });
    }

    private void getDriveState() {
        mApiService.getDriveState(CAR_ID, new Callback<DriveState>() {
            @Override
            public void success(DriveState driveState, Response response) {
                updateState(driveState, MOBILE_UPDATE_DRIVE_STATE);
            }

            @Override
            public void failure(RetrofitError error) {
                logNetworkingError(error);
            }
        });
    }

    private void getLocationMap(){
        mApiService.getDriveState(CAR_ID, new Callback<DriveState>() {
            @Override
            public void success(final DriveState driveState, Response response) {
                updateState(driveState, MOBILE_UPDATE_DRIVE_STATE);
                (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String URL = "http://maps.google.com/maps/api/staticmap?center=" +
//                                String.valueOf(driveState.getLatitude()) + "," +
//                                String.valueOf(driveState.getLongitude()) +
                                33.794839 + "," +
                                -84.401593 +
                                "&zoom=15&size=320x320&sensor=false";
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpGet request = new HttpGet(URL);
                        Bitmap image;

                        InputStream in = null;
                        try {
                            in = httpclient.execute(request).getEntity().getContent();
                            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                            int nRead;
                            byte[] data = new byte[16384];

                            while ((nRead = in.read(data, 0, data.length)) != -1) {
                                buffer.write(data, 0, nRead);
                            }
                            buffer.flush();
                            mGoogleApiClient = getGoogleApiClient(MobileListenerService.this);
                            connectApiClient();
                            Wearable.MessageApi.sendMessage(mGoogleApiClient, mNodeId, MOBILE_UPDATE_LOCATION_MAP, buffer.toByteArray()).await();
                            mGoogleApiClient.disconnect();
                            in.close();

                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        } catch (ClientProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })).start();
            }

            @Override
            public void failure(RetrofitError error) {
                logNetworkingError(error);
            }
        });
    }

    private void getVehicleState() {
        mApiService.getVehicleState(CAR_ID, new Callback<VehicleState>() {
            @Override
            public void success(VehicleState vehicleState, Response response) {
                updateState(vehicleState, MOBILE_UPDATE_VEHICLE_STATE);
            }

            @Override
            public void failure(RetrofitError error) {
                logNetworkingError(error);
            }
        });
    }

    private void logNetworkingError(RetrofitError error) {
        Log.e(TAG, "Networking error " + error.getUrl());
    }

    private Object getDeserializedCarState(byte[] data) {
        try {
            return SerializationUtil.deserialize(data);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateState(final Object state, final String type) {
        mGoogleApiClient = getGoogleApiClient(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                connectApiClient();
                byte[] data = serializeState(state);
                Wearable.MessageApi.sendMessage(mGoogleApiClient, mNodeId, type, data).await();
                mGoogleApiClient.disconnect();
            }
        }).start();
    }

    private byte[] serializeState(Object state) {
        try {
            return SerializationUtil.serialize(state);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void connectApiClient() {
        if (isClientDisconnected()) {
            mGoogleApiClient.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
        }
    }

    private boolean isClientDisconnected() {
        return mGoogleApiClient != null && !(mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting());
    }

    private GoogleApiClient getGoogleApiClient(Context context) {
        return new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
    }
}