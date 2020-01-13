package com.guilhempelissier.go4lunch.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.guilhempelissier.go4lunch.di.DI;
import com.guilhempelissier.go4lunch.repository.PlacesRepository;

public class MainViewModel extends AndroidViewModel {
	private String TAG = "PlaceVM";
	private PlacesRepository placesRepository;
	private MutableLiveData<Boolean> needsPermission = new MutableLiveData<>();

	@SuppressLint("CheckResult")
	public MainViewModel(@NonNull Application application) {
		super(application);
		placesRepository = DI.getPlacesRepository(application.getApplicationContext());

		placesRepository.getPermissionStatus()
			.subscribe(needsPermission::setValue);
	}

	public LiveData<Boolean> getNeedsPermission() {
		return needsPermission;
	}

	public void setNeedsPermission(boolean val) {
		needsPermission.setValue(val);
	}
}
