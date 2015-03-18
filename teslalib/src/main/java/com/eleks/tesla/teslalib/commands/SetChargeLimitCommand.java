package com.eleks.tesla.teslalib.commands;

import java.io.Serializable;

/**
 * Created by maryan.melnychuk on 20.02.2015.
 */
public class SetChargeLimitCommand implements Serializable {
    private int mChargeLimit;

    public SetChargeLimitCommand(int chargeLimit) {
        this.mChargeLimit = chargeLimit;
    }

    public int getChargeLimit() {
        return mChargeLimit;
    }

    public void setChargeLimit(int chargeLimit) {
        this.mChargeLimit = chargeLimit;
    }
}
