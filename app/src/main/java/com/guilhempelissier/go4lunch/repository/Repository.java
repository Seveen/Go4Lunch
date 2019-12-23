package com.guilhempelissier.go4lunch.repository;

import android.content.Context;
import android.location.Location;

import com.google.android.libraries.places.api.model.Place;
import com.guilhempelissier.go4lunch.di.DI;
import com.guilhempelissier.go4lunch.service.LocationService;
import com.guilhempelissier.go4lunch.service.PlacesAPIService;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class Repository {
	private String TAG = "Repository";
	private PlacesAPIService placesAPI;
	private LocationService locationService;

	public Repository(Context applicationContext) {
		placesAPI = DI.getPlacesService(applicationContext);
		locationService = DI.getLocationService(applicationContext);
	}

	public Observable<Location> getCurrentLocation() {
		return  locationService.getObservableLocation();
	}

	public Single<List<Place>> getRestaurantsAround() {
		return placesAPI.getRestaurantsAround();
	}
}
