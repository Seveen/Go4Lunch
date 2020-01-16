package com.guilhempelissier.go4lunch.view.ui;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		restaurantViewModel = ViewModelProviders.of(this).get(RestaurantViewModel.class);

		binding = DataBindingUtil.setContentView(this, R.layout.restaurant_view);
		//TODO move binding in xml
		restaurantImageView = findViewById(R.id.restaurant_image_view);

		recyclerView = findViewById(R.id.restaurant_view_recyclerview);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		adapter = new WorkmatesListAdapter(Collections.emptyList(), false);
		recyclerView.setAdapter(adapter);

		restaurantViewModel.getCurrentRestaurant()
				.observe(this, restaurant -> {
					binding.setRestaurantNameStars(restaurant.getName() + " " + restaurant.getStars());
					binding.setRestaurantAddress(restaurant.getAddress());
					Glide.with(this)
							.load(restaurant.getImageUrl())
							.centerCrop()
							.into(restaurantImageView);
				});
		restaurantViewModel.getWorkmatesEatingThere()
				.observe(this, workmates -> adapter.setData(workmates));
	}
}
