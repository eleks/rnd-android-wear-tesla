package com.eleks.tesla.teslalib.models;

import java.io.Serializable;

/**
 * Created by maryan.melnychuk on 10.02.2015.
 */
public class DriveState implements Serializable {
    private String shift_state;
    private Object speed;
    private float latitude;
    private float longitude;
    private int heading;
    private long gps_as_of;

    public String getShiftState() {
        return shift_state;
    }

    public void setShiftState(String shift_state) {
        this.shift_state = shift_state;
    }

    public Object getSpeed() {
        return speed;
    }

    public void setSpeed(Object speed) {
        this.speed = speed;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getHeading() {
        return heading;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public long getGpsAsOf() {
        return gps_as_of;
    }

    public void setGpsAsOf(long gpsAsOf) {
        this.gps_as_of = gpsAsOf;
    }
}
