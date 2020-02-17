package com.guilhempelissier.go4lunch.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.guilhempelissier.go4lunch.di.DI;
import com.guilhempelissier.go4lunch.model.User;
import com.guilhempelissier.go4lunch.repository.PlacesRepository;
import com.guilhempelissier.go4lunch.repository.UsersRepository;

public class MainViewModel extends AndroidViewModel {
	private PlacesRepository placesRepository;
	private UsersRepository usersRepository;

	private MutableLiveData<Boolean> needsPermission = new MutableLiveData<>();

	@SuppressLint("CheckResult")
	public MainViewModel(@NonNull Application application) {
		super(application);
		placesRepository = DI.getPlacesRepository(application.getApplicationContext());
		usersRepository = DI.getUsersRepository();

		placesRepository.getPermissionStatus()
				.subscribe(needsPermission::setValue);
	}

	public LiveData<Boolean> getNeedsPermission() {
		return needsPermission;
	}

	public void setNeedsPermission(boolean val) {
		needsPermission.setValue(val);
	}

	public void setCurrentRestaurantId(String id) {
		placesRepository.setCurrentRestaurantId(id);
	}

	public LiveData<User> getCurrentUser() {
		return usersRepository.getCurrentUser();
	}

	public void queryAutocompletePrediction(String input) {
		placesRepository.getAutocompletePredictions(input);
	}

	public void clearAutocompleteResults() {
		placesRepository.clearAutocompleteResults();
	}

	public void toggleNotifyMe() {
		User user = usersRepository.getCurrentUser().getValue();
		if (user != null) {
			usersRepository.updateCurrentUserNotificationStatus(!user.isNotifyMe());
		}
	}
}
