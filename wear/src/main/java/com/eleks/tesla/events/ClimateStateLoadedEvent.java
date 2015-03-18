package com.eleks.tesla.events;

import com.eleks.tesla.teslalib.models.ClimateState;

/**
 * Created by maryan.melnychuk on 19.02.2015.
 */
public class ClimateStateLoadedEvent {
    private ClimateState mClimateState;

    public ClimateStateLoadedEvent(ClimateState climateState) {
        this.mClimateState = climateState;
    }

    public ClimateState getClimateState() {
        return mClimateState;
    }

    public void setClimateState(ClimateState climateState) {
        this.mClimateState = climateState;
    }
}
