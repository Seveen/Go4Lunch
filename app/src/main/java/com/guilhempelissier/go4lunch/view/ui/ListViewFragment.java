package com.guilhempelissier.go4lunch.view.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guilhempelissier.go4lunch.R;
import com.guilhempelissier.go4lunch.view.adapter.RestaurantListAdapter;
import com.guilhempelissier.go4lunch.viewmodel.PlaceViewModel;

import java.util.Collections;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListViewFragment extends Fragment {

	private OnFragmentInteractionListener mListener;

	private RecyclerView recyclerView;
	private RestaurantListAdapter adapter;

	private PlaceViewModel placeViewModel;

//	private List<FormattedRestaurant> dummylist = Arrays.asList(
//			new FormattedRestaurant(
//					"Name1", "123 rue 465", "open", "100m", "3", "*", "https://media-cdn.tripadvisor.com/media/photo-s/12/c1/c3/f5/restaurant-araz.jpg", "2"
//			),
//			new FormattedRestaurant(
//					"Name2", "123 rue 465", "open", "100m", "3", "*", "https://media-cdn.tripadvisor.com/media/photo-s/12/c1/c3/f5/restaurant-araz.jpg", "2"
//			)
//	);

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

	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		void onListFragmentInteraction(Uri uri);
	}
}
