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
import com.guilhempelissier.go4lunch.model.Restaurant;
import com.guilhempelissier.go4lunch.model.User;
import com.guilhempelissier.go4lunch.repository.PlacesRepository;
import com.guilhempelissier.go4lunch.repository.UsersRepository;
import com.guilhempelissier.go4lunch.utils.FormatUtils;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class MapViewModel extends AndroidViewModel {
	private String TAG = "MapVM";
	private PlacesRepository placesRepository;
	private UsersRepository usersRepository;

	private MutableLiveData<Location> currentLocation = new MutableLiveData<>();
	private MutableLiveData<List<Restaurant>> results = new MutableLiveData<>();

	private MediatorLiveData<List<FormattedRestaurant>> restaurants;

	@SuppressLint("CheckResult")
	public MapViewModel(@NonNull Application application) {
		super(application);
		placesRepository = DI.getPlacesRepository(application.getApplicationContext());
		usersRepository = DI.getUsersRepository();

		placesRepository.getCurrentLocation()
			.subscribe(location -> currentLocation.setValue(location), error -> Log.d(TAG, error.getMessage()));

		placesRepository.getDetailedRestaurantsAround()
			.subscribe(allResults -> {
				Log.d(TAG, "All results: " + allResults.size());
				results.setValue(allResults);
			}, error -> Log.d(TAG, "Detailed restaurants error: " + error.getMessage()));

		restaurants = new MediatorLiveData<>();
		restaurants.addSource(results, o -> updateRestaurants());
		restaurants.addSource(usersRepository.getWorkmates(), o -> updateRestaurants());
	}

	private void updateRestaurants() {
		List<FormattedRestaurant> formattedRestaurants = new ArrayList<>();
		List<User> workmates = usersRepository.getWorkmates().getValue();

		if (results.getValue() != null) {
			for (Restaurant result : results.getValue()) {
				List<String> workmatesEatingThere = new ArrayList<>();
				if (workmates != null) {
					for (User user : workmates) {
						if (result.getPlaceId().equals(user.getLunch())) {
							workmatesEatingThere.add(user.getUid());
						}
					}
				}
				formattedRestaurants.add(FormatUtils.formatRestaurant(
						currentLocation.getValue(),
						result,
						workmatesEatingThere,
						getApplication().getApplicationContext(),
						usersRepository.getCurrentUser().getValue(),
						LocalDateTime.now()));
			}
			restaurants.setValue(formattedRestaurants);
		}
	}

	public LiveData<List<FormattedRestaurant>> getRestaurants() {
		return restaurants;
	}

	public LiveData<Location> getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentRestaurantId(String id) {
		placesRepository.setCurrentRestaurantId(id);
	}
}
