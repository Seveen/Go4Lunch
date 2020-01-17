package com.guilhempelissier.go4lunch.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.guilhempelissier.go4lunch.di.DI;
import com.guilhempelissier.go4lunch.model.FormattedWorkmate;
import com.guilhempelissier.go4lunch.model.Restaurant;
import com.guilhempelissier.go4lunch.model.User;
import com.guilhempelissier.go4lunch.repository.PlacesRepository;
import com.guilhempelissier.go4lunch.repository.UsersRepository;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesViewModel extends AndroidViewModel {
	private String TAG = "WorkmatesVM";
	private UsersRepository usersRepository;
	private PlacesRepository placesRepository;
	private MediatorLiveData<List<FormattedWorkmate>> formattedWorkmates = new MediatorLiveData<>();
	private MutableLiveData<List<Restaurant>> restaurants = new MutableLiveData<>();

	public WorkmatesViewModel(@NonNull Application application) {
		super(application);
		usersRepository = DI.getUsersRepository();
		placesRepository = DI.getPlacesRepository(application.getApplicationContext());

		placesRepository.getDetailedRestaurantsAround().subscribe(restaurants::setValue);

		formattedWorkmates.addSource(usersRepository.getCurrentAuthUser(), o -> updateWorkmatesList());
		formattedWorkmates.addSource(usersRepository.getWorkmates(), o -> updateWorkmatesList());
	}

	public LiveData<List<FormattedWorkmate>> getWorkmates() {
		return formattedWorkmates;
	}

	private void updateWorkmatesList() {
		List<FormattedWorkmate> list = new ArrayList<>();
		List<User> workmates = usersRepository.getWorkmates().getValue();

		if (workmates != null && restaurants.getValue() != null) {
			FirebaseUser currentUser = usersRepository.getCurrentAuthUser().getValue();
			for (User workmate : workmates) {
				String lunchName = "";
				for (Restaurant restaurant : restaurants.getValue()) {
					if (restaurant.getPlaceId().equals(workmate.getLunch())) {
						lunchName = restaurant.getName();
					}
				}

				if (currentUser != null && !workmate.getUid().equals(currentUser.getUid())) {
					list.add(new FormattedWorkmate(workmate.getUsername(),
							workmate.getLunch(),
							lunchName,
							workmate.getImageUrl()));
				}
			}
		}

		formattedWorkmates.setValue(list);
	}

	public void setCurrentRestaurantId(String id) {
		placesRepository.setCurrentRestaurantId(id);
	}
}
