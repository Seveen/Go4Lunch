package com.guilhempelissier.go4lunch.viewmodel;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.guilhempelissier.go4lunch.di.DI;
import com.guilhempelissier.go4lunch.model.serialization.NearbyResult;
import com.guilhempelissier.go4lunch.repository.PlacesRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class MapViewModel extends AndroidViewModel {
	private String TAG = "MapVM";
	private PlacesRepository placesRepository;
	private MutableLiveData<Boolean> needsPermission = new MutableLiveData<>();
	private MutableLiveData<List<NearbyResult>> restaurantsList = new MutableLiveData<>();
	private MutableLiveData<Location> currentLocation = new MutableLiveData<>();

	public MapViewModel(@NonNull Application application) {
		super(application);
		placesRepository = DI.getRepository(application.getApplicationContext());

		placesRepository.getCurrentLocation()
				.doOnError(error -> {
					if (error instanceof SecurityException) {
						needsPermission.postValue(true);
					}
				})
				.retryWhen(attempts -> attempts.flatMap(i -> Observable.timer(1, TimeUnit.SECONDS)))
				.subscribe(location -> {
					currentLocation.postValue(location);
//					placesRepository.getRestaurantsAround(location, "1500")
//							.subscribe(response -> {
//								List<NearbyResult> results = response.getResults();
//								restaurantsList.postValue(results);
//								Log.d(TAG, "MapViewModel: found restaurant: " + response);
//							}, error -> Log.d(TAG, "Restaurants error: " + error.getMessage()));
					placesRepository.getDetailedRestaurantsAround(location, "1500")
							.subscribe(placeDetailsResponses -> {
								Log.d(TAG, "MapViewModel: detailed" + placeDetailsResponses);
							}, error -> Log.d(TAG, "Detailed restaurants error: " + error.getMessage()));
				}, error -> Log.d(TAG, error.getMessage()));
	}

	public LiveData<List<NearbyResult>> getRestaurantsList() {
		return restaurantsList;
	}

	public LiveData<Location> getCurrentLocation() {
		return currentLocation;
	}

	public LiveData<Boolean> getNeedsPermission() {
		return needsPermission;
	}

	public void setNeedsPermission(boolean val) {
		needsPermission.setValue(val);
	}
}
