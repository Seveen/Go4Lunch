package com.guilhempelissier.go4lunch.viewmodel;

public class FormattedWorkmate {
	private String name;
	private String eatingPlace;
	private String imageUrl;

	//TODO juste le string qui va dans la textview? (compose des autres)
	public FormattedWorkmate(String name, String eatingPlace, String imageUrl) {
		this.name = name;
		this.imageUrl = imageUrl;
		this.eatingPlace = eatingPlace;
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
}
