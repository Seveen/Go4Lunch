package com.guilhempelissier.go4lunch.viewmodel;

import android.annotation.SuppressLint;
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

public class PlaceViewModel extends AndroidViewModel {
	private String TAG = "PlaceVM";
	private PlacesRepository placesRepository;
	private MutableLiveData<Boolean> needsPermission = new MutableLiveData<>();
	private MutableLiveData<List<FormattedRestaurant>> restaurantsList = new MutableLiveData<>();
	private MutableLiveData<Location> currentLocation = new MutableLiveData<>();

	@SuppressLint("CheckResult")
	public PlaceViewModel(@NonNull Application application) {
		super(application);
		placesRepository = DI.getPlacesRepository(application.getApplicationContext());

		placesRepository.getPermissionStatus()
			.subscribe(needsPermission::setValue);

		placesRepository.getCurrentLocation()
			.subscribe(location -> currentLocation.setValue(location), error -> Log.d(TAG, error.getMessage()));

		placesRepository.getDetailedRestaurantsAround()
			.subscribe(allResults -> {
				List<FormattedRestaurant> results = new ArrayList<>();
				for (AllResult result : allResults) {
					Log.d(TAG, "All results: " + result.getId());
					results.add(FormatUtils.formatAllResult(currentLocation.getValue(), result));
				}
				restaurantsList.setValue(results);
			}, error -> Log.d(TAG, "Detailed restaurants error: " + error.getMessage()));
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
