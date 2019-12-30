package com.guilhempelissier.go4lunch.di;

import android.content.Context;

import com.guilhempelissier.go4lunch.repository.PlacesRepository;
import com.guilhempelissier.go4lunch.service.LocationService;
import com.guilhempelissier.go4lunch.service.PlacesAPIStreams;

public class DI {
	private static PlacesRepository placesRepository;
	private static PlacesAPIStreams placesService;
	private static LocationService locationService;

	public static PlacesRepository getRepository(Context applicationContext) {
		if (placesRepository == null) {
			placesRepository = new PlacesRepository(applicationContext);
		}
		return placesRepository;
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
