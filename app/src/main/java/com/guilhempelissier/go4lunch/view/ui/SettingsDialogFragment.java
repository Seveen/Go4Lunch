package com.guilhempelissier.go4lunch.view.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.guilhempelissier.go4lunch.R;
import com.guilhempelissier.go4lunch.viewmodel.MainViewModel;

public class SettingsDialogFragment extends DialogFragment {

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
			MainViewModel mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = requireActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_settings, null);
		Switch button = view.findViewById(R.id.toggle_notification_switch);

		mainViewModel.getCurrentUser().observe(getActivity(), user -> button.setChecked(user.isNotifyMe()));
		button.setOnClickListener(v -> mainViewModel.toggleNotifyMe());

		builder.setMessage(R.string.menu_settings)
				.setNegativeButton(R.string.back, (dialogInterface, i) -> dialogInterface.cancel())
				.setView(view);
		return builder.create();
	}
}
