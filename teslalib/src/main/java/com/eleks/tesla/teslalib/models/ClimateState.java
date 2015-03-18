package com.eleks.tesla.teslalib.models;

import java.io.Serializable;

/**
 * Created by maryan.melnychuk on 10.02.2015.
 */
public class ClimateState implements Serializable {
    private float inside_temp;
    private float outside_temp;
    private float driver_temp_setting;
    private float passenger_temp_setting;
    private boolean is_auto_conditioning_on;
    private Boolean is_front_defroster_on;
    private boolean s_rear_defroster_on;
    private int fan_status;

    public float getInsideTemp() {
        return inside_temp;
    }

    public void setInsideTemp(float insideTemp) {
        this.inside_temp = insideTemp;
    }

    public float getOutsideTemp() {
        return outside_temp;
    }

    public void setOutsideTemp(float outsideTemp) {
        this.outside_temp = outsideTemp;
    }

    public float getDriverTempSetting() {
        return driver_temp_setting;
    }

    public void setDriverTempSetting(float driverTempSetting) {
        this.driver_temp_setting = driverTempSetting;
    }

    public float getPassengerTempSetting() {
        return passenger_temp_setting;
    }

    public void setPassengerTempSetting(float passengerTempSetting) {
        this.passenger_temp_setting = passengerTempSetting;
    }

    public boolean isIsAutoConditioningOn() {
        return is_auto_conditioning_on;
    }

    public void setIsAutoConditioningOn(boolean isAutoConditioningOn) {
        this.is_auto_conditioning_on = isAutoConditioningOn;
    }

    public Boolean getIsFrontDefrosterOn() {
        return is_front_defroster_on;
    }

    public void setIsFrontDefrosterOn(Boolean isFrontDefrosterOn) {
        this.is_front_defroster_on = isFrontDefrosterOn;
    }

    public boolean isSRearDefrosterOn() {
        return s_rear_defroster_on;
    }

    public void setSRearDefrosterOn(boolean sRearDefrosterOn) {
        this.s_rear_defroster_on = sRearDefrosterOn;
    }

    public int getFanStatus() {
        return fan_status;
    }

    public void setFanStatus(int fanStatus) {
        this.fan_status = fanStatus;
    }
}
