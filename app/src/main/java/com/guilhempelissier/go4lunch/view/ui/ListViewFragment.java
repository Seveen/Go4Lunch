package com.guilhempelissier.go4lunch.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guilhempelissier.go4lunch.R;
import com.guilhempelissier.go4lunch.model.FormattedRestaurant;
import com.guilhempelissier.go4lunch.view.adapter.RestaurantListAdapter;
import com.guilhempelissier.go4lunch.viewmodel.ListViewModel;

import java.util.Collections;
import java.util.List;


public class ListViewFragment extends Fragment {

	private RecyclerView recyclerView;
	private RestaurantListAdapter adapter;

	private ListViewModel listViewModel;

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
		listViewModel = ViewModelProviders.of(this).get(ListViewModel.class);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_list_view, null);

		recyclerView = root.findViewById(R.id.restaurantsRecyclerView);
		LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(layoutManager);
		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
		recyclerView.addItemDecoration(dividerItemDecoration);
		adapter = new RestaurantListAdapter(Collections.emptyList());
		recyclerView.setAdapter(adapter);


		List<FormattedRestaurant> initialData = listViewModel.getRestaurantsList().getValue();
		if (initialData != null) {
			adapter.setData(initialData);
		}
		listViewModel.getRestaurantsList().observe(getViewLifecycleOwner(), restaurants -> adapter.setData(restaurants));

		adapter.setOnClickRestaurantListener(id -> {
			listViewModel.setCurrentRestaurantId(id);
			Intent intent = new Intent(getActivity(), RestaurantActivity.class);
			startActivity(intent);
		});

		return root;
	}
}
