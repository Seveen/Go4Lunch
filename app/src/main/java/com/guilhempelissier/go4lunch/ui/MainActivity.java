package com.guilhempelissier.go4lunch.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.guilhempelissier.go4lunch.R;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements MapViewFragment.OnFragmentInteractionListener, WorkmatesFragment.OnFragmentInteractionListener, ListViewFragment.OnFragmentInteractionListener {

	private static final int RC_SIGN_IN = 123;
	private BottomNavigationView bottomNavigationView;
	private NavController navController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		navController = Navigation.findNavController(this, R.id.navigation_container);

		bottomNavigationView = findViewById(R.id.navigation_menu);
		bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
			switch (menuItem.getItemId()) {
				case R.id.menu_map_view:
					navController.navigate(R.id.action_global_mapViewFragment);
					break;
				case R.id.menu_list_view:
					navController.navigate(R.id.action_global_listViewFragment);
					break;
				case R.id.menu_workmates:
					navController.navigate(R.id.action_global_workmatesFragment);
					break;
			}
			return true;
		});

		startSignInActivity();
	}

	private void startSignInActivity() {
		startActivityForResult(
				AuthUI.getInstance()
				.createSignInIntentBuilder()
				.setTheme(R.style.AppTheme)
				.setAvailableProviders(
						Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build(), new AuthUI.IdpConfig.FacebookBuilder().build())
				)
				.setIsSmartLockEnabled(false, true)
				.build(),
			RC_SIGN_IN
		);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			handleLoginResult(resultCode, data);
		}
	}

	private void handleLoginResult(int resultCode, Intent data) {
		IdpResponse response = IdpResponse.fromResultIntent(data);

		if (resultCode == RESULT_OK) {
			Toast.makeText(this, "La connexion est un succes", Toast.LENGTH_SHORT).show();
		} else {
			if (response == null) {
				Toast.makeText(this, "La connexion a ete annulee", Toast.LENGTH_SHORT).show();
			} else {
				switch (response.getError().getErrorCode()) {
					case ErrorCodes.NO_NETWORK: Toast.makeText(this, "Pas internet", Toast.LENGTH_SHORT).show();
					case ErrorCodes.UNKNOWN_ERROR: Toast.makeText(this, "Erreur inconnue", Toast.LENGTH_SHORT).show();
					default:
				}
			}
		}
	}

	@Override
	public void onMapFragmentInteraction(Uri uri) {

	}

	@Override
	public void onListFragmentInteraction(Uri uri) {

	}

	@Override
	public void onWorkmatesFragmentInteraction(Uri uri) {

	}
}
