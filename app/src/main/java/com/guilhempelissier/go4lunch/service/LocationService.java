package com.guilhempelissier.go4lunch.service;

import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import io.reactivex.Observable;

public class LocationService {
	Observable<Location> lastLocation;

	public LocationService(Context applicationContext) {
		FusedLocationProviderClient locationClient = LocationServices.getFusedLocationProviderClient(applicationContext);

		lastLocation = Observable.create(emitter -> {
			LocationCallback locationCallback = new LocationCallback() {
				@Override
				public void onLocationResult(LocationResult locationResult) {
					Log.d("Location Service", "onLocationResult: new location result");
					emitter.onNext(locationResult.getLastLocation());
				}
			};
			LocationRequest locationRequest = LocationRequest.create();
			locationRequest.setInterval(1000);
			locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
			locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
		});
	}

	public Observable<Location> getObservableLocation() {
		return lastLocation;
	}
}
