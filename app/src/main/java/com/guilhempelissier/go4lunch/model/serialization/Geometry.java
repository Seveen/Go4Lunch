
package com.guilhempelissier.go4lunch.model.serialization;

import com.google.gson.annotations.Expose;

public class Geometry {

    @Expose
    private Location_ location;

    public Location_ getLocation() {
        return location;
    }

    public void setLocation(Location_ location) {
        this.location = location;
    }

}
