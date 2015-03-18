package com.eleks.tesla.mainApp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eleks.tesla.R;
import com.eleks.tesla.events.ChargeStateLoadedEvent;
import com.eleks.tesla.events.ClimateStateLoadedEvent;
import com.eleks.tesla.events.DriveStateLoadedEvent;
import com.eleks.tesla.events.ToHandHoldRequestEvent;
import com.eleks.tesla.events.VehicleStateLoadedEvent;

import de.greenrobot.event.EventBus;

import static com.eleks.tesla.teslalib.ApiPathConstants.*;

/**
 * Created by maryan.melnychuk on 20.02.2015.
 */
public class StatusTeslaFragment extends BaseTeslaFragment {
    private boolean isGetChargeRunning, isGetClimateRunning, isGetDriveRunning, isGetVehicleRunning;

    private ProgressBar mLoadingBar;
    private TextView mLoadingText;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    private void loadData() {
        EventBus.getDefault().post(new ToHandHoldRequestEvent(WEAR_GET_CHARGE_STATE));
        isGetChargeRunning = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_status, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLoadingBar = (ProgressBar) view.findViewById(R.id.loading_bar);
        mLoadingText = (TextView) view.findViewById(R.id.loading_status);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }

    public void onEventMainThread(ChargeStateLoadedEvent event) {
        isGetChargeRunning = false;
        EventBus.getDefault().post(new ToHandHoldRequestEvent(WEAR_GET_CLIMATE_STATE));
        isGetClimateRunning = true;
    }

    public void onEventMainThread(ClimateStateLoadedEvent event) {
        isGetClimateRunning = false;
        EventBus.getDefault().post(new ToHandHoldRequestEvent(WEAR_GET_DRIVE_STATE));
        isGetDriveRunning = true;
    }

    public void onEventMainThread(DriveStateLoadedEvent event) {
        isGetDriveRunning = false;
        EventBus.getDefault().post(new ToHandHoldRequestEvent(WEAR_GET_VEHICLE_STATE));
        isGetVehicleRunning = true;
    }

    public void onEventMainThread(VehicleStateLoadedEvent event) {
        isGetVehicleRunning = false;
        showDataLoaded();
    }

    private void showDataLoaded() {
        if (hasntRunningRequests()) {
            mLoadingBar.setVisibility(View.INVISIBLE);
            mLoadingText.setText(getString(R.string.data_loaded));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    private boolean hasntRunningRequests() {
        return !isGetChargeRunning && !isGetVehicleRunning && !isGetDriveRunning && !isGetClimateRunning;
    }
}
