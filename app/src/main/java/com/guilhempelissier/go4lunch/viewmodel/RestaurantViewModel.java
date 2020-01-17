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
import com.guilhempelissier.go4lunch.model.Restaurant;
import com.guilhempelissier.go4lunch.model.User;
import com.guilhempelissier.go4lunch.repository.PlacesRepository;
import com.guilhempelissier.go4lunch.repository.UsersRepository;

import java.util.ArrayList;
import java.util.List;

public class RestaurantViewModel extends AndroidViewModel {

	private PlacesRepository placesRepository;
	private UsersRepository usersRepository;

	private MutableLiveData<List<Restaurant>> results = new MutableLiveData<>();
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
		currentRestaurant.addSource(usersRepository.getCurrentUser(), o -> updateCurrentRestaurant());
	}

	private void updateCurrentRestaurant() {
		List<User> workmates = usersRepository.getWorkmates().getValue();

		for (Restaurant result : results.getValue()) {
			if (result.getPlaceId().equals(currentRestaurantId.getValue())) {
				List<String> workmateNames = new ArrayList<>();
				List<FormattedWorkmate> formattedWorkmates = new ArrayList<>();
				if (workmates != null) {
					for (User user : workmates) {
						if (result.getPlaceId().equals(user.getLunch())) {
							FormattedWorkmate formattedWorkmate = new FormattedWorkmate(
									user.getUsername(),
									result.getPlaceId(),
									result.getName(),
									user.getImageUrl());
							formattedWorkmates.add(formattedWorkmate);
							workmateNames.add(user.getUsername());
						}
					}
				}
				currentRestaurant.setValue(FormatUtils.formatRestaurant(
						currentLocation.getValue(),
						result,
						workmateNames,
						getApplication().getApplicationContext(),
						usersRepository.getCurrentUser().getValue()));
				workmatesEatingThere.setValue(formattedWorkmates);
			}
		}
	}

	public void toggleEatingLunchHere() {
		FormattedRestaurant restaurant = currentRestaurant.getValue();
		if (restaurant != null) {
			if (restaurant.isMyLunch()) {
				usersRepository.updateCurrentUserLunch("");
			} else {
				usersRepository.updateCurrentUserLunch(restaurant.getId());
			}
		}
	}

	public void toggleLikeRestaurant() {
		FormattedRestaurant restaurant = currentRestaurant.getValue();
		User user = usersRepository.getCurrentUser().getValue();
		List<String> likedRestaurants = user.getLikedRestaurants();

		if (restaurant != null) {
			if (likedRestaurants.contains(restaurant.getId())) {
				likedRestaurants.remove(restaurant.getId());
			} else {
				likedRestaurants.add(restaurant.getId());
			}
		}

		usersRepository.updateCurrentUserLikes(likedRestaurants);
	}

	public LiveData<FormattedRestaurant> getCurrentRestaurant() {
		return currentRestaurant;
	}

	public LiveData<List<FormattedWorkmate>> getWorkmatesEatingThere() {
		return workmatesEatingThere;
	}
}
