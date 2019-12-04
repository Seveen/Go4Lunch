package com.guilhempelissier.go4lunch.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.guilhempelissier.go4lunch.R;
import com.guilhempelissier.go4lunch.databinding.ListViewItemBinding;
import com.guilhempelissier.go4lunch.viewmodel.FormattedRestaurant;

import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder> {

	private List<FormattedRestaurant> restaurants;


	public RestaurantListAdapter(List<FormattedRestaurant> initialList) {
		restaurants = initialList;
	}

	public void setData(List<FormattedRestaurant> newData) {
		restaurants = newData;
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
		ListViewItemBinding binding = ListViewItemBinding.inflate(layoutInflater, parent, false);
		return new RestaurantViewHolder(binding);
	}

	@Override
	public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
		FormattedRestaurant restaurant = restaurants.get(position);
		holder.bind(restaurant);
	}

	@Override
	public int getItemCount() {
		return restaurants.size();
	}

	public class RestaurantViewHolder extends RecyclerView.ViewHolder {
		private final ListViewItemBinding binding;
		private ImageView picture;

		public RestaurantViewHolder(ListViewItemBinding binding) {
			super(binding.getRoot());
			picture = binding.getRoot().findViewById(R.id.list_item_image);
			this.binding = binding;

			//onclicklistener (TODO example mareu)
		}

		public void bind(FormattedRestaurant restaurant) {
			Glide.with(binding.getRoot())
					.load(restaurant.getImageUrl())
					.into(picture);
			binding.setName(restaurant.getName());
			binding.setAddress(restaurant.getAddress());
			binding.setOpeningTime(restaurant.getOpeningTime());
			binding.setDistance(restaurant.getDistance());
			binding.setNbWorkmates(restaurant.getNbWorkmates());
			binding.setStars(restaurant.getStars());
		}
	}
}
