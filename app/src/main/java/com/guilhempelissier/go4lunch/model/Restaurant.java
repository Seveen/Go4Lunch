package com.guilhempelissier.go4lunch.model;

import com.guilhempelissier.go4lunch.model.serialization.Geometry;
import com.guilhempelissier.go4lunch.model.serialization.OpeningHours;
import com.guilhempelissier.go4lunch.model.serialization.Photo;

import java.util.List;

public class Restaurant {

	private Geometry geometry;
	private String name;
	private List<Photo> photos;
	private String placeId;
	private String vicinity;
	private Double rating;
	private String formattedPhoneNumber;
	private String website;
	private OpeningHours openingHours;

	public Restaurant(Geometry geometry, String name, List<Photo> photos, String placeId, String vicinity, Double rating, String formattedPhoneNumber, String website, OpeningHours openingHours) {
		this.geometry = geometry;
		this.name = name;
		this.photos = photos;
		this.placeId = placeId;
		this.vicinity = vicinity;
		this.rating = rating;
		this.formattedPhoneNumber = formattedPhoneNumber;
		this.website = website;
		this.openingHours = openingHours;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public String getName() {
		return name;
	}

	public List<Photo> getPhotos() {
		return photos;
	}

	public String getPlaceId() {
		return placeId;
	}

	public String getVicinity() {
		return vicinity;
	}

	public Double getRating() {
		return rating;
	}

	public String getFormattedPhoneNumber() {
		return formattedPhoneNumber;
	}

	public String getWebsite() {
		return website;
	}

	public OpeningHours getOpeningHours() {
		return openingHours;
	}
}
