package com.eleks.tesla.teslalib.models;

import java.io.Serializable;

/**
 * Created by maryan.melnychuk on 10.02.2015.
 */
public class GuiSettings implements Serializable {
    private String gui_distance_units;
    private String gui_temperature_units;
    private String gui_charge_rate_units;
    private boolean gui_24_hour_time;
    private String gui_range_display;

    public String getGuiDistanceUnits() {
        return gui_distance_units;
    }

    public void setGuiDistanceUnits(String guiDistanceUnits) {
        this.gui_distance_units = guiDistanceUnits;
    }

    public String getGuiTemperatureUnits() {
        return gui_temperature_units;
    }

    public void setGuiTemperatureUnits(String guiTemperatureUnits) {
        this.gui_temperature_units = guiTemperatureUnits;
    }

    public String getGuiChargeRateUnits() {
        return gui_charge_rate_units;
    }

    public void setGuiChargeRateUnits(String guiChargeRateUnits) {
        this.gui_charge_rate_units = guiChargeRateUnits;
    }

    public boolean isGui24HourTime() {
        return gui_24_hour_time;
    }

    public void setGui24HourTime(boolean gui24HourTime) {
        this.gui_24_hour_time = gui24HourTime;
    }

    public String getGuiRangeDisplay() {
        return gui_range_display;
    }

    public void setGuiRangeDisplay(String guiRangeDisplay) {
        this.gui_range_display = guiRangeDisplay;
    }
}
