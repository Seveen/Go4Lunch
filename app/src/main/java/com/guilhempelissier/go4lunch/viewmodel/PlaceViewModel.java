package com.guilhempelissier.go4lunch.viewmodel;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.guilhempelissier.go4lunch.di.DI;
import com.guilhempelissier.go4lunch.model.FormattedRestaurant;
import com.guilhempelissier.go4lunch.model.serialization.AllResult;
import com.guilhempelissier.go4lunch.repository.PlacesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class PlaceViewModel extends AndroidViewModel {
	private String TAG = "PlaceVM";
	private PlacesRepository placesRepository;
	private MutableLiveData<Boolean> needsPermission = new MutableLiveData<>();
	private MutableLiveData<List<FormattedRestaurant>> restaurantsList = new MutableLiveData<>();
	private MutableLiveData<Location> currentLocation = new MutableLiveData<>();

	public PlaceViewModel(@NonNull Application application) {
		super(application);
		placesRepository = DI.getPlacesRepository(application.getApplicationContext());

		placesRepository.getCurrentLocation()
				.doOnError(error -> {
					if (error instanceof SecurityException) {
						needsPermission.postValue(true);
					}
				})
				.retryWhen(attempts -> attempts.flatMap(i -> Observable.timer(1, TimeUnit.SECONDS)))
				.subscribe(location -> {
					Boolean updateLocation = false;

					if (currentLocation.getValue() != null) {
						Float distanceFromOldLocation = currentLocation.getValue().distanceTo(location);
						if (distanceFromOldLocation > 100.0) {
							updateLocation = true;
						}
					} else {
						updateLocation = true;
					}

					if (updateLocation) {
						currentLocation.postValue(location);
						placesRepository.getDetailedRestaurantsAround(location, "1500")
								.subscribe(allResults -> {
									List<FormattedRestaurant> results = new ArrayList<>();
									for (AllResult result : allResults) {
										Log.d(TAG, "All results: " + result.getId());
										results.add(FormatUtils.formatAllResult(currentLocation.getValue(), result));
									}
									restaurantsList.postValue(results);
								}, error -> Log.d(TAG, "Detailed restaurants error: " + error.getMessage()));
					}
				}, error -> Log.d(TAG, error.getMessage()));
	}

	public LiveData<List<FormattedRestaurant>> getRestaurantsList() {
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
