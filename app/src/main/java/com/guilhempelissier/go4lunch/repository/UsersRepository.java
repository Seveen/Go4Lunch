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
			FirebaseService.getUser(user.getUid()).addOnSuccessListener(documentSnapshot -> {
				if (!documentSnapshot.exists()) {
					FirebaseService.createUser(user.getUid(), user.getDisplayName(),
							user.getPhotoUrl().toString());
				}
			});

			FirebaseService.getUsersCollection().addSnapshotListener((snapshot, e) -> {
				if (snapshot != null && !snapshot.isEmpty()) {
					List<User> users = snapshot.toObjects(User.class);
					List<User> workmateUsers = new ArrayList<>();

					for (User usr : users) {
						if (currentAuthUser.getValue() != null && usr.getUid().equals(currentAuthUser.getValue().getUid())) {
							currentUser.setValue(usr);
						} else {
							workmateUsers.add(usr);
						}
					}
					Collections.sort(workmateUsers, new LunchComparator());
					workmates.setValue(workmateUsers);
				}
			});
		}
	}

	public void updateCurrentUserLunch(String lunch) {
		if (currentAuthUser.getValue() != null) {
			FirebaseService.updateLunch(currentAuthUser.getValue().getUid(), lunch);
		}
	}

	public void updateCurrentUserLikes(List<String> likes) {
		if (currentAuthUser.getValue() != null) {
			FirebaseService.updateLikes(currentAuthUser.getValue().getUid(), likes);
		}
	}

	public void updateCurrentUserNotificationStatus(Boolean isNotified) {
		if (currentAuthUser.getValue() != null) {
			FirebaseService.updateNotification(currentAuthUser.getValue().getUid(), isNotified);
		}
	}
}
