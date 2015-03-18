package com.eleks.tesla.events;

import android.graphics.Bitmap;

/**
 * Created by maryan.melnychuk on 06.03.2015.
 */
public class LocationMapLoadedEvent {
    private Bitmap mMapImage;

    public LocationMapLoadedEvent(Bitmap mapImage) {
        this.mMapImage = mapImage;
    }

    public Bitmap getMapImage() {
        return mMapImage;
    }
}
