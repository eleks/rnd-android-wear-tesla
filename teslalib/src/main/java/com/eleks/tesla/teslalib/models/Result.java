package com.eleks.tesla.teslalib.models;

import java.io.Serializable;

/**
 * Created by maryan.melnychuk on 10.02.2015.
 */
public class Result implements Serializable{
    private String reason;
    private boolean result;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
