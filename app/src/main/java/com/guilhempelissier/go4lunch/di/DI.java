package com.guilhempelissier.go4lunch.di;

import android.content.Context;

import com.guilhempelissier.go4lunch.repository.Repository;
import com.guilhempelissier.go4lunch.service.LocationService;
import com.guilhempelissier.go4lunch.service.PlacesAPIStreams;

public class DI {
	private static Repository repository;
	private static PlacesAPIStreams placesService;
	private static LocationService locationService;

	public static Repository getRepository(Context applicationContext) {
		if (repository == null) {
			repository = new Repository(applicationContext);
		}
		return repository;
	}

	public static PlacesAPIStreams getPlacesService() {
		if (placesService == null) {
			placesService = new PlacesAPIStreams();
		}
		return placesService;
	}

	public static LocationService getLocationService(Context applicationContext) {
		if (locationService == null) {
			locationService = new LocationService(applicationContext);
		}
		return locationService;
	}
}
