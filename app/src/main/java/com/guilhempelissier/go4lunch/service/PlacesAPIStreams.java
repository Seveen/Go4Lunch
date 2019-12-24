package com.guilhempelissier.go4lunch.service;

import android.location.Location;

import com.guilhempelissier.go4lunch.BuildConfig;
import com.guilhempelissier.go4lunch.model.PlacesResponse;

import io.reactivex.Observable;

import static android.location.Location.FORMAT_DEGREES;

public class PlacesAPIStreams {

	public static Observable<PlacesResponse> getRestaurantsAround(Location location,
																  String radius) {
		String locationLatitude = Location.convert(location.getLatitude(),FORMAT_DEGREES);
		String locationLongitude = Location.convert(location.getLongitude(),FORMAT_DEGREES);
		String locationCoordinates = locationLatitude + "," + locationLongitude;

		PlacesAPIService placesAPIService = PlacesAPIService.retrofit.create(PlacesAPIService.class);
		return placesAPIService.getRestaurantsAround(locationCoordinates,
				radius, BuildConfig.PLACES_KEY, "restaurant");
	}
}
