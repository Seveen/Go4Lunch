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

import java.util.ArrayList;
import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder> {

	private List<FormattedRestaurant> restaurants = new ArrayList<>();
	private OnClickRestaurantListener listener;

	public RestaurantListAdapter(List<FormattedRestaurant> initialList) {
		restaurants.addAll(initialList);
	}

	public void setData(List<FormattedRestaurant> newData) {
		restaurants.clear();
		restaurants.addAll(newData);
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

	class RestaurantViewHolder extends RecyclerView.ViewHolder {
		private final ListViewItemBinding binding;
		private ImageView picture;

		RestaurantViewHolder(ListViewItemBinding binding) {
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

		void bind(FormattedRestaurant restaurant) {
			Glide.with(binding.getRoot())
					.load(restaurant.getImageUrl())
					.centerCrop()
					.into(picture);
			binding.setRestaurant(restaurant);

			int nbWorkmates = restaurant.getWorkmates().size();
			if (nbWorkmates == 0) {
				binding.setNbWorkmates("");
			} else {
				binding.setNbWorkmates(Integer.toString(nbWorkmates));
			}
		}
	}

	public interface OnClickRestaurantListener {
		void onClick(String id);
	}

	public void setOnClickRestaurantListener(OnClickRestaurantListener listener) {
		this.listener = listener;
	}
}
