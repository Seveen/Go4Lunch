package com.guilhempelissier.go4lunch.model.serialization;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AutocompleteResult {
	@Expose
	private String description;
	@SerializedName("distance_meters")
	private Integer distance;
	@SerializedName("place_id")
	private String placeId;
	@Expose
	private List<Term> terms;
	@Expose
	private List<String> types;
	@SerializedName("matched_substrings")
	private List<Substring> matchedSubstrings;
	@SerializedName("structured_formatting")
	private StructuredFormatting structuredFormatting;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public List<Term> getTerms() {
		return terms;
	}

	public void setTerms(List<Term> terms) {
		this.terms = terms;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	public List<Substring> getMatchedSubstrings() {
		return matchedSubstrings;
	}

	public void setMatchedSubstrings(List<Substring> matchedSubstrings) {
		this.matchedSubstrings = matchedSubstrings;
	}

	public StructuredFormatting getStructuredFormatting() {
		return structuredFormatting;
	}

	public void setStructuredFormatting(StructuredFormatting structuredFormatting) {
		this.structuredFormatting = structuredFormatting;
	}
}
