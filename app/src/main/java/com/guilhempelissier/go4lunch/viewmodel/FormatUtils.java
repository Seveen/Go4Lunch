package com.guilhempelissier.go4lunch.viewmodel;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.guilhempelissier.go4lunch.BuildConfig;
import com.guilhempelissier.go4lunch.model.FormattedRestaurant;
import com.guilhempelissier.go4lunch.model.serialization.AllResult;
import com.guilhempelissier.go4lunch.model.serialization.DetailsResult;
import com.guilhempelissier.go4lunch.model.serialization.Location_;
import com.guilhempelissier.go4lunch.model.serialization.NearbyResult;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class FormatUtils {
	public static String formatRating(Double rating) {
		StringBuilder result = new StringBuilder();
		for (int i=0; i<=rating.intValue(); i++) {
			result.append('*');
		}
		return result.toString();
	}

	public static String formatPhotoUrl(String ref) {
		StringBuilder result = new StringBuilder();
		result.append("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=");
		result.append(ref);
		result.append("&key=");
		result.append(BuildConfig.PLACES_KEY);

		return result.toString();
	}

	public static String formatOpenNow(Boolean open) {
		if (open == null) {
			return "No opening information";
		} else {
			return open ? "Open now" : "Closed";
		}
	}

	public static String formatDistance(float distance) {
		StringBuilder result = new StringBuilder();
		DecimalFormat format = new DecimalFormat("####");
		format.setRoundingMode(RoundingMode.DOWN);
		result.append(format.format(distance));
		result.append("m");
		return result.toString();
	}

	public static FormattedRestaurant formatAllResult(Location currentLocation, AllResult result) {
		DetailsResult details = result.getDetailsResult();
		NearbyResult nearby = result.getNearbyResult();

		Location_ restaurantLoc = nearby.getGeometry().getLocation();
		float[] distanceResult = new float[1];
		Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(),
				restaurantLoc.getLat(), restaurantLoc.getLng(), distanceResult);
		String distance = FormatUtils.formatDistance(distanceResult[0]);

		String openNow = FormatUtils.formatOpenNow(nearby.getOpeningHours().getOpenNow());

		return new FormattedRestaurant(result.getId(),
				details.getName(),
				details.getFormattedAddress(),
				openNow,
				distance,
				FormatUtils.formatRating(details.getRating()),
				FormatUtils.formatPhotoUrl(nearby.getPhotos().get(0).getPhotoReference()),
				new LatLng(restaurantLoc.getLat(), restaurantLoc.getLng())
		);
	}
}
