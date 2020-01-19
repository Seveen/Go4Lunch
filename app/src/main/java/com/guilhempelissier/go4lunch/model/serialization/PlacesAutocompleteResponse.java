package com.guilhempelissier.go4lunch.model.serialization;

import com.google.gson.annotations.Expose;

import java.util.List;

public class PlacesAutocompleteResponse {
	@Expose
	private String status;
	@Expose
	private List<AutocompleteResult> predictions;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<AutocompleteResult> getPredictions() {
		return predictions;
	}

	public void setPredictions(List<AutocompleteResult> predictions) {
		this.predictions = predictions;
	}
}
