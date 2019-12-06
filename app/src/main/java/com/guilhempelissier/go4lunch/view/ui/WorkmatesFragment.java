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
import com.guilhempelissier.go4lunch.view.adapter.WorkmatesListAdapter;
import com.guilhempelissier.go4lunch.viewmodel.FormattedRestaurant;
import com.guilhempelissier.go4lunch.viewmodel.FormattedWorkmate;

import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WorkmatesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WorkmatesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkmatesFragment extends Fragment {

	private OnFragmentInteractionListener mListener;

	private RecyclerView recyclerView;
	private WorkmatesListAdapter adapter;

	private List<FormattedWorkmate> dummylist = Arrays.asList(
			new FormattedWorkmate(
					"Hugh", "le zinc", "https://m.media-amazon.com/images/M/MV5BNDExMzIzNjk3Nl5BMl5BanBnXkFtZTcwOTE4NDU5OA@@._V1_.jpg"
			)
	);

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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_workmates, null);

		recyclerView = root.findViewById(R.id.workmatesRecyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		adapter = new WorkmatesListAdapter(dummylist, true);
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
		void onWorkmatesFragmentInteraction(Uri uri);
	}
}
