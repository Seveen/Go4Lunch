package com.guilhempelissier.go4lunch.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.guilhempelissier.go4lunch.di.DI;
import com.guilhempelissier.go4lunch.model.LunchComparator;
import com.guilhempelissier.go4lunch.model.User;
import com.guilhempelissier.go4lunch.service.AuthService;
import com.guilhempelissier.go4lunch.service.FirebaseService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsersRepository {
	private AuthService authService;
	private MutableLiveData<FirebaseUser> currentAuthUser = new MutableLiveData<>();
	private MutableLiveData<Boolean> isUserConnected = new MutableLiveData<>();
	private MutableLiveData<User> currentUser = new MutableLiveData<>();
	private MutableLiveData<List<User>> workmates = new MutableLiveData<>();

	public UsersRepository() {
		authService = DI.getAuthService();
		updateConnectedUser();

		FirebaseService.getUsersCollection().addSnapshotListener((snapshot, e) -> {
			if (snapshot != null && !snapshot.isEmpty()) {
				List<User> users = snapshot.toObjects(User.class);
				List<User> workmateUsers = new ArrayList<>();

				for (User user : users) {
					if (currentAuthUser.getValue() != null && user.getUid().equals(currentAuthUser.getValue().getUid())) {
						currentUser.setValue(user);
					} else {
						workmateUsers.add(user);
					}
				}
				Collections.sort(workmateUsers, new LunchComparator());
				workmates.setValue(workmateUsers);
			}
		});
	}

	public LiveData<FirebaseUser> getCurrentAuthUser() {
		return currentAuthUser;
	}

	public LiveData<User> getCurrentUser() {
		return currentUser;
	}

	public LiveData<Boolean> isUserConnected() {
		return isUserConnected;
	}

	public LiveData<List<User>> getWorkmates() {
		return workmates;
	}

	public void disconnectCurrentUser() {
		authService.disconnectCurrentUser();
		updateConnectedUser();
	}

	public void updateConnectedUser() {
		FirebaseUser user = authService.getCurrentUser();
		Boolean connected = authService.isUserConnected();
		currentAuthUser.setValue(user);
		isUserConnected.setValue(connected);

		if (connected) {
			FirebaseService.createUser(user.getUid(), user.getDisplayName(),
					user.getPhotoUrl().toString());
		}
	}

	public void updateCurrentUserLunch(String lunch) {
		if (currentUser.getValue() != null) {
			FirebaseService.updateLunch(currentUser.getValue().getUid(), lunch);
		}
	}

	public void updateCurrentUserLikes(List<String> likes) {
		if (currentUser.getValue() != null) {
			FirebaseService.updateLikes(currentUser.getValue().getUid(), likes);
		}
	}
}
