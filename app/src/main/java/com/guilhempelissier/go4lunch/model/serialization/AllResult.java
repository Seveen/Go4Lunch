package com.guilhempelissier.go4lunch.model.serialization;

public class AllResult {
	private String id;
	private DetailsResult detailsResult;
	private NearbyResult nearbyResult;

	public AllResult(String id, DetailsResult detailsResult, NearbyResult nearbyResult) {
		this.id = id;
		this.detailsResult = detailsResult;
		this.nearbyResult = nearbyResult;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DetailsResult getDetailsResult() {
		return detailsResult;
	}

	public void setDetailsResult(DetailsResult detailsResult) {
		this.detailsResult = detailsResult;
	}

	public NearbyResult getNearbyResult() {
		return nearbyResult;
	}

	public void setNearbyResult(NearbyResult nearbyResult) {
		this.nearbyResult = nearbyResult;
	}
}
