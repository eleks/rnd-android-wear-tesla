package com.eleks.tesla.events;

import com.eleks.tesla.teslalib.models.VehicleState;

/**
 * Created by maryan.melnychuk on 19.02.2015.
 */
public class VehicleStateLoadedEvent {
    private VehicleState mVehicleState;

    public VehicleStateLoadedEvent(VehicleState vehicleState) {
        this.mVehicleState = vehicleState;
    }

    public VehicleState getVehicleState() {
        return mVehicleState;
    }

    public void setVehicleState(VehicleState vehicleState) {
        this.mVehicleState = vehicleState;
    }
}
