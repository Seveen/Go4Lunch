package com.guilhempelissier.go4lunch.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.guilhempelissier.go4lunch.di.DI;
import com.guilhempelissier.go4lunch.model.FormattedWorkmate;
import com.guilhempelissier.go4lunch.model.User;
import com.guilhempelissier.go4lunch.repository.UsersRepository;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesViewModel extends AndroidViewModel {
	private String TAG = "WorkmatesVM";
	private UsersRepository usersRepository;
	private MediatorLiveData<List<FormattedWorkmate>> formattedWorkmates = new MediatorLiveData<>();

	public WorkmatesViewModel(@NonNull Application application) {
		super(application);
		usersRepository = DI.getUsersRepository();

		formattedWorkmates.addSource(usersRepository.getCurrentUser(), o -> updateWorkmatesList());
		formattedWorkmates.addSource(usersRepository.getWorkmates(), o -> updateWorkmatesList());
	}

	public LiveData<List<FormattedWorkmate>> getWorkmates() {
		return formattedWorkmates;
	}

	private void updateWorkmatesList() {
		List<FormattedWorkmate> list = new ArrayList<>();
		List<User> workmates = usersRepository.getWorkmates().getValue();

		if (workmates!=null) {
			for (User workmate : workmates) {
				FirebaseUser currentUser = usersRepository.getCurrentUser().getValue();
				if (currentUser != null && !workmate.getUid().equals(currentUser.getUid())) {
					list.add(new FormattedWorkmate(workmate.getUsername(),
							workmate.getLunch(),
							workmate.getImageUrl()));
				}
			}
		}

		formattedWorkmates.setValue(list);
	}
}
