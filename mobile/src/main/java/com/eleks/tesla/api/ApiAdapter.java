package com.eleks.tesla.api;

import com.eleks.tesla.teslalib.models.ChargeState;
import com.eleks.tesla.teslalib.models.ClimateState;
import com.eleks.tesla.teslalib.models.DriveState;
import com.eleks.tesla.teslalib.models.GuiSettings;
import com.eleks.tesla.teslalib.models.Result;
import com.eleks.tesla.teslalib.models.Vehicle;
import com.eleks.tesla.teslalib.models.VehicleState;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by maryan.melnychuk on 09.02.2015.
 */
public interface ApiAdapter {
    @GET("/vehicles")
    void getVehicles(Callback<List<Vehicle>> callback);

    //Sates ----------------------------------------------------------------------------------------
    @GET("/vehicles/{id}/mobile_enabled")
    void getVehicleStatus(@Path("id") String carId,
                          Callback<Result> callback);

    @GET("/vehicles/{id}/command/charge_state")
    void getChargeStatus(@Path("id") String carId,
                         Callback<ChargeState> callback);

    @GET("/vehicles/{id}/command/climate_state")
    void getClimateState(@Path("id") String carId,
                         Callback<ClimateState> callback);

    @GET("/vehicles/{id}/command/drive_state")
    void getDriveState(@Path("id") String carId,
                       Callback<DriveState> callback);

    @GET("/vehicles/{id}/command/gui_settings")
    void getGuiSettings(@Path("id") String carId,
                        Callback<GuiSettings> callback);

    @GET("/vehicles/{id}/command/vehicle_state")
    void getVehicleState(@Path("id") String carId,
                         Callback<VehicleState> callback);

    //Commands -------------------------------------------------------------------------------------
    @GET("/vehicles/{id}/command/charge_port_door_open")
    void commandOpenChargePortDoor(@Path("id") String carId,
                                   Callback<Result> callback);

    @GET("/vehicles/{id}/command/charge_standard")
    void commandChargeStandart(@Path("id") String carId,
                               Callback<Result> callback);

    @GET("/vehicles/{id}/command/charge_max_range")
    void commandChargeMaxRange(@Path("id") String carId,
                               Callback<Result> callback);

    @GET("/vehicles/{id}/command/set_charge_limit")
    void commandSetChargeLimit(@Path("id") String carId,
                               @Query("percent") int percent,
                               Callback<Result> callback);

    @GET("/vehicles/{id}/command/charge_start")
    void commandChargeStart(@Path("id") String carId,
                            Callback<Result> callback);

    @GET("/vehicles/{id}/command/charge_stop")
    void commandChargeStop(@Path("id") String carId,
                           Callback<Result> callback);

    @GET("/vehicles/{id}/command/flash_lights")
    void commandFlashLights(@Path("id") String carId,
                            Callback<Result> callback);

    @GET("/vehicles/{id}/command/honk_horn")
    void commandHonkHorn(@Path("id") String carId,
                         Callback<Result> callback);

    @GET("/vehicles/{id}/command/door_lock")
    void commandDoorLock(@Path("id") String carId,
                         Callback<Result> callback);

    @GET("/vehicles/{id}/command/door_unlock")
    void commandDoorUnlock(@Path("id") String carId,
                           Callback<Result> callback);

    @GET("/vehicles/{id}/command/set_temps")
    void commandSetTemps(@Path("id") String carId,
                         @Query("driver_temp") String driverTemp,
                         @Query("passenger_temp") String passengerTemp,
                         Callback<Result> callback);

    @GET("/vehicles/{id}/command/auto_conditioning_start")
    void commandAutoConditioningStart(@Path("id") String carId,
                                      Callback<Result> callback);

    @GET("/vehicles/{id}/command/auto_conditioning_stop")
    void commandAutoConditioningStop(@Path("id") String carId,
                                     Callback<Result> callback);

    @GET("/vehicles/{id}/command/sun_roof_control?state={state}")
    void commandSunRoofControl(@Path("id") String carId,
                               @Query("state") String state, //open = 100%, close = 0%, comfort = 80%, and vent = ~15%
                               Callback<Result> callback);
}
