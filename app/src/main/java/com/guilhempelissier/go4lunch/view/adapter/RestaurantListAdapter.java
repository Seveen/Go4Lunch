package com.guilhempelissier.go4lunch.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.guilhempelissier.go4lunch.R;
import com.guilhempelissier.go4lunch.databinding.ListViewItemBinding;
import com.guilhempelissier.go4lunch.model.FormattedRestaurant;

import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder> {

	private List<FormattedRestaurant> restaurants;
	private OnClickRestaurantListener listener;


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

			binding.Layout.setOnClickListener(view -> {
				int position = getAdapterPosition();

				if (listener != null && position != RecyclerView.NO_POSITION) {
					listener.onClick(restaurants.get(position).getId());
				}
			});
		}

		public void bind(FormattedRestaurant restaurant) {
			Glide.with(binding.getRoot())
					.load(restaurant.getImageUrl())
					.centerCrop()
					.into(picture);
			binding.setName(restaurant.getName());
			binding.setAddress(restaurant.getAddress());
			binding.setOpeningTime(restaurant.getOpenNow());
			binding.setDistance(restaurant.getDistance());

			int nbWorkmates = restaurant.getWorkmates().size();
			if (nbWorkmates == 0) {
				binding.setNbWorkmates("");
				binding.setIsWorkmateNotZero(false);
			} else {
				binding.setNbWorkmates(Integer.toString(nbWorkmates));
				binding.setIsWorkmateNotZero(true);
			}

			binding.setStars(restaurant.getStars());
		}
	}

	public interface OnClickRestaurantListener {
		void onClick(String id);
	}

	public void setOnClickRestaurantListener(OnClickRestaurantListener listener) {
		this.listener = listener;
	}
}
