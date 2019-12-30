
package com.guilhempelissier.go4lunch.model.serialization;

import com.google.gson.annotations.Expose;

public class GeometryWithViewport {

    @Expose
    private Location_ location;
    @Expose
    private Viewport viewport;

    public Location_ getLocation() {
        return location;
    }

    public void setLocation(Location_ location) {
        this.location = location;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }
}
