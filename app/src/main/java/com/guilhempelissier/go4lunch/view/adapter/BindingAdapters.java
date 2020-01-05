package com.guilhempelissier.go4lunch.view.adapter;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class BindingAdapters {
	@BindingAdapter("circledImageURL")
	public static void setCircledImageUrl(ImageView view, String url) {
		if (url != null) {
			Glide.with(view.getContext())
					.load(url)
					.apply(RequestOptions.circleCropTransform())
					.into(view);
		}
	}
}
