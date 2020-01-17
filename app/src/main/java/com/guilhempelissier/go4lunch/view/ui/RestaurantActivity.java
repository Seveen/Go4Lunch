package com.guilhempelissier.go4lunch.view.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.guilhempelissier.go4lunch.R;
import com.guilhempelissier.go4lunch.databinding.RestaurantViewBinding;
import com.guilhempelissier.go4lunch.view.adapter.WorkmatesListAdapter;
import com.guilhempelissier.go4lunch.viewmodel.RestaurantViewModel;

import java.util.Collections;

public class RestaurantActivity extends AppCompatActivity {
	private RestaurantViewBinding binding;

	private RecyclerView recyclerView;
	private WorkmatesListAdapter adapter;

	private ImageView restaurantImageView;

	private RestaurantViewModel restaurantViewModel;

	private String phoneNumber;
	private String website;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		restaurantViewModel = ViewModelProviders.of(this).get(RestaurantViewModel.class);

		binding = DataBindingUtil.setContentView(this, R.layout.restaurant_view);
		//TODO move binding in xml
		restaurantImageView = findViewById(R.id.restaurant_image_view);

		recyclerView = findViewById(R.id.restaurant_view_recyclerview);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		adapter = new WorkmatesListAdapter(Collections.emptyList(), false, getApplicationContext());
		recyclerView.setAdapter(adapter);

		restaurantViewModel.getCurrentRestaurant()
				.observe(this, restaurant -> {
					binding.setRestaurantNameStars(restaurant.getName() + " " + restaurant.getStars());
					binding.setRestaurantAddress(restaurant.getAddress());
					Glide.with(this)
							.load(restaurant.getImageUrl())
							.centerCrop()
							.into(restaurantImageView);
					binding.setIsUserEatingHere(restaurant.isMyLunch());
					binding.setIsRestaurantLiked(restaurant.isLikedByCurrentUser());
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
