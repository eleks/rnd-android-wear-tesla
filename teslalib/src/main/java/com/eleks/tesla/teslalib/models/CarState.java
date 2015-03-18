package com.eleks.tesla.teslalib.models;

import java.io.Serializable;

/**
 * Created by Ihor.Demedyuk on 11.02.2015.
 */
public class CarState {
    public static final String CHARGING_STATE = "Charging";

    private float mDistance;
    private boolean mIsLocked;
    private boolean mIsCharging;
    private int mBatteryCharge;  // [0:100]

    public boolean isLocked() {
        return mIsLocked;
    }

    public void setIsLocked(boolean isLocked) {
        this.mIsLocked = isLocked;
    }

    public int getBatteryCharge() {
        return mBatteryCharge;
    }

    public void setBatteryCharge(int batteryCharge) {
        this.mBatteryCharge = batteryCharge;
    }

    public boolean isIsCharging() {
        return mIsCharging;
    }

    public void setIsCharging(boolean mIsCharging) {
        this.mIsCharging = mIsCharging;
    }

    public float getDistance() {
        return mDistance;
    }

    public void setDistance(float distance) {
        this.mDistance = distance;
    }

    public void updateCharge(ChargeState chargeState){
        mDistance = chargeState.getEstBatteryRange();
        mIsCharging = chargeState.getChargingState().equalsIgnoreCase(CHARGING_STATE);
        mBatteryCharge = chargeState.getBatteryLevel();

    }

    public void updateVehicleState(VehicleState vehicleState){
        mIsLocked = vehicleState.isLocked();
    }
}
