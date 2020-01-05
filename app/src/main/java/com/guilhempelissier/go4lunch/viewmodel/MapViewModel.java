package com.guilhempelissier.go4lunch.viewmodel;

import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.guilhempelissier.go4lunch.BuildConfig;
import com.guilhempelissier.go4lunch.di.DI;
import com.guilhempelissier.go4lunch.model.FormattedRestaurant;
import com.guilhempelissier.go4lunch.model.serialization.AllResult;
import com.guilhempelissier.go4lunch.model.serialization.DetailsResult;
import com.guilhempelissier.go4lunch.model.serialization.Location_;
import com.guilhempelissier.go4lunch.model.serialization.NearbyResult;
import com.guilhempelissier.go4lunch.repository.PlacesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class MapViewModel extends AndroidViewModel {
	private String TAG = "MapVM";
	private PlacesRepository placesRepository;
	private MutableLiveData<Boolean> needsPermission = new MutableLiveData<>();
	private MutableLiveData<List<FormattedRestaurant>> restaurantsList = new MutableLiveData<>();
	private MutableLiveData<Location> currentLocation = new MutableLiveData<>();

	public MapViewModel(@NonNull Application application) {
		super(application);
		placesRepository = DI.getPlacesRepository(application.getApplicationContext());

		//TODO quand les api call fonctionnent correctement, transformer DetailedResults en FormattedRestaurant
		placesRepository.getCurrentLocation()
				.doOnError(error -> {
					if (error instanceof SecurityException) {
						needsPermission.postValue(true);
					}
				})
				.retryWhen(attempts -> attempts.flatMap(i -> Observable.timer(1, TimeUnit.SECONDS)))
				.subscribe(location -> {
					currentLocation.postValue(location);
//					placesRepository.getRestaurantsAround(location, "1500")
//							.subscribe(response -> {
//								List<NearbyResult> results = response.getResults();
//								restaurantsList.postValue(results);
//								Log.d(TAG, "MapViewModel: found restaurant: " + response);
//							}, error -> Log.d(TAG, "Restaurants error: " + error.getMessage()));
					placesRepository.getDetailedRestaurantsAround(location, "1500")
							.subscribe(allResults -> {
								List<FormattedRestaurant> results = new ArrayList<>();
								for (AllResult result : allResults) {
									results.add(formatAllResult(result));
								}
								restaurantsList.postValue(results);
							}, error -> Log.d(TAG, "Detailed restaurants error: " + error.getMessage()));
				}, error -> Log.d(TAG, error.getMessage()));
	}

	public LiveData<List<FormattedRestaurant>> getRestaurantsList() {
		return restaurantsList;
	}

	public LiveData<Location> getCurrentLocation() {
		return currentLocation;
	}

	public LiveData<Boolean> getNeedsPermission() {
		return needsPermission;
	}

	public void setNeedsPermission(boolean val) {
		needsPermission.setValue(val);
	}

	private FormattedRestaurant formatAllResult(AllResult result) {
		DetailsResult details = result.getDetailsResult();
		NearbyResult nearby = result.getNearbyResult();
		Location currentLoc = currentLocation.getValue();
		Location_ restaurantLoc = nearby.getGeometry().getLocation();

		float[] distanceResult = new float[1];

		Location.distanceBetween(currentLoc.getLatitude(), currentLoc.getLongitude(),
				restaurantLoc.getLat(), restaurantLoc.getLng(), distanceResult);

		String distance = distanceResult[0] + "m";

		return new FormattedRestaurant(result.getId(),
				details.getName(),
				details.getFormattedAddress(),
				formatOpenNow(nearby.getOpeningHours().getOpenNow()),
				distance,
				formatRating(details.getRating()),
				formatPhotoUrl(nearby.getPhotos().get(0).getPhotoReference()),
				new LatLng(restaurantLoc.getLat(), restaurantLoc.getLng())
			);
	}

	private String formatRating(Double rating) {
		StringBuilder result = new StringBuilder();
		for (int i=0; i<rating.intValue(); i++) {
			result.append('*');
		}
		return result.toString();
	}

	private String formatPhotoUrl(String ref) {
		StringBuilder result = new StringBuilder();
		result.append("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=");
		result.append(ref);
		result.append("&key=");
		result.append(BuildConfig.PLACES_KEY);

		return result.toString();
	}

	private String formatOpenNow(Boolean open) {
		return open ? "Open now" : "Closed";
	}
}
