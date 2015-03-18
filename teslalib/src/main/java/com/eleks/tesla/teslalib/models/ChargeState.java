package com.eleks.tesla.teslalib.models;

import java.io.Serializable;

/**
 * Created by maryan.melnychuk on 10.02.2015.
 */
public class ChargeState implements Serializable {
    public static final String STATE_CHARGING = "Charging";
    public static final String STATE_COMPLETE = "Complete";

    private String charging_state;
    private boolean charge_to_max_range;
    private int max_range_charge_counter;
    private boolean fast_charger_present;
    private float battery_range;
    private float est_battery_range;
    private float ideal_battery_range;
    private int battery_level;
    private float battery_current;
    private String charge_starting_range;
    private String charge_starting_soc;
    private int charger_voltage;
    private int charger_pilot_current;
    private int charger_actual_current;
    private int charger_power;
    private String time_to_full_charge;
    private float charge_rate;
    private boolean charge_port_door_open;

    public String getChargingState() {
        return charging_state;
    }

    public void setChargingState(String chargingState) {
        this.charging_state = chargingState;
    }

    public boolean isChargeToMaxRange() {
        return charge_to_max_range;
    }

    public void setChargeToMaxRange(boolean chargeToMaxRange) {
        this.charge_to_max_range = chargeToMaxRange;
    }

    public int getMaxRangeChargeCounter() {
        return max_range_charge_counter;
    }

    public void setMaxRangeChargeCounter(int maxRangeChargeCounter) {
        this.max_range_charge_counter = maxRangeChargeCounter;
    }

    public boolean isFastChargerPresent() {
        return fast_charger_present;
    }

    public void setFastChargerPresent(boolean fastChargerPresent) {
        this.fast_charger_present = fastChargerPresent;
    }

    public float getBatteryRange() {
        return battery_range;
    }

    public void setBatteryRange(float batteryRange) {
        this.battery_range = batteryRange;
    }

    public float getEstBatteryRange() {
        return est_battery_range;
    }

    public void setEstBatteryRange(float estBatteryRange) {
        this.est_battery_range = estBatteryRange;
    }

    public float getIdealBatteryRange() {
        return ideal_battery_range;
    }

    public void setIdealBatteryRange(float idealBatteryRange) {
        this.ideal_battery_range = idealBatteryRange;
    }

    public int getBatteryLevel() {
        return battery_level;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.battery_level = batteryLevel;
    }

    public float getBatteryCurrent() {
        return battery_current;
    }

    public void setBatteryCurrent(float batteryCurrent) {
        this.battery_current = batteryCurrent;
    }

    public String getChargeStartingRange() {
        return charge_starting_range;
    }

    public void setChargeStartingRange(String chargeStartingRange) {
        this.charge_starting_range = chargeStartingRange;
    }

    public String getChargeStartingSoc() {
        return charge_starting_soc;
    }

    public void setChargeStartingSoc(String chargeStartingSoc) {
        this.charge_starting_soc = chargeStartingSoc;
    }

    public int getChargerVoltage() {
        return charger_voltage;
    }

    public void setChargerVoltage(int chargerVoltage) {
        this.charger_voltage = chargerVoltage;
    }

    public int getChargerPilotCurrent() {
        return charger_pilot_current;
    }

    public void setChargerPilotCurrent(int chargerPilotCurrent) {
        this.charger_pilot_current = chargerPilotCurrent;
    }

    public int getChargerActualCurrent() {
        return charger_actual_current;
    }

    public void setChargerActualCurrent(int chargerActualCurrent) {
        this.charger_actual_current = chargerActualCurrent;
    }

    public int getChargerPower() {
        return charger_power;
    }

    public void setChargerPower(int chargerPower) {
        this.charger_power = chargerPower;
    }

    public String getTimeToFullCharge() {
        return time_to_full_charge;
    }

    public void setTimeToFullCharge(String timeToFullCharge) {
        this.time_to_full_charge = timeToFullCharge;
    }

    public float getChargeRate() {
        return charge_rate;
    }

    public void setChargeRate(float chargeRate) {
        this.charge_rate = chargeRate;
    }

    public boolean isChargePortDoorOpen() {
        return charge_port_door_open;
    }

    public void setChargePortDoorOpen(boolean chargePortDoorOpen) {
        this.charge_port_door_open = chargePortDoorOpen;
    }
}
