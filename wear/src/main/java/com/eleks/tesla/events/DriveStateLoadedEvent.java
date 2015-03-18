package com.eleks.tesla.events;

import com.eleks.tesla.teslalib.models.DriveState;

/**
 * Created by maryan.melnychuk on 19.02.2015.
 */
public class DriveStateLoadedEvent {
    private DriveState mDriveState;

    public DriveStateLoadedEvent(DriveState driveState) {
        this.mDriveState = driveState;
    }

    public DriveState getDriveState() {
        return mDriveState;
    }

    public void setDriveState(DriveState driveState) {
        this.mDriveState = driveState;
    }
}
