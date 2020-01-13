package com.guilhempelissier.go4lunch.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.guilhempelissier.go4lunch.di.DI;
import com.guilhempelissier.go4lunch.model.User;
import com.guilhempelissier.go4lunch.service.AuthService;
import com.guilhempelissier.go4lunch.service.FirebaseService;

import java.util.ArrayList;
import java.util.List;

public class UsersRepository {
	private AuthService authService;
	private MutableLiveData<FirebaseUser> currentUser = new MutableLiveData<>();
	private MutableLiveData<Boolean> isUserConnected = new MutableLiveData<>();
	private MutableLiveData<List<User>> workmates = new MutableLiveData<>();

	//TODO rx plutot que livedata?
	public UsersRepository() {
		authService = DI.getAuthService();
		updateConnectedUser();

		//TODO FIX: aucune update apres le fetch original
		FirebaseService.getAllUsers().addOnSuccessListener(queryDocumentSnapshots -> {
			List<User> list = new ArrayList<>();
			for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
				User user = document.toObject(User.class);
				if (user!=null) {
					list.add(user);
				}
			}
			workmates.postValue(list);
		});
	}

	public LiveData<FirebaseUser> getCurrentUser() {
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
		currentUser.postValue(user);
		isUserConnected.postValue(connected);

		if (connected) {
			FirebaseService.createUser(user.getUid(), user.getDisplayName(),
					user.getPhotoUrl().toString());
		}
	}
}
