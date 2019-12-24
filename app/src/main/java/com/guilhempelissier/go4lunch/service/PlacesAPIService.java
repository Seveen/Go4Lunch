package com.guilhempelissier.go4lunch.service;



import com.guilhempelissier.go4lunch.model.PlacesResponse;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlacesAPIService {

	@GET("maps/api/place/nearbysearch/json")
	Observable<PlacesResponse> getRestaurantsAround(@Query("location") String location,
													@Query("radius") String radius,
													@Query("key") String key,
													@Query("type") String type);

	Single<PlacesResponse> getDetailsAboutRestaurant(String id);

	public static final Retrofit retrofit = new Retrofit.Builder()
			.baseUrl("https://maps.googleapis.com/")
			.addConverterFactory(GsonConverterFactory.create())
			.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
			.build();
}
