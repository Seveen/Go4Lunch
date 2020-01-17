package com.guilhempelissier.go4lunch.model;

public class FormattedWorkmate {
	private String name;
	private String eatingPlaceId;
	private String eatingPlace;
	private String imageUrl;

	public FormattedWorkmate(String name, String eatingPlaceId, String eatingPlace, String imageUrl) {
		this.name = name;
		this.eatingPlaceId = eatingPlaceId;
		this.eatingPlace = eatingPlace;
		this.imageUrl = imageUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getEatingPlace() {
		return eatingPlace;
	}

	public void setEatingPlace(String eatingPlace) {
		this.eatingPlace = eatingPlace;
	}

	public String getEatingPlaceId() {
		return eatingPlaceId;
	}

	public void setEatingPlaceId(String eatingPlaceId) {
		this.eatingPlaceId = eatingPlaceId;
	}
}
