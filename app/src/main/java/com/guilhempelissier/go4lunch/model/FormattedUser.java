package com.guilhempelissier.go4lunch.model;

public class FormattedUser {
	String email;
	String displayName;
	String avatarUrl;

	//TODO changer url
	public static FormattedUser defaultUser() {
		return new FormattedUser(" ", " ", "https://www.uclg-planning.org/sites/default/files/styles/featured_home_left/public/no-user-image-square.jpg?itok=PANMBJF-");
	}

	public FormattedUser(String email, String displayName, String avatarUrl) {
		this.email = email;
		this.displayName = displayName;
		this.avatarUrl = avatarUrl;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
}
