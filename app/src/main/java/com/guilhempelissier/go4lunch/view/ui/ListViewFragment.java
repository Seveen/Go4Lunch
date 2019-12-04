package com.guilhempelissier.go4lunch.view.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guilhempelissier.go4lunch.R;
import com.guilhempelissier.go4lunch.view.adapter.RestaurantListAdapter;
import com.guilhempelissier.go4lunch.viewmodel.FormattedRestaurant;

import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.internal.util.ArrayListSupplier;


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

	private List<FormattedRestaurant> dummylist = Arrays.asList(
			new FormattedRestaurant(
					"Name1", "123 rue 465", "open", "100m", "3", "*", "https://media-cdn.tripadvisor.com/media/photo-s/12/c1/c3/f5/restaurant-araz.jpg"
			),
			new FormattedRestaurant(
					"Name2", "123 rue 465", "open", "100m", "3", "*", "https://media-cdn.tripadvisor.com/media/photo-s/12/c1/c3/f5/restaurant-araz.jpg"
			)
	);

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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_list_view, null);

		recyclerView = root.findViewById(R.id.restaurantsRecyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		adapter = new RestaurantListAdapter(dummylist);
		recyclerView.setAdapter(adapter);

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
