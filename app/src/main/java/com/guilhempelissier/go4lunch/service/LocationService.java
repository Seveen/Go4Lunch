package com.guilhempelissier.go4lunch.service;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class LocationService {
	private Observable<Location> lastLocation;

	public LocationService(Context applicationContext) {
		FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(applicationContext);

		lastLocation = Observable.create(emitter -> {
			if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
				emitter.onError(new SecurityException("Permission to access location needed"));
			} else {
				LocationCallback locationCallback = new LocationCallback() {
					@Override
					public void onLocationResult(LocationResult locationResult) {
						Log.d("Location Service", "onLocationResult: new location result");
						emitter.onNext(locationResult.getLastLocation());
					}
				};
				LocationRequest locationRequest = LocationRequest.create();
				locationRequest.setInterval(10000);
				locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
				locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
			}
		});
	}

	public Observable<Location> getObservableLocation() {
		return lastLocation
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}
}
