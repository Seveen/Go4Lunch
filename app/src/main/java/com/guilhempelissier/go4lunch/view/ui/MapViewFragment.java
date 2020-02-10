package com.guilhempelissier.go4lunch.view.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.guilhempelissier.go4lunch.R;
import com.guilhempelissier.go4lunch.model.FormattedRestaurant;
import com.guilhempelissier.go4lunch.viewmodel.MapViewModel;

import java.util.ArrayList;
import java.util.List;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {
	private GoogleMap map;

	private MapViewModel mapViewModel;
	private Location currentLocation;

	private List<Marker> currentMarkers = new ArrayList<>();

	public MapViewFragment() {
		// Required empty public constructor
	}

	public static MapViewFragment newInstance() {
		return new MapViewFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if(getActivity() != null) {
			SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
			if (mapFragment != null) {
				mapFragment.getMapAsync(this);
			}
			FloatingActionButton centerButton = getView().findViewById(R.id.map_center_fab);
			centerButton.setOnClickListener(view -> centerMapOnCurrentLocation());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_map_view, container, false);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
		map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.style_json));

		List<FormattedRestaurant> initialData = mapViewModel.getRestaurants().getValue();
		if (initialData != null) {
			updateMarkers(initialData);
		}
		mapViewModel.getRestaurants().observe(getViewLifecycleOwner(), this::updateMarkers);

		Location initialLocation = mapViewModel.getCurrentLocation().getValue();
		if (initialLocation != null) {
			currentLocation = initialLocation;
			centerMapOnCurrentLocation();
		}
		mapViewModel.getCurrentLocation().observe(getViewLifecycleOwner(), location -> {
			currentLocation = location;
			centerMapOnCurrentLocation();
		});

		map.setOnMarkerClickListener(marker -> {
			Object tag = marker.getTag();
			if (tag != null) {
				mapViewModel.setCurrentRestaurantId(tag.toString());
				Intent intent = new Intent(getActivity(), RestaurantActivity.class);
				startActivity(intent);
				return true;
			} else {
				return false;
			}
		});
	}

	private void updateMarkers(List<FormattedRestaurant> list) {
		clearMarkers();
		for (FormattedRestaurant restaurant : list) {
			LatLng latLng = restaurant.getLatLng();
			Bitmap icon;
			if (restaurant.getWorkmates().size() != 0) {
				icon = BitmapFactory.decodeResource(getResources(), R.drawable.green_marker);
			} else {
				icon = BitmapFactory.decodeResource(getResources(), R.drawable.red_marker);
			}

			Marker marker = map.addMarker(new MarkerOptions()
					.position(latLng)
					.icon(BitmapDescriptorFactory.fromBitmap(icon)));
			marker.setTag(restaurant.getId());
			currentMarkers.add(marker);
		}
	}

	private void clearMarkers() {
		for (Marker marker : currentMarkers) {
			marker.remove();
		}
		currentMarkers.clear();
	}

	private void centerMapOnLocation(Location location) {
		LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16));
	}

	private void centerMapOnCurrentLocation() {
		if(currentLocation != null) {
			centerMapOnLocation(currentLocation);
		}
	}
}
