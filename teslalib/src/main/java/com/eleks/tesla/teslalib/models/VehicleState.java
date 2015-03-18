package com.eleks.tesla.teslalib.models;

import java.io.Serializable;

/**
 * Created by maryan.melnychuk on 10.02.2015.
 */
public class VehicleState implements Serializable {
    private boolean df;
    private boolean dr;
    private boolean pf;
    private boolean pr;
    private boolean ft;
    private boolean rt;
    private String car_verson;
    private boolean locked;
    private boolean sun_roof_installed;
    private String sun_roof_state;
    private int sun_roof_percent_open;
    private boolean dark_rims;
    private String wheel_type;
    private boolean has_spoiler;
    private String roof_color;
    private boolean perf_config;

    public boolean isDriverFrontDoor() {
        return df;
    }

    public void setDriverFrontDoor(boolean df) {
        this.df = df;
    }

    public boolean isDriverRearDoor() {
        return dr;
    }

    public void setDriverRearDoor(boolean dr) {
        this.dr = dr;
    }

    public boolean isPassengerFrontDoor() {
        return pf;
    }

    public void setPassengerFrontDoor(boolean pf) {
        this.pf = pf;
    }

    public boolean isPassengerRearDoor() {
        return pr;
    }

    public void setPassengerRearDoor(boolean pr) {
        this.pr = pr;
    }

    public boolean isFrontTrunk() {
        return ft;
    }

    public void setFrontTrunk(boolean ft) {
        this.ft = ft;
    }

    public boolean isRearTrunk() {
        return rt;
    }

    public void setRearTrunk(boolean rt) {
        this.rt = rt;
    }

    public String getCarVerson() {
        return car_verson;
    }

    public void setCarVerson(String carVerson) {
        this.car_verson = carVerson;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isSunRoofInstalled() {
        return sun_roof_installed;
    }

    public void setSunRoofInstalled(boolean sunRoofInstalled) {
        this.sun_roof_installed = sunRoofInstalled;
    }

    public String getSunRoofState() {
        return sun_roof_state;
    }

    public void setSunRoofState(String sunRoofState) {
        this.sun_roof_state = sunRoofState;
    }

    public int getSunRoofPercentOpen() {
        return sun_roof_percent_open;
    }

    public void setSunRoofPercentOpen(int sunRoofPercentOpen) {
        this.sun_roof_percent_open = sunRoofPercentOpen;
    }

    public boolean isDarkRims() {
        return dark_rims;
    }

    public void setDarkRims(boolean darkRims) {
        this.dark_rims = darkRims;
    }

    public String getWheelType() {
        return wheel_type;
    }

    public void setWheelType(String wheelType) {
        this.wheel_type = wheelType;
    }

    public boolean isHasSpoiler() {
        return has_spoiler;
    }

    public void setHasSpoiler(boolean hasSpoiler) {
        this.has_spoiler = hasSpoiler;
    }

    public String getRoofColor() {
        return roof_color;
    }

    public void setRoofColor(String roofColor) {
        this.roof_color = roofColor;
    }

    public boolean isPerfConfig() {
        return perf_config;
    }

    public void setPerfConfig(boolean perfConfig) {
        this.perf_config = perfConfig;
    }
}
