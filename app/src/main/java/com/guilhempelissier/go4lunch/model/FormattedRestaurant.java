package com.guilhempelissier.go4lunch.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class FormattedRestaurant {
	private String id;
	private String name;
	private String address;
	private String openNow;
	private String distance;
	private String stars;
	private String imageUrl;
	private LatLng latLng;
	private List<String> workmates;

	public FormattedRestaurant(String id, String name, String address, String openNow, String distance, String stars, String imageUrl, LatLng latLng, List<String> workmates) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.openNow = openNow;
		this.distance = distance;
		this.stars = stars;
		this.imageUrl = imageUrl;
		this.latLng = latLng;
		this.workmates = workmates;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOpenNow() {
		return openNow;
	}

	public void setOpenNow(String openNow) {
		this.openNow = openNow;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getStars() {
		return stars;
	}

	public void setStars(String stars) {
		this.stars = stars;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public LatLng getLatLng() {
		return latLng;
	}

	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}

	public List<String> getWorkmates() {
		return workmates;
	}

	public void setWorkmates(List<String> workmates) {
		this.workmates = workmates;
	}
}
