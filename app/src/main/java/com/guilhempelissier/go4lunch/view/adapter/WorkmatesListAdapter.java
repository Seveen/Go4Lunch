package com.guilhempelissier.go4lunch.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.guilhempelissier.go4lunch.R;
import com.guilhempelissier.go4lunch.databinding.WorkmatesViewItemBinding;
import com.guilhempelissier.go4lunch.viewmodel.FormattedWorkmate;

import java.util.List;

public class WorkmatesListAdapter extends RecyclerView.Adapter<WorkmatesListAdapter.WorkmateViewHolder> {

	private List<FormattedWorkmate> workmates;
	private boolean displayEatingPlace;

	public WorkmatesListAdapter(List<FormattedWorkmate> initialList, boolean displayEatingPlace) {
		workmates = initialList;
		this.displayEatingPlace = displayEatingPlace;
	}

	public void setData(List<FormattedWorkmate> newData) {
		workmates = newData;
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public WorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
		WorkmatesViewItemBinding binding = WorkmatesViewItemBinding.inflate(layoutInflater, parent, false);
		return new WorkmateViewHolder(binding);
	}

	@Override
	public void onBindViewHolder(@NonNull WorkmateViewHolder holder, int position) {
		FormattedWorkmate workmate = workmates.get(position);
		holder.bind(workmate);
	}

	@Override
	public int getItemCount() {
		return workmates.size();
	}

	public class WorkmateViewHolder extends RecyclerView.ViewHolder {
		private final WorkmatesViewItemBinding binding;
		private ImageView picture;

		public WorkmateViewHolder(WorkmatesViewItemBinding binding) {
			super(binding.getRoot());
			picture = binding.getRoot().findViewById(R.id.workmates_item_image);
			this.binding = binding;

			//onclicklistener (TODO example mareu)
		}

		public void bind(FormattedWorkmate workmate) {
			String text;
			Glide.with(binding.getRoot())
					.load(workmate.getImageUrl())
					.apply(RequestOptions.circleCropTransform())
					.into(picture);
			if (displayEatingPlace) {
				text = workmate.getName() + " " + workmate.getEatingPlace();
			} else {
				text = workmate.getName() + " is joining!";
			}
			binding.setText(text);
		}
	}
}
