package com.guilhempelissier.go4lunch.repository;

import android.content.Context;
import android.location.Location;

import com.guilhempelissier.go4lunch.di.DI;
import com.guilhempelissier.go4lunch.model.serialization.AllResult;
import com.guilhempelissier.go4lunch.model.serialization.PlacesNearbyResponse;
import com.guilhempelissier.go4lunch.service.LocationService;
import com.guilhempelissier.go4lunch.service.PlacesAPIStreams;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class PlacesRepository {
	private String TAG = "PlacesRepository";
	private PlacesAPIStreams placesAPI;
	private LocationService locationService;

	public PlacesRepository(Context applicationContext) {
		placesAPI = DI.getPlacesService();
		locationService = DI.getLocationService(applicationContext);
	}

	public Observable<Location> getCurrentLocation() {
		return locationService.getObservableLocation();
	}

	public Observable<PlacesNearbyResponse> getRestaurantsAround(Location location, String radius) {
		return placesAPI.getRestaurantsAround(location, radius);
	}

	public Single<List<AllResult>> getDetailedRestaurantsAround(Location location, String radius) {
		return placesAPI.getDetailedRestaurantsAround(location, radius);
	}
}
