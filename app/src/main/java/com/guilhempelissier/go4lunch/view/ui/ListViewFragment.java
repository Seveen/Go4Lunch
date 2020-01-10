package com.guilhempelissier.go4lunch.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guilhempelissier.go4lunch.R;
import com.guilhempelissier.go4lunch.model.FormattedRestaurant;
import com.guilhempelissier.go4lunch.view.adapter.RestaurantListAdapter;
import com.guilhempelissier.go4lunch.viewmodel.PlaceViewModel;

import java.util.Collections;
import java.util.List;


public class ListViewFragment extends Fragment {

	private RecyclerView recyclerView;
	private RestaurantListAdapter adapter;

	private PlaceViewModel placeViewModel;

	public ListViewFragment() {
		// Required empty public constructor
	}

	public static ListViewFragment newInstance() {
		ListViewFragment fragment = new ListViewFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		placeViewModel = ViewModelProviders.of(this).get(PlaceViewModel.class);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_list_view, null);



		recyclerView = root.findViewById(R.id.restaurantsRecyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		adapter = new RestaurantListAdapter(Collections.emptyList());
		recyclerView.setAdapter(adapter);

		List<FormattedRestaurant> initialData = placeViewModel.getRestaurantsList().getValue();
		if (initialData != null) {
			adapter.setData(initialData);
		}
		placeViewModel.getRestaurantsList().observe(this, restaurants -> {
			adapter.setData(restaurants);
		});

		adapter.setOnClickRestaurantListener(id -> {
			//TODO switch selected restaurant in vm
			Intent intent = new Intent(getActivity(), RestaurantActivity.class);
			startActivity(intent);
		});

		return root;
	}
}
