package com.guilhempelissier.go4lunch.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.guilhempelissier.go4lunch.di.DI;
import com.guilhempelissier.go4lunch.model.FormattedUser;
import com.guilhempelissier.go4lunch.repository.UsersRepository;

public class AuthViewModel extends AndroidViewModel {
	UsersRepository usersRepository;

	public AuthViewModel(@NonNull Application application) {
		super(application);
		usersRepository = DI.getUsersRepository();
	}

	public LiveData<FormattedUser> getCurrentUser() {
		return Transformations.map(usersRepository.getCurrentUser(),
			user -> {
				if (user == null) {
					return FormattedUser.defaultUser();
				} else {
					return new FormattedUser(user.getEmail(), user.getDisplayName(), user.getPhotoUrl().toString());
				}
			}
		);
	}

	public LiveData<Boolean> isUserConnected() {
		return usersRepository.isUserConnected();
	}

	public void disconnectCurrentUser() {
		usersRepository.disconnectCurrentUser();
	}

	public void updateCurrentUser() {
		usersRepository.updateConnectedUser();
	}
}
