package com.eleks.tesla.teslalib.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by maryan.melnychuk on 09.02.2015.
 */
public class Vehicle implements Serializable {
    private String color;
    private String display_name;
    private int id;
    private String option_codes;
    private int user_id;
    private String vehicle_id;
    private String vin;
    private List<String> tokens;
    private String state;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOption_codes() {
        return option_codes;
    }

    public void setOption_codes(String option_codes) {
        this.option_codes = option_codes;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
