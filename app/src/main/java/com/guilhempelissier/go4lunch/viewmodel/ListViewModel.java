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
import com.guilhempelissier.go4lunch.utils.SortingUtils;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class ListViewModel extends AndroidViewModel {
	private String TAG = "ListVM";
	private PlacesRepository placesRepository;
	private UsersRepository usersRepository;

	private MutableLiveData<List<Restaurant>> results = new MutableLiveData<>();
	private MutableLiveData<Location> currentLocation = new MutableLiveData<>();
	private MutableLiveData<PlacesRepository.Sorting> sortingMethod = new MutableLiveData<>();

	private MediatorLiveData<List<FormattedRestaurant>> restaurants = new MediatorLiveData<>();

	@SuppressLint("CheckResult")
	public ListViewModel(@NonNull Application application) {
		super(application);
		placesRepository = DI.getPlacesRepository(application.getApplicationContext());
		usersRepository = DI.getUsersRepository();

		placesRepository.getCurrentLocation()
				.subscribe(currentLocation::setValue, error -> Log.d(TAG, error.getMessage()));

		placesRepository.getDetailedRestaurantsAround()
				.subscribe(results::setValue, error -> Log.d(TAG, "Detailed restaurants error: " + error.getMessage()));

		placesRepository.getSortingMethod()
				.subscribe(sortingMethod::setValue);

		restaurants.addSource(results, o -> updateRestaurants());
		restaurants.addSource(usersRepository.getWorkmates(), o -> updateRestaurants());
		restaurants.addSource(currentLocation, o -> updateRestaurants());
		restaurants.addSource(sortingMethod, o -> updateRestaurants());
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
				Location location = currentLocation.getValue();
				if (location != null) {
					formattedRestaurants.add(FormatUtils.formatRestaurant(
							location,
							result,
							workmatesEatingThere,
							getApplication().getApplicationContext(),
							usersRepository.getCurrentUser().getValue(),
							LocalDateTime.now()));
				}
			}

			SortingUtils.sortRestaurantsBy(formattedRestaurants, sortingMethod.getValue());

			restaurants.setValue(formattedRestaurants);
		}
	}

	public LiveData<List<FormattedRestaurant>> getRestaurantsList() {
		return restaurants;
	}

	public void setCurrentRestaurantId(String id) {
		placesRepository.setCurrentRestaurantId(id);
	}

	public void setSortingMethod(PlacesRepository.Sorting sorting) {
		placesRepository.setSortingMethod(sorting);
	}
}
