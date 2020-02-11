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
import com.guilhempelissier.go4lunch.utils.FormatUtils;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class RestaurantViewModel extends AndroidViewModel {

	private PlacesRepository placesRepository;
	private UsersRepository usersRepository;

	private MutableLiveData<Restaurant> result = new MutableLiveData<>();
	private MediatorLiveData<FormattedRestaurant> currentRestaurant = new MediatorLiveData<>();
	private MutableLiveData<Location> currentLocation = new MutableLiveData<>();

	private MutableLiveData<List<FormattedWorkmate>> workmatesEatingThere = new MutableLiveData<>();

	public RestaurantViewModel(@NonNull Application application) {
		super(application);
		placesRepository = DI.getPlacesRepository(application.getApplicationContext());
		usersRepository = DI.getUsersRepository();

		placesRepository.getCurrentRestaurant()
				.subscribe(result::setValue);

		placesRepository.getCurrentLocation()
				.subscribe(currentLocation::setValue);

		currentRestaurant.addSource(result, o -> updateCurrentRestaurant());
		currentRestaurant.addSource(usersRepository.getCurrentUser(), o -> updateCurrentRestaurant());
	}

	private void updateCurrentRestaurant() {
		List<User> workmates = usersRepository.getWorkmates().getValue();

		if(result.getValue() != null && currentLocation.getValue() != null) {
			List<String> workmateNames = new ArrayList<>();
			List<FormattedWorkmate> formattedWorkmates = new ArrayList<>();
			if (workmates != null) {
				for (User user : workmates) {
					if (result.getValue().getPlaceId().equals(user.getLunch())) {
						FormattedWorkmate formattedWorkmate = new FormattedWorkmate(
								user.getUsername(),
								result.getValue().getPlaceId(),
								result.getValue().getName(),
								user.getImageUrl());
						formattedWorkmates.add(formattedWorkmate);
						workmateNames.add(user.getUsername());
					}
				}
			}
			currentRestaurant.setValue(FormatUtils.formatRestaurant(
					currentLocation.getValue(),
					result.getValue(),
					workmateNames,
					getApplication().getApplicationContext(),
					usersRepository.getCurrentUser().getValue(),
					LocalDateTime.now()));
			workmatesEatingThere.setValue(formattedWorkmates);
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

		if (user != null) {
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
	}

	public LiveData<FormattedRestaurant> getCurrentRestaurant() {
		return currentRestaurant;
	}

	public LiveData<List<FormattedWorkmate>> getWorkmatesEatingThere() {
		return workmatesEatingThere;
	}
}
