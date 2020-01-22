
package com.guilhempelissier.go4lunch.model.serialization;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailsResult {

    @SerializedName("formatted_phone_number")
    private String formattedPhoneNumber;
    @Expose
    private String id;
    @Expose
    private String reference;
    @Expose
    private String website;
    @SerializedName("opening_hours")
    private OpeningHours openingHours;
    @Expose
    private Geometry geometry;
    @Expose
    private String name;
    @Expose
    private List<Photo> photos;
    @Expose
    private String vicinity;
    @Expose
    private Double rating;

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public String getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public String getWebsite() {
        return website;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
