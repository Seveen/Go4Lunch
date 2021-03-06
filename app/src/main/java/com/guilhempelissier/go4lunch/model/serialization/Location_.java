
package com.guilhempelissier.go4lunch.model.serialization;

import com.google.gson.annotations.Expose;

public class Location_ {

    @Expose
    private Double lat;
    @Expose
    private Double lng;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Location_(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
