package com.eleks.tesla.teslalib.commands;

import java.io.Serializable;

/**
 * Created by maryan.melnychuk on 20.02.2015.
 */
public class SetTempCommand implements Serializable {
    private String mDriverTemp, mPassengerTemp;

    public SetTempCommand(String driverTemp, String passengerTemp) {
        this.mDriverTemp = driverTemp;
        this.mPassengerTemp = passengerTemp;
    }

    public String getDriverTemp() {
        return mDriverTemp;
    }

    public void setDriverTemp(String driverTemp) {
        this.mDriverTemp = driverTemp;
    }

    public String getPassengerTemp() {
        return mPassengerTemp;
    }

    public void setPassengerTemp(String passengerTemp) {
        this.mPassengerTemp = passengerTemp;
    }
}
