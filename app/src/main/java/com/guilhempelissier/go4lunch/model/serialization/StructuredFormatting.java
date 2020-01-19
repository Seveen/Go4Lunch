package com.guilhempelissier.go4lunch.model.serialization;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class StructuredFormatting {
	@SerializedName("main_text")
	private String mainText;
	@SerializedName("main_text_matched_substrings")
	private List<Substring> mainTextMatchedSubstrings;
	@SerializedName("secondary_text")
	private String secondaryText;

	public String getMainText() {
		return mainText;
	}

	public void setMainText(String mainText) {
		this.mainText = mainText;
	}

	public List<Substring> getMainTextMatchedSubstrings() {
		return mainTextMatchedSubstrings;
	}

	public void setMainTextMatchedSubstrings(List<Substring> mainTextMatchedSubstrings) {
		this.mainTextMatchedSubstrings = mainTextMatchedSubstrings;
	}

	public String getSecondaryText() {
		return secondaryText;
	}

	public void setSecondaryText(String secondaryText) {
		this.secondaryText = secondaryText;
	}
}
