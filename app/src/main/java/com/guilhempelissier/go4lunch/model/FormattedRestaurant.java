package com.guilhempelissier.go4lunch.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class FormattedRestaurant {
	private String id;
	private String name;
	private String address;
	private String openNow;
	private String distanceMeters;
	private int distance;
	private int stars;
	private String imageUrl;
	private LatLng latLng;
	private List<String> workmates;
	private boolean isMyLunch;
	private boolean isLikedByCurrentUser;
	private String phoneNumber;
	private String website;

	public FormattedRestaurant(String id, String name, String address, String openNow, String distanceMeters, int distance, int stars, String imageUrl, LatLng latLng, List<String> workmates, boolean isMyLunch, boolean isLikedByCurrentUser, String phoneNumber, String website) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.openNow = openNow;
		this.distance = distance;
		this.distanceMeters = distanceMeters;
		this.stars = stars;
		this.imageUrl = imageUrl;
		this.latLng = latLng;
		this.workmates = workmates;
		this.isMyLunch = isMyLunch;
		this.isLikedByCurrentUser = isLikedByCurrentUser;
		this.phoneNumber = phoneNumber;
		this.website = website;
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

	public String getDistanceMeters() {
		return distanceMeters;
	}

	public void setDistanceMeters(String distanceMeters) {
		this.distanceMeters = distanceMeters;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
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

	public boolean isMyLunch() {
		return isMyLunch;
	}

	public void setMyLunch(boolean myLunch) {
		isMyLunch = myLunch;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public boolean isLikedByCurrentUser() {
		return isLikedByCurrentUser;
	}

	public void setLikedByCurrentUser(boolean likedByCurrentUser) {
		isLikedByCurrentUser = likedByCurrentUser;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
}
