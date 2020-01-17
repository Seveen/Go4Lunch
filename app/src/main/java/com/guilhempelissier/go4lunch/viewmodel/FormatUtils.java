package com.guilhempelissier.go4lunch.viewmodel;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.guilhempelissier.go4lunch.BuildConfig;
import com.guilhempelissier.go4lunch.R;
import com.guilhempelissier.go4lunch.model.FormattedRestaurant;
import com.guilhempelissier.go4lunch.model.Restaurant;
import com.guilhempelissier.go4lunch.model.serialization.Location_;
import com.guilhempelissier.go4lunch.model.serialization.OpeningHours;
import com.guilhempelissier.go4lunch.model.serialization.Period;

import org.threeten.bp.LocalDateTime;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class FormatUtils {
	public static int formatRating(Double rating) {
		return (int) Math.round(rating/2.0);
	}

	public static String formatPhotoUrl(String ref) {
		StringBuilder result = new StringBuilder();
		result.append("https://maps.googleapis.com/maps/api/place/photo?maxheight=400&photoreference=");
		result.append(ref);
		result.append("&key=");
		result.append(BuildConfig.PLACES_KEY);

		return result.toString();
	}

	public static String formatOpenNow(OpeningHours openingHours, Context ctx) {
		List<Period> periods = openingHours.getPeriods();

		LocalDateTime now = LocalDateTime.now();
		int currentDay = now.getDayOfWeek().getValue();
		if (currentDay == 7) {
			currentDay = 0;
		}
		int currentHour = now.getHour();
		int currentMinute = now.getMinute();
		int currentHourMinute = currentHour * 100 + currentMinute;

		for (Period period : periods) {
			boolean isAlwaysOpen = period.getOpen().getDay() == 0
					&& period.getOpen().getTime() == 0
					&& period.getClose() == null;

			if (isAlwaysOpen) {
				return ctx.getString(R.string.openH24);
			}

			if (period.getOpen().getDay() == currentDay) {
				if (period.getOpen().getTime() <= currentHourMinute) {
					return ctx.getString(R.string.open_until) + " " + formatTime(period.getClose().getTime());
				} else {
					return ctx.getString(R.string.closed) + " " + formatTime(period.getOpen().getTime());
				}
			}
		}
		return ctx.getString(R.string.no_opening_infos);
	}

	public static String formatTime(int time) {
		DecimalFormat hourFormat = new DecimalFormat("##");
		DecimalFormat minutesFormat = new DecimalFormat("00");

		int hours = time/100;
		int minutes = time - (hours * 100);
		return hourFormat.format(hours) + ":" + minutesFormat.format(minutes);
	}

	public static String formatDistance(float distance) {
		StringBuilder result = new StringBuilder();
		DecimalFormat format = new DecimalFormat("####");
		format.setRoundingMode(RoundingMode.DOWN);
		result.append(format.format(distance));
		result.append("m");
		return result.toString();
	}

	public static FormattedRestaurant formatRestaurant(Location currentLocation, Restaurant result, List<String> workmates, Context ctx) {

		Location_ restaurantLoc = result.getGeometry().getLocation();
		float[] distanceResult = new float[1];
		Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(),
				restaurantLoc.getLat(), restaurantLoc.getLng(), distanceResult);
		String distance = FormatUtils.formatDistance(distanceResult[0]);

		OpeningHours openingHours = result.getOpeningHours();

		String openNow;
		if (openingHours != null) {
			openNow = FormatUtils.formatOpenNow(openingHours, ctx);
		} else {
			openNow = ctx.getString(R.string.no_opening_infos);
		}

		return new FormattedRestaurant(result.getPlaceId(),
				result.getName(),
				result.getVicinity(),
				openNow,
				distance,
				FormatUtils.formatRating(result.getRating()),
				FormatUtils.formatPhotoUrl(result.getPhotos().get(0).getPhotoReference()),
				new LatLng(restaurantLoc.getLat(), restaurantLoc.getLng()),
				workmates
		);
	}
}
