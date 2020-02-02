package com.guilhempelissier.go4lunch;

import com.guilhempelissier.go4lunch.model.FormattedRestaurant;
import com.guilhempelissier.go4lunch.repository.PlacesRepository;
import com.guilhempelissier.go4lunch.utils.SortingUtils;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class SortingTest {

	@Test
	public void restaurant_sorting_isCorrect() {
		FormattedRestaurant restaurant1 = new FormattedRestaurant(
				"","","","","",
				100,
				3,
				"",null,
				Arrays.asList("Keanu"),
				false,false,"",""
		);

		FormattedRestaurant restaurant2 = new FormattedRestaurant(
				"","","","","",
				150,
				2,
				"",null,
				Collections.emptyList(),
				false,false,"",""
		);

		FormattedRestaurant restaurant3 = new FormattedRestaurant(
				"","","","","",
				200,
				1,
				"",null,
				Arrays.asList("Keanu", "Hugh"),
				false,false,"",""
		);

		List<FormattedRestaurant> restaurants = Arrays.asList(restaurant1, restaurant2, restaurant3);

		SortingUtils.sortRestaurantsBy(restaurants, PlacesRepository.Sorting.DistanceLeast);
		assertEquals(Arrays.asList(restaurant1, restaurant2, restaurant3), restaurants);

		SortingUtils.sortRestaurantsBy(restaurants, PlacesRepository.Sorting.DistanceMost);
		assertEquals(Arrays.asList(restaurant3, restaurant2, restaurant1), restaurants);

		SortingUtils.sortRestaurantsBy(restaurants, PlacesRepository.Sorting.WorkmatesLeast);
		assertEquals(Arrays.asList(restaurant2, restaurant1, restaurant3), restaurants);

		SortingUtils.sortRestaurantsBy(restaurants, PlacesRepository.Sorting.WorkmatesMost);
		assertEquals(Arrays.asList(restaurant3, restaurant1, restaurant2), restaurants);

		SortingUtils.sortRestaurantsBy(restaurants, PlacesRepository.Sorting.RatingLeast);
		assertEquals(Arrays.asList(restaurant3, restaurant2, restaurant1), restaurants);

		SortingUtils.sortRestaurantsBy(restaurants, PlacesRepository.Sorting.RatingMost);
		assertEquals(Arrays.asList(restaurant1, restaurant2, restaurant3), restaurants);

	}
}
