
package com.guilhempelissier.go4lunch.model.serialization;

import com.google.gson.annotations.Expose;

public class Viewport {

    @Expose
    private Location_ northeast;
    @Expose
    private Location_ southwest;

    public Location_ getNortheast() {
        return northeast;
    }

    public void setNortheast(Location_ northeast) {
        this.northeast = northeast;
    }

    public Location_ getSouthwest() {
        return southwest;
    }

    public void setSouthwest(Location_ southwest) {
        this.southwest = southwest;
    }
}
