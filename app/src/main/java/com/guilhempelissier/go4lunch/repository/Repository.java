package com.guilhempelissier.go4lunch.repository;

import android.content.Context;
import android.location.Location;

import com.guilhempelissier.go4lunch.di.DI;
import com.guilhempelissier.go4lunch.model.PlacesResponse;
import com.guilhempelissier.go4lunch.service.LocationService;
import com.guilhempelissier.go4lunch.service.PlacesAPIStreams;

import io.reactivex.Observable;

public class Repository {
	private String TAG = "Repository";
	private PlacesAPIStreams placesAPI;
	private LocationService locationService;

	public Repository(Context applicationContext) {
		placesAPI = DI.getPlacesService();
		locationService = DI.getLocationService(applicationContext);
	}

	public Observable<Location> getCurrentLocation() {
		return locationService.getObservableLocation();
	}

	public Observable<PlacesResponse> getRestaurantsAround(Location location, String radius) {
		return placesAPI.getRestaurantsAround(location, radius);
	}
}
