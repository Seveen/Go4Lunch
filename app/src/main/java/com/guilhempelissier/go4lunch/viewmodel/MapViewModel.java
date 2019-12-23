package com.guilhempelissier.go4lunch.viewmodel;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.libraries.places.api.model.Place;
import com.guilhempelissier.go4lunch.di.DI;
import com.guilhempelissier.go4lunch.repository.Repository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MapViewModel extends AndroidViewModel {
	private String TAG = "MapVM";
	private Repository repository;
	private MutableLiveData<List<Place>> restaurantsList = new MutableLiveData<>();
	private MutableLiveData<Location> currentLocation = new MutableLiveData<>();

	public MapViewModel(@NonNull Application application) {
		super(application);
		repository = DI.getRepository(application.getApplicationContext());

		repository.getRestaurantsAround()
				.subscribeOn(Schedulers.newThread())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(list -> restaurantsList.postValue(list), error -> Log.d(TAG, error.getMessage()));

		repository.getCurrentLocation()
				.subscribeOn(Schedulers.newThread())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(location -> currentLocation.postValue(location), error -> Log.d(TAG, error.getMessage()));
	}

	public LiveData<List<Place>> getRestaurantsList() {
		return restaurantsList;
	}

	public LiveData<Location> getCurrentLocation() {
		return currentLocation;
	}
}
