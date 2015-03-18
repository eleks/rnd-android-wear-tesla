package com.eleks.tesla.events;

import java.io.Serializable;

/**
 * Created by Ihor.Demedyuk on 04.03.2015.
 */
public class ToHandHoldRequestEvent {
    private String mPath;
    private Serializable mCommand;

    public ToHandHoldRequestEvent(String path){
        mPath = path;
    }

    public ToHandHoldRequestEvent(String path, Serializable command){
        mPath = path;
        mCommand = command;
    }

    public String getPath() {
        return mPath;
    }

    public Serializable getCommand() {
        return mCommand;
    }
}
