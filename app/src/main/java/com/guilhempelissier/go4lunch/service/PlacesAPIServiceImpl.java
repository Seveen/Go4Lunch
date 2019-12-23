package com.guilhempelissier.go4lunch.service;

import android.Manifest;
import android.content.Context;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.guilhempelissier.go4lunch.BuildConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class PlacesAPIServiceImpl implements PlacesAPIService {
	private String TAG = "PlacesAPIService";

	private Context applicationContext;

	private PlacesClient placesClient;
	private List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ID, Place.Field.LAT_LNG, Place.Field.TYPES);

	public PlacesAPIServiceImpl(Context applicationContext) {
		this.applicationContext = applicationContext;
		Places.initialize(applicationContext, BuildConfig.PLACES_KEY);
		placesClient = Places.createClient(applicationContext);
	}

	//TODO changer du sdk vers des call api via http
	@Override
	public Single<List<Place>> getRestaurantsAround() {
		FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);
		Log.d(TAG, "Starting observable");
		if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
			Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);

			return Single.create(emitter -> placeResponse.addOnCompleteListener(task -> {
				if (task.isSuccessful()){
					List<Place> list = new ArrayList<>();
					Log.d(TAG, "Transaction finished");
//					FindCurrentPlaceResponse response = task.getResult();
//					for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
//						Place place = placeLikelihood.getPlace();
//						if (place.getTypes().contains(Place.Type.RESTAURANT)) {
//							list.add(place);
//						}
//					}
					emitter.onSuccess(list);
				} else {
					Exception exception = task.getException();
					emitter.onError(exception);
				}
			}));
		} else {
			return null;
		}
	}

	@Override
	public Single<Place> getDetailsAboutRestaurant(String id) {
		return null;
	}
}
