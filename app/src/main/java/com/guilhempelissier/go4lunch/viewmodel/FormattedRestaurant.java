package com.guilhempelissier.go4lunch.viewmodel;

public class FormattedRestaurant {
	private String id;
	private String name;
	private String address;
	private String openingTime;
	private String distance;
	private String nbWorkmates;
	private String stars;
	private String imageUrl;

	public FormattedRestaurant(String name, String address, String openingTime, String distance, String nbWorkmates, String stars, String imageUrl, String id) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.openingTime = openingTime;
		this.distance = distance;
		this.nbWorkmates = nbWorkmates;
		this.stars = stars;
		this.imageUrl = imageUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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

	public String getOpeningTime() {
		return openingTime;
	}

	public void setOpeningTime(String openingTime) {
		this.openingTime = openingTime;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getNbWorkmates() {
		return nbWorkmates;
	}

	public void setNbWorkmates(String nbWorkmates) {
		this.nbWorkmates = nbWorkmates;
	}

	public String getStars() {
		return stars;
	}

	public void setStars(String stars) {
		this.stars = stars;
	}
}
