package com.guilhempelissier.go4lunch.view.ui;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.guilhempelissier.go4lunch.R;
import com.guilhempelissier.go4lunch.model.FormattedRestaurant;
import com.guilhempelissier.go4lunch.viewmodel.PlaceViewModel;

import java.util.List;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {
	private GoogleMap map;
	private FloatingActionButton centerButton;

	private PlaceViewModel placeViewModel;
	private Location currentLocation;

	public MapViewFragment() {
		// Required empty public constructor
	}

	public static MapViewFragment newInstance() {
		MapViewFragment fragment = new MapViewFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		placeViewModel = ViewModelProviders.of(this).get(PlaceViewModel.class);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if(getActivity() != null) {
			SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
			if (mapFragment != null) {
				mapFragment.getMapAsync(this);
			}
			centerButton = getView().findViewById(R.id.map_center_fab);
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

		List<FormattedRestaurant> initialData = placeViewModel.getRestaurantsList().getValue();
		if (initialData != null) {
			addMarkers(initialData);
		}
		placeViewModel.getRestaurantsList().observe(this, list -> {
			addMarkers(list);
		});

		Location initialLocation = placeViewModel.getCurrentLocation().getValue();
		if (initialLocation != null) {
			currentLocation = initialLocation;
			centerMapOnCurrentLocation();
		}
		placeViewModel.getCurrentLocation().observe(this, location -> {
			currentLocation = location;
			centerMapOnCurrentLocation();
		});
	}

	private void addMarkers(List<FormattedRestaurant> list) {
		for (FormattedRestaurant restaurant : list) {
			LatLng latLng = restaurant.getLatLng();
			map.addMarker(new MarkerOptions().position(latLng).title(restaurant.getName()));
		}
	}

	private void centerMapOnLocation(Location location) {
		LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16));
	}

	private void centerMapOnCurrentLocation() {
		if(currentLocation != null) {
			centerMapOnLocation(currentLocation);
		} else {
			placeViewModel.setNeedsPermission(true);
		}
	}
}
