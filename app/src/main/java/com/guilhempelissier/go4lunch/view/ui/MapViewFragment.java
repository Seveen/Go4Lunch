package com.guilhempelissier.go4lunch.view.ui;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
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
import com.guilhempelissier.go4lunch.model.Location_;
import com.guilhempelissier.go4lunch.model.Result;
import com.guilhempelissier.go4lunch.viewmodel.MapViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapViewFragment extends Fragment implements OnMapReadyCallback {

	private OnFragmentInteractionListener mListener;
	private GoogleMap map;
	private FloatingActionButton centerButton;

	private MapViewModel mapViewModel;
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
			centerButton = getView().findViewById(R.id.map_center_fab);
			centerButton.setOnClickListener(view -> centerMapOnCurrentLocation());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_map_view, container, false);
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onMapFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			mListener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
		map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.style_json));

		mapViewModel.getRestaurantsList().observe(this, list -> {
			for (Result restaurant : list) {
				Location_ location = restaurant.getGeometry().getLocation();
				LatLng latLng = new LatLng(location.getLat(), location.getLng());
				map.addMarker(new MarkerOptions().position(latLng).title(restaurant.getName()));
			}
		});

		mapViewModel.getCurrentLocation().observe(this, location -> currentLocation = location);
	}

	private void centerMapOnLocation(Location location) {
		LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16));
	}

	private void centerMapOnCurrentLocation() {
		centerMapOnLocation(currentLocation);
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		void onMapFragmentInteraction(Uri uri);
	}
}
