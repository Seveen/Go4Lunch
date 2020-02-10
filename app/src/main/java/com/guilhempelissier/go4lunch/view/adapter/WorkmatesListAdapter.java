package com.guilhempelissier.go4lunch.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.guilhempelissier.go4lunch.R;
import com.guilhempelissier.go4lunch.databinding.WorkmatesViewItemBinding;
import com.guilhempelissier.go4lunch.model.FormattedWorkmate;

import java.util.Collections;
import java.util.List;

public class WorkmatesListAdapter extends RecyclerView.Adapter<WorkmatesListAdapter.WorkmateViewHolder> {

	private OnClickWorkmateListener listener;
	private List<FormattedWorkmate> workmates;
	private boolean displayEatingPlace;

	private Context context;

	public WorkmatesListAdapter(boolean displayEatingPlace, Context context) {
		workmates = Collections.emptyList();
		this.displayEatingPlace = displayEatingPlace;
		this.context = context;
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

	class WorkmateViewHolder extends RecyclerView.ViewHolder {
		private final WorkmatesViewItemBinding binding;
		private ImageView picture;

		WorkmateViewHolder(WorkmatesViewItemBinding binding) {
			super(binding.getRoot());
			picture = binding.workmatesItemImage;
			this.binding = binding;

			binding.constraintLayout.setOnClickListener(view -> {
				int position = getAdapterPosition();

				if (listener != null && position != RecyclerView.NO_POSITION) {
					listener.onClick(workmates.get(position).getEatingPlaceId());
				}
			});
		}

		void bind(FormattedWorkmate workmate) {
			String text;
			Glide.with(binding.getRoot())
					.load(workmate.getImageUrl())
					.apply(RequestOptions.circleCropTransform())
					.into(picture);
			if (displayEatingPlace) {
				if (workmate.getEatingPlace() == "") {
					text = workmate.getName() + " " + context.getString(R.string.hasnt_chosen);
				} else {
					text = workmate.getName() + " " + context.getString(R.string.eats_at) + " " + workmate.getEatingPlace();
				}
			} else {
				text = workmate.getName() + " " + context.getString(R.string.is_joining);
			}
			binding.setText(text);
		}
	}

	public interface OnClickWorkmateListener {
		void onClick(String id);
	}

	public void setListener(OnClickWorkmateListener listener) {
		this.listener = listener;
	}
}
