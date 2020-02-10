package com.guilhempelissier.go4lunch.viewmodel;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.guilhempelissier.go4lunch.BuildConfig;
import com.guilhempelissier.go4lunch.R;
import com.guilhempelissier.go4lunch.model.FormattedRestaurant;
import com.guilhempelissier.go4lunch.model.Restaurant;
import com.guilhempelissier.go4lunch.model.User;
import com.guilhempelissier.go4lunch.model.serialization.Location_;
import com.guilhempelissier.go4lunch.model.serialization.OpeningHours;
import com.guilhempelissier.go4lunch.model.serialization.Period;
import com.guilhempelissier.go4lunch.utils.LatLngUtils;

import org.threeten.bp.LocalDateTime;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class FormatUtils {
	public static int formatRating(Double rating) {
		int result = 0;
		if (rating >= 4.5) {
			result = 3;
		} else if (rating >= 2.5) {
			result = 2;
		} else if (rating >= 0.5) {
			result = 1;
		}
		return result;
	}

	public static String formatPhotoUrl(String ref) {
		StringBuilder result = new StringBuilder();
		result.append("https://maps.googleapis.com/maps/api/place/photo?maxheight=400&photoreference=");
		result.append(ref);
		result.append("&key=");
		result.append(BuildConfig.PLACES_KEY);

		return result.toString();
	}

	public static String formatOpenNow(OpeningHours openingHours, LocalDateTime now, Context ctx) {
		List<Period> periods = openingHours.getPeriods();

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

	public static String formatDistance(double distance) {
		StringBuilder result = new StringBuilder();
		DecimalFormat format = new DecimalFormat("####");
		format.setRoundingMode(RoundingMode.DOWN);
		result.append(format.format(distance));
		result.append("m");
		return result.toString();
	}

	public static boolean formatIsLunch(User user, Restaurant restaurant) {
		if (user != null) {
			return restaurant.getPlaceId().equals(user.getLunch());
		}
		return false;
	}

	public static boolean formatIsLiked(User user, Restaurant restaurant) {
		if (user != null) {
			return user.getLikedRestaurants().contains(restaurant.getPlaceId());
		}
		return false;
	}

	public static FormattedRestaurant formatRestaurant(Location currentLocation, Restaurant result, List<String> workmates, Context ctx, User currentUser,
													   LocalDateTime currentDateTime) {

		Location_ restaurantLoc = result.getGeometry().getLocation();

		double distanceMeters = LatLngUtils.getDistanceInMetersBetweenTwoLatLng(
				currentLocation.getLatitude(), currentLocation.getLongitude(),
				restaurantLoc.getLat(), restaurantLoc.getLng());

		String distance = FormatUtils.formatDistance(distanceMeters);

		OpeningHours openingHours = result.getOpeningHours();

		String openNow;
		if (openingHours != null) {
			openNow = FormatUtils.formatOpenNow(openingHours, currentDateTime, ctx);
		} else {
			openNow = ctx.getString(R.string.no_opening_infos);
		}

		String photoReference = "";
		if (result.getPhotos() != null) {
			if (result.getPhotos().get(0) != null) {
				photoReference = result.getPhotos().get(0).getPhotoReference();
			}
		}

		return new FormattedRestaurant(result.getPlaceId(),
				result.getName(),
				result.getVicinity(),
				openNow,
				distance,
				Math.round(distanceMeters),
				FormatUtils.formatRating(result.getRating()),
				FormatUtils.formatPhotoUrl(photoReference),
				new LatLng(restaurantLoc.getLat(), restaurantLoc.getLng()),
				workmates,
				FormatUtils.formatIsLunch(currentUser, result),
				FormatUtils.formatIsLiked(currentUser, result),
				result.getFormattedPhoneNumber(),
				result.getWebsite()
		);
	}
}
