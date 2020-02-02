package com.guilhempelissier.go4lunch.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Objects;

public class FormattedRestaurant {
	private String id;
	private String name;
	private String address;
	private String openNow;
	private String distanceMeters;
	private long distance;
	private int stars;
	private String imageUrl;
	private LatLng latLng;
	private List<String> workmates;
	private boolean isMyLunch;
	private boolean isLikedByCurrentUser;
	private String phoneNumber;
	private String website;

	public FormattedRestaurant(String id, String name, String address, String openNow, String distanceMeters, long distance, int stars, String imageUrl, LatLng latLng, List<String> workmates, boolean isMyLunch, boolean isLikedByCurrentUser, String phoneNumber, String website) {
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

	public long getDistance() {
		return distance;
	}

	public void setDistance(long distance) {
		this.distance = distance;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FormattedRestaurant that = (FormattedRestaurant) o;
		return distance == that.distance &&
				stars == that.stars &&
				isMyLunch == that.isMyLunch &&
				isLikedByCurrentUser == that.isLikedByCurrentUser &&
				Objects.equals(id, that.id) &&
				Objects.equals(name, that.name) &&
				Objects.equals(address, that.address) &&
				Objects.equals(openNow, that.openNow) &&
				Objects.equals(distanceMeters, that.distanceMeters) &&
				Objects.equals(imageUrl, that.imageUrl) &&
				Objects.equals(latLng, that.latLng) &&
				Objects.equals(workmates, that.workmates) &&
				Objects.equals(phoneNumber, that.phoneNumber) &&
				Objects.equals(website, that.website);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, address, openNow, distanceMeters, distance, stars, imageUrl, latLng, workmates, isMyLunch, isLikedByCurrentUser, phoneNumber, website);
	}
}
