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

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class MapViewModel extends AndroidViewModel {
	private String TAG = "MapVM";
	private PlacesRepository placesRepository;
	private MutableLiveData<Boolean> needsPermission = new MutableLiveData<>();
	private MutableLiveData<List<FormattedRestaurant>> restaurantsList = new MutableLiveData<>();
	private MutableLiveData<Location> currentLocation = new MutableLiveData<>();

	public MapViewModel(@NonNull Application application) {
		super(application);
		placesRepository = DI.getPlacesRepository(application.getApplicationContext());

		Disposable disposable = placesRepository.getCurrentLocation()
				.doOnError(error -> {
					if (error instanceof SecurityException) {
						needsPermission.postValue(true);
					}
				})
				.retryWhen(attempts -> attempts.flatMap(i -> Observable.timer(1, TimeUnit.SECONDS)))
				.subscribe(location -> {
					Boolean updateLocation = false;

					if (currentLocation.getValue() != null) {
						Float distanceFromOldLocation = currentLocation.getValue().distanceTo(location);
						if (distanceFromOldLocation < 1500.0) {
							updateLocation = true;
						}
					} else {
						updateLocation = true;
					}

					if (updateLocation) {
						currentLocation.postValue(location);
						placesRepository.getDetailedRestaurantsAround(location, "1500")
								.subscribe(allResults -> {
									List<FormattedRestaurant> results = new ArrayList<>();
									for (AllResult result : allResults) {
										Log.d(TAG, "All results: " + result.getId());
										results.add(formatAllResult(result));
									}
									restaurantsList.postValue(results);
								}, error -> Log.d(TAG, "Detailed restaurants error: " + error.getMessage()));
					}
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

		String distance = formatDistance(distanceResult[0]);

		String openNow;
		if (nearby.getOpeningHours() != null) {
			openNow = formatOpenNow(nearby.getOpeningHours().getOpenNow());
		} else {
			openNow = "No opening information";
		}

		return new FormattedRestaurant(result.getId(),
				details.getName(),
				details.getFormattedAddress(),
				openNow,
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

	private String formatDistance(float distance) {
		StringBuilder result = new StringBuilder();
		DecimalFormat format = new DecimalFormat("####");
		format.setRoundingMode(RoundingMode.DOWN);
		result.append(format.format(distance));
		result.append("m");
		return result.toString();
	}
}
