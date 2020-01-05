package com.guilhempelissier.go4lunch.di;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.guilhempelissier.go4lunch.repository.PlacesRepository;
import com.guilhempelissier.go4lunch.repository.UsersRepository;
import com.guilhempelissier.go4lunch.service.AuthService;
import com.guilhempelissier.go4lunch.service.FirebaseService;
import com.guilhempelissier.go4lunch.service.LocationService;
import com.guilhempelissier.go4lunch.service.PlacesAPIStreams;

public class DI {
	private static PlacesRepository placesRepository;
	private static UsersRepository usersRepository;

	private static PlacesAPIStreams placesService;
	private static LocationService locationService;
	private static AuthService authService;
	private static FirebaseService firebaseService;

	public static PlacesRepository getPlacesRepository(Context applicationContext) {
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

	public static UsersRepository getUsersRepository() {
		if (usersRepository == null) {
			usersRepository = new UsersRepository();
		}
		return usersRepository;
	}

	public static AuthService getAuthService() {
		if (authService == null) {
			authService = new AuthService(FirebaseAuth.getInstance());
		}
		return authService;
	}
}
