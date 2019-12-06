package com.guilhempelissier.go4lunch.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.guilhempelissier.go4lunch.R;
import com.guilhempelissier.go4lunch.databinding.RestaurantViewBinding;
import com.guilhempelissier.go4lunch.view.adapter.WorkmatesListAdapter;
import com.guilhempelissier.go4lunch.viewmodel.FormattedRestaurant;
import com.guilhempelissier.go4lunch.viewmodel.FormattedWorkmate;

import java.util.Arrays;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity {
	//TODO debug data
	private FormattedRestaurant dummyRestaurant = new FormattedRestaurant(
			"Name1", "123 rue 465", "open", "100m", "3", "*", "https://media-cdn.tripadvisor.com/media/photo-s/12/c1/c3/f5/restaurant-araz.jpg", "2"
	);

	private List<FormattedWorkmate> dummylist = Arrays.asList(
			new FormattedWorkmate(
					"Hugh", "rst", "https://m.media-amazon.com/images/M/MV5BNDExMzIzNjk3Nl5BMl5BanBnXkFtZTcwOTE4NDU5OA@@._V1_.jpg"
			)
	);

	private RestaurantViewBinding binding;

	private RecyclerView recyclerView;
	private WorkmatesListAdapter adapter;

	private ImageView restaurantImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.restaurant_view);

		restaurantImageView = findViewById(R.id.restaurant_image_view);
		Glide.with(this)
				.load(dummyRestaurant.getImageUrl())
				.into(restaurantImageView);

		recyclerView = findViewById(R.id.restaurant_view_recyclerview);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		adapter = new WorkmatesListAdapter(dummylist, false);
		recyclerView.setAdapter(adapter);

		binding.setRestaurantNameStars(dummyRestaurant.getName() + " " + dummyRestaurant.getStars());
		binding.setRestaurantAddress(dummyRestaurant.getAddress());
	}
}
