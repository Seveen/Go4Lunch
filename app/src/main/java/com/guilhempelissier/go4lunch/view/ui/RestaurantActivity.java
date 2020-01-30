package com.guilhempelissier.go4lunch.view.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.guilhempelissier.go4lunch.R;
import com.guilhempelissier.go4lunch.databinding.RestaurantViewBinding;
import com.guilhempelissier.go4lunch.view.adapter.WorkmatesListAdapter;
import com.guilhempelissier.go4lunch.viewmodel.RestaurantViewModel;

public class RestaurantActivity extends AppCompatActivity {
	private RestaurantViewBinding binding;

	private RecyclerView recyclerView;
	private WorkmatesListAdapter adapter;

	private RestaurantViewModel restaurantViewModel;

	private String phoneNumber;
	private String website;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		restaurantViewModel = ViewModelProviders.of(this).get(RestaurantViewModel.class);

		binding = DataBindingUtil.setContentView(this, R.layout.restaurant_view);

		recyclerView = binding.restaurantViewRecyclerview;
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		recyclerView.setLayoutManager(layoutManager);
		DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
		recyclerView.addItemDecoration(dividerItemDecoration);
		adapter = new WorkmatesListAdapter(false, getApplicationContext());
		recyclerView.setAdapter(adapter);

		restaurantViewModel.getCurrentRestaurant()
				.observe(this, restaurant -> {
					binding.setRestaurant(restaurant);
					Glide.with(this)
							.load(restaurant.getImageUrl())
							.centerCrop()
							.into(binding.restaurantImageView);
					phoneNumber = restaurant.getPhoneNumber();
					website = restaurant.getWebsite();
				});
		restaurantViewModel.getWorkmatesEatingThere()
				.observe(this, workmates -> adapter.setData(workmates));

		binding.restaurantEatHereFab.setOnClickListener(view -> restaurantViewModel.toggleEatingLunchHere());
		binding.restaurantLikeButton.setOnClickListener(view -> restaurantViewModel.toggleLikeRestaurant());
		binding.restaurantCallButton.setOnClickListener(view -> callRestaurant());
		binding.restaurantWebsiteButton.setOnClickListener(view -> visitWebsite());
	}

	public void callRestaurant() {
		if (!phoneNumber.equals("")) {
			Intent intent = new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:" + phoneNumber));

			startActivity(intent);
		}
	}

	public void visitWebsite() {
		if (!website.equals("")) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(website));

			startActivity(intent);
		}
	}
}
