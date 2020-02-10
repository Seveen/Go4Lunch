package com.guilhempelissier.go4lunch.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.guilhempelissier.go4lunch.di.DI;
import com.guilhempelissier.go4lunch.model.Restaurant;
import com.guilhempelissier.go4lunch.service.LocationService;
import com.guilhempelissier.go4lunch.service.PlacesAPIStreams;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class PlacesRepository {
	private String TAG = "PlacesRepository";

	private Location cachedLocation;
	private BehaviorSubject<Location> locationStream;
	private BehaviorSubject<Boolean> permissionStatus;
	private BehaviorSubject<List<Restaurant>> restaurantsStream;
	private BehaviorSubject<Sorting> sortingMethod;
	private BehaviorSubject<Restaurant> currentSelectedRestaurant;
	private BehaviorSubject<List<Restaurant>> workmatesRestaurants;

	@SuppressLint("CheckResult")
	public PlacesRepository(Context applicationContext) {
		LocationService locationService = DI.getLocationService(applicationContext);

		locationStream = BehaviorSubject.create();
		permissionStatus = BehaviorSubject.create();
		restaurantsStream = BehaviorSubject.create();
		sortingMethod = BehaviorSubject.create();
		currentSelectedRestaurant = BehaviorSubject.create();
		workmatesRestaurants = BehaviorSubject.create();

		sortingMethod.onNext(Sorting.DistanceLeast);

		locationService.getObservableLocation()
			.doOnError( error -> {
				if (error instanceof SecurityException) {
					permissionStatus.onNext(true);
				}
			})
			.retryWhen(attempts -> attempts.flatMap(i -> Observable.timer(1, TimeUnit.SECONDS)))
			.filter(location -> {
				if (cachedLocation == null) {
					return true;
				}
				return cachedLocation.distanceTo(location) > 100.0;
			})
			.subscribe(location -> {
				cachedLocation = location;
				locationStream.onNext(location);
				PlacesAPIStreams.getDetailedRestaurantsAround(location, "1500")
						.subscribe(restaurants -> restaurantsStream.onNext(restaurants), error -> Log.d(TAG, "PlacesRepository: error: "+ error.getMessage()));
			});
	}

	public void setCurrentRestaurantId(String id) {
		PlacesAPIStreams.getRestaurant(id).subscribe(currentSelectedRestaurant::onNext);
	}

	public void setSortingMethod(Sorting sorting) {
		sortingMethod.onNext(sorting);
	}

	public Observable<Location> getCurrentLocation() {
		return locationStream;
	}

	public Observable<Boolean> getPermissionStatus() {
		return permissionStatus;
	}

	public Observable<List<Restaurant>> getDetailedRestaurantsAround() {
		return restaurantsStream;
	}

	public Observable<Restaurant> getCurrentRestaurant() {
		return currentSelectedRestaurant;
	}

	public Observable<List<Restaurant>> getWorkmatesRestaurants() {
		return workmatesRestaurants;
	}

	public void updateWorkmatesRestaurants(List<String> restaurants) {
		PlacesAPIStreams.getRestaurants(restaurants)
				.subscribe(workmatesRestaurants::onNext, error -> Log.d(TAG, "PlacesRepository: error: "+ error.getMessage()));
	}

	public void getAutocompletePredictions(String input) {
		if (cachedLocation != null && !input.equals("")) {
			PlacesAPIStreams.getPlaceAutocomplete(input, cachedLocation, "10000")
					.subscribe(restaurantsStream::onNext, error -> Log.d(TAG, "PlacesRepository: error: "+ error.getMessage()));
		}
	}

	public void clearAutocompleteResults() {
		if (cachedLocation != null) {
			PlacesAPIStreams.getDetailedRestaurantsAround(cachedLocation, "1500")
					.subscribe(restaurantsStream::onNext, error -> Log.d(TAG, "PlacesRepository: error: "+ error.getMessage()));
		}
	}

	public Observable<Sorting> getSortingMethod() {
		return sortingMethod;
	}

	public enum Sorting {
		WorkmatesMost,
		WorkmatesLeast,
		DistanceMost,
		DistanceLeast,
		RatingMost,
		RatingLeast
	}
}
