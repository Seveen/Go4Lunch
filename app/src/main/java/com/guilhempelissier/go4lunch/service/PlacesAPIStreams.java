package com.guilhempelissier.go4lunch.service;

import android.location.Location;

import com.guilhempelissier.go4lunch.BuildConfig;
import com.guilhempelissier.go4lunch.model.Restaurant;
import com.guilhempelissier.go4lunch.model.serialization.DetailsResult;
import com.guilhempelissier.go4lunch.model.serialization.PlacesAutocompleteResponse;
import com.guilhempelissier.go4lunch.model.serialization.PlacesDetailsResponse;
import com.guilhempelissier.go4lunch.model.serialization.PlacesNearbyResponse;
import com.guilhempelissier.go4lunch.utils.LatLngUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PlacesAPIStreams {

	private static Observable<PlacesNearbyResponse> getRestaurantsAround(Location location, String radius) {
		String locationCoordinates = LatLngUtils.convertLocationToStringCoordinates(location);

		PlacesAPIService placesAPIService = PlacesAPIService.retrofit.create(PlacesAPIService.class);
		return placesAPIService.getRestaurantsAround(
				locationCoordinates, radius, BuildConfig.PLACES_KEY, "restaurant")
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread());
	}

	private static Observable<PlacesDetailsResponse> getDetailsAboutRestaurant(String id) {
		PlacesAPIService placesAPIService = PlacesAPIService.retrofit.create(PlacesAPIService.class);
		String detailedFields = "geometry,name,photos,vicinity,rating,formatted_phone_number,opening_hours,website";

		return placesAPIService.getDetailsAboutRestaurant(id, BuildConfig.PLACES_KEY, detailedFields)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread());
	}

	public static Single<List<Restaurant>> getPlaceAutocomplete(String input, Location location, String radius) {
		String locationCoordinates = LatLngUtils.convertLocationToStringCoordinates(location);

		PlacesAPIService placesAPIService = PlacesAPIService.retrofit.create(PlacesAPIService.class);
		return placesAPIService.getAutocompleteResponse(input, BuildConfig.PLACES_KEY, locationCoordinates, radius, "establishment")
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.flatMapIterable(PlacesAutocompleteResponse::getPredictions)
				.filter(autocompleteResult -> autocompleteResult.getTypes().contains("restaurant"))
				.flatMap(autocompleteResult -> Observable.zip(
						Observable.just(autocompleteResult),
						getDetailsAboutRestaurant(autocompleteResult.getPlaceId())
								.filter(placesDetailsResponse -> placesDetailsResponse.getResult() != null),
						((res, details) -> createRestaurantFromResult(res.getPlaceId(), details.getResult()))))
				.toList();
	}

	public static Single<List<Restaurant>> getDetailedRestaurantsAround(Location location,
																		 String radius) {
		return getRestaurantsAround(location, radius)
				.flatMapIterable(PlacesNearbyResponse::getResults)
				.flatMap(result -> Observable.zip(
							Observable.just(result),
							getDetailsAboutRestaurant(result.getPlaceId()).filter(placesDetailsResponse -> placesDetailsResponse.getResult() != null),
						((res, details) -> createRestaurantFromResult(res.getPlaceId(), details.getResult()))))
				.toList();
	}

	public static Observable<Restaurant> getRestaurant(String id) {
		return getDetailsAboutRestaurant(id)
				.filter(placesDetailsResponse -> placesDetailsResponse.getResult() != null)
				.map(placesDetailsResponse -> createRestaurantFromResult(id, placesDetailsResponse.getResult()));
	}

	public static Single<List<Restaurant>> getRestaurants(List<String> ids) {
		return Observable.just(ids)
				.flatMapIterable(id -> id)
				.flatMap(PlacesAPIStreams::getRestaurant)
				.toList();
	}

	private static Restaurant createRestaurantFromResult(String id, DetailsResult detailsResult) {
		return new Restaurant(detailsResult.getGeometry(), detailsResult.getName(), detailsResult.getPhotos(),
				id, detailsResult.getVicinity(), detailsResult.getRating(),
				detailsResult.getFormattedPhoneNumber(),
				detailsResult.getWebsite(),
				detailsResult.getOpeningHours());
	}
}
