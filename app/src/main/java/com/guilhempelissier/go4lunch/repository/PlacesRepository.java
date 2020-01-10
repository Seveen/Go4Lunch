package com.guilhempelissier.go4lunch.repository;

import android.content.Context;
import android.location.Location;

import com.guilhempelissier.go4lunch.di.DI;
import com.guilhempelissier.go4lunch.model.serialization.AllResult;
import com.guilhempelissier.go4lunch.service.LocationService;
import com.guilhempelissier.go4lunch.service.PlacesAPIStreams;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class PlacesRepository {
	private String TAG = "PlacesRepository";
	private LocationService locationService;

	private Location cachedLocation;
	private BehaviorSubject<Location> locationStream;
	private BehaviorSubject<Boolean> permissionStatus;
	private BehaviorSubject<List<AllResult>> restaurantsStream;

	public PlacesRepository(Context applicationContext) {
		locationService = DI.getLocationService(applicationContext);

		locationStream = BehaviorSubject.create();
		permissionStatus = BehaviorSubject.create();
		restaurantsStream = BehaviorSubject.create();

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
							.subscribe(restaurantsStream::onNext);
				});
	}

	public Observable<Location> getCurrentLocation() {
		return locationStream;
	}

	public Observable<Boolean> getPermissionStatus() {
		return permissionStatus;
	}

	public Observable<List<AllResult>> getDetailedRestaurantsAround() {
		return restaurantsStream;
	}
}
