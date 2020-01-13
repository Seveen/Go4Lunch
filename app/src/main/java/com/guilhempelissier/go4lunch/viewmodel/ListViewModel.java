package com.guilhempelissier.go4lunch.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.guilhempelissier.go4lunch.di.DI;
import com.guilhempelissier.go4lunch.model.FormattedRestaurant;
import com.guilhempelissier.go4lunch.model.User;
import com.guilhempelissier.go4lunch.model.serialization.AllResult;
import com.guilhempelissier.go4lunch.repository.PlacesRepository;
import com.guilhempelissier.go4lunch.repository.UsersRepository;

import java.util.ArrayList;
import java.util.List;

public class ListViewModel extends AndroidViewModel {
	private String TAG = "ListVM";
	private PlacesRepository placesRepository;
	private UsersRepository usersRepository;
	private MutableLiveData<List<AllResult>> results = new MutableLiveData<>();
	private MediatorLiveData<List<FormattedRestaurant>> restaurants;
	private MutableLiveData<Location> currentLocation = new MutableLiveData<>();

	@SuppressLint("CheckResult")
	public ListViewModel(@NonNull Application application) {
		super(application);
		placesRepository = DI.getPlacesRepository(application.getApplicationContext());
		usersRepository = DI.getUsersRepository();

		placesRepository.getCurrentLocation()
				.subscribe(location -> currentLocation.setValue(location), error -> Log.d(TAG, error.getMessage()));

		placesRepository.getDetailedRestaurantsAround()
				.subscribe(allResults -> {
					results.setValue(allResults);

				}, error -> Log.d(TAG, "Detailed restaurants error: " + error.getMessage()));


		restaurants = new MediatorLiveData<>();
		restaurants.addSource(results, o -> updateRestaurants());
		restaurants.addSource(usersRepository.getWorkmates(), o -> updateRestaurants());
	}

	private void updateRestaurants() {
		List<FormattedRestaurant> formattedRestaurants = new ArrayList<>();
		List<User> workmates = usersRepository.getWorkmates().getValue();

		for (AllResult result : results.getValue()) {
			List<String> workmatesEatingThere = new ArrayList<>();
			if (workmates != null) {
				for (User user : workmates) {
					if (result.getId().equals(user.getLunch())) {
						workmatesEatingThere.add(user.getUid());
					}
				}
			}
			formattedRestaurants.add(FormatUtils.formatAllResult(currentLocation.getValue(), result, workmatesEatingThere));
		}
		restaurants.setValue(formattedRestaurants);
	}

	public LiveData<List<FormattedRestaurant>> getRestaurantsList() {
		return restaurants;
	}
}
