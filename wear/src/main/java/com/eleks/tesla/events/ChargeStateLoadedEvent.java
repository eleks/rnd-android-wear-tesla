package com.eleks.tesla.events;

import com.eleks.tesla.teslalib.models.ChargeState;

/**
 * Created by maryan.melnychuk on 19.02.2015.
 */
public class ChargeStateLoadedEvent {
    private ChargeState mChargeState;

    public ChargeStateLoadedEvent(ChargeState chargeState) {
        this.mChargeState = chargeState;
    }

    public ChargeState getChargeState() {
        return mChargeState;
    }

    public void setChargeState(ChargeState chargeState) {
        this.mChargeState = chargeState;
    }
}
