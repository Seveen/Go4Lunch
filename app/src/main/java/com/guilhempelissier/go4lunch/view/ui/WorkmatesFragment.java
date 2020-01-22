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
import com.guilhempelissier.go4lunch.view.adapter.WorkmatesListAdapter;
import com.guilhempelissier.go4lunch.viewmodel.WorkmatesViewModel;

public class WorkmatesFragment extends Fragment {

	private RecyclerView recyclerView;
	private WorkmatesListAdapter adapter;

	private WorkmatesViewModel workmatesViewModel;

	public WorkmatesFragment() {
		// Required empty public constructor
	}

	public static WorkmatesFragment newInstance() {
		WorkmatesFragment fragment = new WorkmatesFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		workmatesViewModel = ViewModelProviders.of(this).get(WorkmatesViewModel.class);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_workmates, null);

		recyclerView = root.findViewById(R.id.workmatesRecyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		adapter = new WorkmatesListAdapter(true, getContext());
		recyclerView.setAdapter(adapter);

		workmatesViewModel.getWorkmates().observe(getViewLifecycleOwner(), workmates -> adapter.setData(workmates));

		adapter.setListener(placeId -> {
			if (placeId != null) {
				workmatesViewModel.setCurrentRestaurantId(placeId);
				Intent intent = new Intent(getActivity(), RestaurantActivity.class);
				startActivity(intent);
			}
		});

		return root;
	}
}
