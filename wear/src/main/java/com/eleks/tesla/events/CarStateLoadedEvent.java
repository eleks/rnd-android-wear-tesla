package com.eleks.tesla.events;


import com.eleks.tesla.teslalib.models.CarState;

/**
 * Created by Ihor.Demedyuk on 13.02.2015.
 */
public class CarStateLoadedEvent {
    private CarState mCarState;

    public CarStateLoadedEvent(CarState carState){
        mCarState = carState;
    }

    public CarState getCarState() {
        return mCarState;
    }

}
