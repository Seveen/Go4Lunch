package com.guilhempelissier.go4lunch.service;

import com.google.android.libraries.places.api.model.Place;

import java.util.List;

import io.reactivex.Single;

public interface PlacesAPIService {
	Single<List<Place>> getRestaurantsAround();
	Single<Place> getDetailsAboutRestaurant(String id);
}
