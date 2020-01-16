
package com.guilhempelissier.go4lunch.model.serialization;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
}
