package com.guilhempelissier.go4lunch.service;

import android.location.Location;
import android.util.Log;

import com.guilhempelissier.go4lunch.BuildConfig;
import com.guilhempelissier.go4lunch.model.serialization.AllResult;
import com.guilhempelissier.go4lunch.model.serialization.DetailsResult;
import com.guilhempelissier.go4lunch.model.serialization.NearbyResult;
import com.guilhempelissier.go4lunch.model.serialization.PlacesDetailsResponse;
import com.guilhempelissier.go4lunch.model.serialization.PlacesNearbyResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.location.Location.FORMAT_DEGREES;

public class PlacesAPIStreams {
	static String TAG = "PlacesAPIStreams";

	public static Observable<PlacesNearbyResponse> getRestaurantsAround(Location location,
																		String radius) {
		String locationLatitude = Location.convert(location.getLatitude(),FORMAT_DEGREES);
		String locationLongitude = Location.convert(location.getLongitude(),FORMAT_DEGREES);
		String locationCoordinates = locationLatitude + "," + locationLongitude;

		PlacesAPIService placesAPIService = PlacesAPIService.retrofit.create(PlacesAPIService.class);
		return placesAPIService.getRestaurantsAround(locationCoordinates, radius, BuildConfig.PLACES_KEY, "restaurant")
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread());
	}

	public static Observable<PlacesDetailsResponse> getDetailsAboutRestaurant(String id) {
		PlacesAPIService placesAPIService = PlacesAPIService.retrofit.create(PlacesAPIService.class);

		return placesAPIService.getDetailsAboutRestaurant(id, BuildConfig.PLACES_KEY)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread());
	}

	public static Single<List<AllResult>> getDetailedRestaurantsAround(Location location,
																		 String radius) {
		Observable<NearbyResult> restaurantsAround = getRestaurantsAround(location, radius)
			.flatMapIterable(placesNearbyResponse -> {
				Log.d(TAG, "getDetailedRestaurantsAround Status: " + placesNearbyResponse.getStatus());
				return placesNearbyResponse.getResults();
			});

		Observable<DetailsResult> detailedRestaurant = restaurantsAround
			.flatMap(nearbyResult -> {
				Log.d(TAG, "getDetailedRestaurantsAround: processing result: " + nearbyResult.getName());
				return getDetailsAboutRestaurant(nearbyResult.getId());
			})
			.flatMap(placesDetailsResponse -> Observable.just(placesDetailsResponse.getResult()));

		return restaurantsAround.zipWith(detailedRestaurant, ((nearbyResult, detailsResult) -> {
				Log.d(TAG, "All results: " + nearbyResult.getId() + ", " + detailsResult.getName());
				return new AllResult(nearbyResult.getId(), detailsResult, nearbyResult);
			}))
			.toList()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread());
	}
}
