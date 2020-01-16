package com.guilhempelissier.go4lunch.viewmodel;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.guilhempelissier.go4lunch.di.DI;
import com.guilhempelissier.go4lunch.model.FormattedRestaurant;
import com.guilhempelissier.go4lunch.model.FormattedWorkmate;
import com.guilhempelissier.go4lunch.model.User;
import com.guilhempelissier.go4lunch.model.serialization.AllResult;
import com.guilhempelissier.go4lunch.repository.PlacesRepository;
import com.guilhempelissier.go4lunch.repository.UsersRepository;

import java.util.ArrayList;
import java.util.List;

public class RestaurantViewModel extends AndroidViewModel {

	private PlacesRepository placesRepository;
	private UsersRepository usersRepository;

	private MutableLiveData<List<AllResult>> results = new MutableLiveData<>();
	private MutableLiveData<String> currentRestaurantId = new MutableLiveData<>();
	private MediatorLiveData<FormattedRestaurant> currentRestaurant = new MediatorLiveData<>();
	private MutableLiveData<Location> currentLocation = new MutableLiveData<>();

	private MutableLiveData<List<FormattedWorkmate>> workmatesEatingThere = new MutableLiveData<>();

	public RestaurantViewModel(@NonNull Application application) {
		super(application);
		placesRepository = DI.getPlacesRepository(application.getApplicationContext());
		usersRepository = DI.getUsersRepository();

		placesRepository.getDetailedRestaurantsAround()
				.subscribe(results::setValue);

		placesRepository.getCurrentRestaurantId()
				.subscribe(currentRestaurantId::setValue);

		placesRepository.getCurrentLocation()
				.subscribe(currentLocation::setValue);

		currentRestaurant.addSource(results, o -> updateCurrentRestaurant());
		currentRestaurant.addSource(currentRestaurantId, o -> updateCurrentRestaurant());
	}

	private void updateCurrentRestaurant() {
		List<User> workmates = usersRepository.getWorkmates().getValue();

		for (AllResult result : results.getValue()) {
			if (result.getId().equals(currentRestaurantId.getValue())) {
				List<String> workmateNames = new ArrayList<>();
				List<FormattedWorkmate> formattedWorkmates = new ArrayList<>();
				if (workmates != null) {
					for (User user : workmates) {
						if (result.getId().equals(user.getLunch())) {
							FormattedWorkmate formattedWorkmate = new FormattedWorkmate(
									user.getUsername(),
									result.getNearbyResult().getName(),
									user.getImageUrl());
							formattedWorkmates.add(formattedWorkmate);
							workmateNames.add(user.getUsername());
						}
					}
				}
				currentRestaurant.setValue(FormatUtils.formatAllResult(currentLocation.getValue(), result, workmateNames));
				workmatesEatingThere.setValue(formattedWorkmates);
			}
		}
	}

	public LiveData<FormattedRestaurant> getCurrentRestaurant() {
		return currentRestaurant;
	}

	public LiveData<List<FormattedWorkmate>> getWorkmatesEatingThere() {
		return workmatesEatingThere;
	}
}
