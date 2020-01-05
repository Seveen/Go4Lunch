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
import com.guilhempelissier.go4lunch.model.User;
import com.guilhempelissier.go4lunch.repository.UsersRepository;

import java.util.ArrayList;
import java.util.List;

public class WorkmatesViewModel extends AndroidViewModel {
	private String TAG = "WorkmatesVM";
	private UsersRepository usersRepository;
	private MutableLiveData<List<FormattedWorkmate>> workmates = new MutableLiveData<>();

	public WorkmatesViewModel(@NonNull Application application) {
		super(application);
		usersRepository = DI.getUsersRepository();
	}

	public LiveData<List<FormattedWorkmate>> getWorkmates() {
		MediatorLiveData fusedData = new MediatorLiveData<List<FormattedWorkmate>>();
		fusedData.addSource(usersRepository.getCurrentUser(), o -> updateWorkmatesList(fusedData));
		fusedData.addSource(usersRepository.getWorkmates(), o -> updateWorkmatesList(fusedData));

		return fusedData;
	}

	private void updateWorkmatesList(MediatorLiveData<List<FormattedWorkmate>> fusedData) {
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

		fusedData.setValue(list);
	}
}
