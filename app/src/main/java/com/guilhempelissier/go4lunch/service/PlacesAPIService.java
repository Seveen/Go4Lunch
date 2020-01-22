package com.guilhempelissier.go4lunch.service;

import com.guilhempelissier.go4lunch.model.serialization.PlacesAutocompleteResponse;
import com.guilhempelissier.go4lunch.model.serialization.PlacesDetailsResponse;
import com.guilhempelissier.go4lunch.model.serialization.PlacesNearbyResponse;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesAPIService {

	@GET("maps/api/place/nearbysearch/json")
	Observable<PlacesNearbyResponse> getRestaurantsAround(@Query("location") String location,
														  @Query("radius") String radius,
														  @Query("key") String key,
														  @Query("type") String type);

	@GET("maps/api/place/details/json")
	Observable<PlacesDetailsResponse> getDetailsAboutRestaurant(@Query("place_id") String id,
																@Query("key") String key,
																@Query("fields") String fields);

	@GET("maps/api/place/autocomplete/json?strictbounds")
	Observable<PlacesAutocompleteResponse> getAutocompleteResponse(@Query("input") String input,
																   @Query("key") String key,
																   @Query("location") String location,
																   @Query("radius") String radius,
																   @Query("types") String types);

	public static final Retrofit retrofit = new Retrofit.Builder()
			.baseUrl("https://maps.googleapis.com/")
			.addConverterFactory(GsonConverterFactory.create())
			.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
			.build();
}
