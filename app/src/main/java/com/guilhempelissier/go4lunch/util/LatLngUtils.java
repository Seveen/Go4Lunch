package com.guilhempelissier.go4lunch.util;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.RectangularBounds;

public class LatLngUtils {

	public static RectangularBounds getBoundsAround(Location location, int length) {
		double latRadian = Math.toRadians(location.getLatitude());

		double degLatKm = 110.574235;
		double degLongKm = 110.572833 * Math.cos(latRadian);
		double deltaLat = length / 1000.0 / degLatKm;
		double deltaLong = length / 1000.0 / degLongKm;

		double minLat = location.getLatitude() - deltaLat;
		double minLong = location.getLongitude() - deltaLong;
		double maxLat = location.getLatitude() + deltaLat;
		double maxLong = location.getLongitude() + deltaLong;

		LatLng minLatLng = new LatLng(minLat, minLong);
		LatLng maxLatLng = new LatLng(maxLat, maxLong);

		return RectangularBounds.newInstance(minLatLng, maxLatLng);
	}
}
