package com.guilhempelissier.go4lunch.view.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.guilhempelissier.go4lunch.R;
import com.guilhempelissier.go4lunch.di.DI;
import com.guilhempelissier.go4lunch.repository.Repository;

import java.util.Arrays;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements MapViewFragment.OnFragmentInteractionListener, WorkmatesFragment.OnFragmentInteractionListener, ListViewFragment.OnFragmentInteractionListener {

	private static final int PERMISSIONS_READ_CONTACT = 126;
	private static final int RC_SIGN_IN = 123;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	private BottomNavigationView bottomNavigationView;
	private NavController navController;
	private DrawerLayout drawerLayout;

	private FusedLocationProviderClient fusedLocationClient;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		drawerLayout = findViewById(R.id.drawer_layout);
		actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
		drawerLayout.addDrawerListener(actionBarDrawerToggle);
		actionBarDrawerToggle.syncState();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

		AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
				getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
		autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

		autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
			String TAG = "Autocomplete";
			@Override
			public void onPlaceSelected(@NonNull Place place) {
				Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
			}

			@Override
			public void onError(@NonNull Status status) {
				Log.i(TAG, "An error occurred: " + status);
			}
		});

		//TODO gestion permissions ?
		if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
			getLocationPermission();
		}

		//TODO changer le comportement de la signin activity
		//startSignInActivity();

		//TODO debug test repository
		Repository repository = DI.getRepository(getApplicationContext());
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
		return super.onCreateView(name, context, attrs);
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if(actionBarDrawerToggle.onOptionsItemSelected(item))
			return true;

		return super.onOptionsItemSelected(item);
	}

	public void getLocationPermission() {
		ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_READ_CONTACT);
	}
}
