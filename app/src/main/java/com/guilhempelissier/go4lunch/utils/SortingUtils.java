package com.guilhempelissier.go4lunch.utils;

import com.guilhempelissier.go4lunch.model.FormattedRestaurant;
import com.guilhempelissier.go4lunch.repository.PlacesRepository;

import java.util.Collections;
import java.util.List;

public class SortingUtils {

	public static void sortRestaurantsBy(List<FormattedRestaurant> restaurants, PlacesRepository.Sorting sorting) {
		switch (sorting) {
			case RatingLeast:
				Collections.sort(restaurants, (a, b) -> Integer.compare(a.getStars(), b.getStars()));
				break;
			case RatingMost:
				Collections.sort(restaurants, (a, b) -> -Integer.compare(a.getStars(), b.getStars()));
				break;
			case DistanceLeast:
				Collections.sort(restaurants, (a, b) -> Long.compare(a.getDistance(), b.getDistance()));
				break;
			case DistanceMost:
				Collections.sort(restaurants, (a, b) -> -Long.compare(a.getDistance(), b.getDistance()));
				break;
			case WorkmatesLeast:
				Collections.sort(restaurants, (a, b) -> Integer.compare(a.getWorkmates().size(), b.getWorkmates().size()));
				break;
			case WorkmatesMost:
				Collections.sort(restaurants, (a, b) -> -Integer.compare(a.getWorkmates().size(), b.getWorkmates().size()));
				break;
		}
	}
}
