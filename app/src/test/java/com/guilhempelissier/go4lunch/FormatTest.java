package com.guilhempelissier.go4lunch;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.guilhempelissier.go4lunch.model.FormattedRestaurant;
import com.guilhempelissier.go4lunch.model.Restaurant;
import com.guilhempelissier.go4lunch.model.User;
import com.guilhempelissier.go4lunch.model.serialization.DateTime;
import com.guilhempelissier.go4lunch.model.serialization.Geometry;
import com.guilhempelissier.go4lunch.model.serialization.Location_;
import com.guilhempelissier.go4lunch.model.serialization.OpeningHours;
import com.guilhempelissier.go4lunch.model.serialization.Period;
import com.guilhempelissier.go4lunch.model.serialization.Photo;
import com.guilhempelissier.go4lunch.utils.FormatUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

public class FormatTest {

	@Mock
	Context mockContext;
	@Mock
	Location mockCurrentLocation;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		//Mock strings resources
		when(mockContext.getString(R.string.openH24))
				.thenReturn("Open 24/7");
		when(mockContext.getString(R.string.open_until))
				.thenReturn("Open until");
		when(mockContext.getString(R.string.closed))
				.thenReturn("Closed. Opens at");
		when(mockContext.getString(R.string.no_opening_infos))
				.thenReturn("No opening information");
		//Mock current location
		when(mockCurrentLocation.getLatitude())
				.thenReturn(10.0);
		when(mockCurrentLocation.getLongitude())
				.thenReturn(10.0);

	}

	@Test
	public void rating_formatting_isCorrect() {
		for(double d=0; d<0.5; d += 0.1) {
			assertEquals(0, FormatUtils.formatRating(d));
		}

		for(double d=0.5; d<2.5; d += 0.1) {
			assertEquals(1, FormatUtils.formatRating(d));
		}

		for(double d=2.5; d<4.5; d += 0.1) {
			assertEquals(2, FormatUtils.formatRating(d));
		}

		for(double d=4.5; d<=5; d += 0.1) {
			assertEquals(3, FormatUtils.formatRating(d));
		}
	}

	@Test
	public void opening_hours_formatting_isCorrect() {
		//02-feb-2020 was a sunday
		LocalDateTime now = LocalDateTime.of(2020, 2, 2, 12, 1);

		OpeningHours openingHours = new OpeningHours();
		List<Period> periods = new ArrayList<>();

		periods.add(new Period(
				new DateTime(0, 1200), 		// opens sunday at 12:00
				new  DateTime(0, 1400)		// closes sunday at 14:00
		));
		openingHours.setPeriods(periods);

		assertEquals("Open until 14:00",
				FormatUtils.formatOpenNow(openingHours, now, mockContext));

		periods.clear();
		periods.add(new Period(
			new DateTime(0, 1400),		// opens sunday at 14:00
			new DateTime(0, 1700)		// closes sunday at 17:00
		));

		assertEquals("Closed. Opens at 14:00",
				FormatUtils.formatOpenNow(openingHours, now, mockContext));

		periods.clear();
		periods.add(new Period(
			new DateTime(0, 0), // Open on day 0 hour 0
				// with closing time null is the way Google represents always open stores.
			null
		));

		assertEquals("Open 24/7",
				FormatUtils.formatOpenNow(openingHours, now, mockContext));

		periods.clear();
		assertEquals("No opening information",
				FormatUtils.formatOpenNow(openingHours, now, mockContext));
	}

	@Test
	public void restaurant_formatting_isCorrect() {
		User currentUser = new User("id0", "John Doe", "www.example.com/photoUrl");

		LocalDateTime now = LocalDateTime.of(2020, 2, 2, 12, 1);
		OpeningHours openingHours = new OpeningHours();
		List<Period> periods = new ArrayList<>();
		periods.add(new Period(
				new DateTime(0, 1200), 		// opens sunday at 12:00
				new  DateTime(0, 1400)		// closes sunday at 14:00
		));
		openingHours.setPeriods(periods);

		Photo photo = new Photo();
		photo.setPhotoReference("photo-reference");

		Restaurant result = new Restaurant(
				new Geometry(new Location_(10.0, 10.0), null),
				"Restaurant test",
				Collections.singletonList(photo),
				"placeId0000",
				"14 rue du test",
				4.6,
				"0412345678",
				"www.example.com",
				openingHours);
		List<String> workmates = Arrays.asList("Hugh", "Keanu");

		String expectedImageUrl =
				"https://maps.googleapis.com/maps/api/place/photo?maxheight=400&photoreference=photo-reference&key="
				+BuildConfig.PLACES_KEY;
		FormattedRestaurant expected = new FormattedRestaurant(
				"placeId0000",
				"Restaurant test",
				"14 rue du test",
				"Open until 14:00",
				"0m",
				0,
				3,
				expectedImageUrl,
				new LatLng(10.0, 10.0),
				workmates,
				false,
				false,
				"0412345678",
				"www.example.com"
		);

		assertEquals(expected, FormatUtils.formatRestaurant(
				mockCurrentLocation,
				result,
				workmates,
				mockContext,
				currentUser,
				now));
	}
}


































