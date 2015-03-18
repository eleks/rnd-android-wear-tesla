package com.eleks.tesla.teslalib;

/**
 * Created by maryan.melnychuk on 19.02.2015.
 */
public interface ApiPathConstants {
    /* Wear commands */
    public static final String WEAR_GET_CAR_CONFIG = "/getCarConfig";
    public static final String WEAR_GET_CHARGE_STATE = "/getChargeState";
    public static final String WEAR_GET_CLIMATE_STATE = "/getClimateState";
    public static final String WEAR_GET_DRIVE_STATE = "/getDriveState";
    public static final String WEAR_GET_VEHICLE_STATE = "/getVehicleState";
    public static final String WEAR_GET_LOCATION_MAP = "/getLocationMap";
    public static final String WEAR_ACTION_HORN = "/actionHorn";
    public static final String WEAR_ACTION_DOOR_LOCK = "/actionDoorLock";
    public static final String WEAR_ACTION_DOOR_UNLOCK = "/actionDoorUnlock";
    public static final String WEAR_ACTION_CHARGING_START = "/actionChargingStart";
    public static final String WEAR_ACTION_CHARGING_STOP = "/actionChargingStop";
    public static final String WEAR_ACTION_FLASHLIGHTS = "/actionFlashlights";
    public static final String WEAR_ACTION_AUTO_CONDITIONING_START = "/actionAutoConditioningStart";
    public static final String WEAR_ACTION_AUTO_CONDITIONING_STOP = "/actionAutoConditioningStop";
    public static final String WEAR_SET_TEMPS = "/setTemps";
    public static final String WEAR_SET_MAX_CHARGING = "/setMaxCharging";
    public static final String WEAR_CLOSE_SUNROOF = "/closeSunRoof";

    /* Mobiles commands */
    public static final String MOBILE_UPDATE_CAR_CONFIG = "/updateCarConfig";
    public static final String MOBILE_UPDATE_CHARGE_STATE = "/updateChargeState";
    public static final String MOBILE_UPDATE_CLIMATE_STATE= "/updateClimateState";
    public static final String MOBILE_UPDATE_DRIVE_STATE= "/updateDriveState";
    public static final String MOBILE_UPDATE_VEHICLE_STATE = "/updateVehicleState";
    public static final String MOBILE_UPDATE_LOCATION_MAP = "/updateLocationMap";
    public static final String MOBILE_SHOW_NOTIFICATION_BAD_WEATHER = "/notificationBadWeather";
    public static final String MOBILE_SHOW_NOTIFICATION_UNEXPECTED_STOP_CHARGING = "/notificationUnexpectedStopCharge";
    public static final String MOBILE_SHOW_NOTIFICATION_CAR_UNLOCKED = "/notificationCarUnlocked";
}
