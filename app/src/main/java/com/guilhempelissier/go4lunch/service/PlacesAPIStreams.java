package com.guilhempelissier.go4lunch.service;

import android.location.Location;

import com.guilhempelissier.go4lunch.BuildConfig;
import com.guilhempelissier.go4lunch.model.Restaurant;
import com.guilhempelissier.go4lunch.model.serialization.DetailsResult;
import com.guilhempelissier.go4lunch.model.serialization.PlacesAutocompleteResponse;
import com.guilhempelissier.go4lunch.model.serialization.PlacesDetailsResponse;
import com.guilhempelissier.go4lunch.model.serialization.PlacesNearbyResponse;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.location.Location.FORMAT_DEGREES;

public class PlacesAPIStreams {
	public static String detailedFields =
			"formatted_phone_number,opening_hours,website";

	public static Observable<PlacesNearbyResponse> getRestaurantsAround(Location location, String radius) {
		String locationLatitude = Location.convert(location.getLatitude(),FORMAT_DEGREES);
		String locationLongitude = Location.convert(location.getLongitude(),FORMAT_DEGREES);
		String locationCoordinates = locationLatitude + "," + locationLongitude;

		PlacesAPIService placesAPIService = PlacesAPIService.retrofit.create(PlacesAPIService.class);
		return placesAPIService.getRestaurantsAround(
				locationCoordinates, radius, BuildConfig.PLACES_KEY, "restaurant")
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread());
	}

	public static Observable<PlacesDetailsResponse> getDetailsAboutRestaurant(String id) {
		PlacesAPIService placesAPIService = PlacesAPIService.retrofit.create(PlacesAPIService.class);

		return placesAPIService.getDetailsAboutRestaurant(id, BuildConfig.PLACES_KEY, detailedFields)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread());
	}

	public static Single<List<Restaurant>> getDetailedRestaurantsAround(Location location,
																		 String radius) {
		return getRestaurantsAround(location, radius)
				.flatMapIterable(PlacesNearbyResponse::getResults)
				.flatMap(result -> Observable.zip(Observable.just(result), getDetailsAboutRestaurant(result.getPlaceId()),
						((res, details) -> {
							DetailsResult detailsResult = details.getResult();
							return new Restaurant(res.getGeometry(), res.getName(), res.getPhotos(),
									res.getPlaceId(), res.getVicinity(), res.getRating(),
									detailsResult.getFormattedPhoneNumber(),
									detailsResult.getWebsite(),
									detailsResult.getOpeningHours());
						}
						)))
				.toList();
	}

	public static Observable<PlacesAutocompleteResponse> getPlaceAutocomplete(String input, Location location, String radius) {
		String locationLatitude = Location.convert(location.getLatitude(),FORMAT_DEGREES);
		String locationLongitude = Location.convert(location.getLongitude(),FORMAT_DEGREES);
		String locationCoordinates = locationLatitude + "," + locationLongitude;

		PlacesAPIService placesAPIService = PlacesAPIService.retrofit.create(PlacesAPIService.class);

		return placesAPIService.getAutocompleteResponse(input, BuildConfig.PLACES_KEY, locationCoordinates, radius, "establishment")
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread());
	}
}
