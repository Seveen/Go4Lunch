package com.guilhempelissier.go4lunch.model;

import java.util.ArrayList;
import java.util.List;

public class User {
	private String uid;
	private String username;
	private String imageUrl;
	private String lunch;
	private List<String> likedRestaurants;
	private boolean notifyMe;

	public User() {}

	public User(String uid, String username,String imageUrl) {
		this.uid = uid;
		this.username = username;
		this.imageUrl = imageUrl;
		this.lunch = "";
		this.likedRestaurants = new ArrayList<>();
		this.notifyMe = true;
	}

	public User(String uid, String username,String imageUrl, String lunch, List<String> likedRestaurants) {
		this.uid = uid;
		this.username = username;
		this.imageUrl = imageUrl;
		this.lunch = lunch;
		this.likedRestaurants = likedRestaurants;
		this.notifyMe = true;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLunch() {
		return lunch;
	}

	public void setLunch(String lunch) {
		this.lunch = lunch;
	}

	public List<String> getLikedRestaurants() {
		return likedRestaurants;
	}

	public void setLikedRestaurants(List<String> likedRestaurants) {
		this.likedRestaurants = likedRestaurants;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public boolean isNotifyMe() {
		return notifyMe;
	}

	public void setNotifyMe(boolean notifyMe) {
		this.notifyMe = notifyMe;
	}
}
