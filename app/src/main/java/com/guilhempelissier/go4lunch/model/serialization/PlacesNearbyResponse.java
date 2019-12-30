
package com.guilhempelissier.go4lunch.model.serialization;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlacesNearbyResponse {

    @SerializedName("html_attributions")
    private List<Object> htmlAttributions;
    @Expose
    private List<NearbyResult> results;
    @Expose
    private String status;

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public List<NearbyResult> getResults() {
        return results;
    }

    public void setResults(List<NearbyResult> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
