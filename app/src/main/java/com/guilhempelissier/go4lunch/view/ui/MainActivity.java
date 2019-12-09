package com.guilhempelissier.go4lunch.view.ui;

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

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.guilhempelissier.go4lunch.BuildConfig;
import com.guilhempelissier.go4lunch.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements MapViewFragment.OnFragmentInteractionListener, WorkmatesFragment.OnFragmentInteractionListener, ListViewFragment.OnFragmentInteractionListener {

	private static final int RC_SIGN_IN = 123;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	private BottomNavigationView bottomNavigationView;
	private NavController navController;

	private DrawerLayout drawerLayout;

	//TODO
	int PERMISSIONS_READ_CONTACT = 126;

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

		//TODO changer le comportement de la signin activity
		initAndTestPlaces();
		startSignInActivity();
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

	public void initAndTestPlaces() {

		String TAG = "TEST PLACES";
		Log.d("API", BuildConfig.PLACES_KEY);
		Places.initialize(getApplicationContext(), BuildConfig.PLACES_KEY);
		PlacesClient placesClient = Places.createClient(this);

		List<Place.Field> placeFields = Collections.singletonList(Place.Field.NAME);
		FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
			Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
			placeResponse.addOnCompleteListener(task -> {
				if (task.isSuccessful()){
					FindCurrentPlaceResponse response = task.getResult();
					for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
						Log.i(TAG, String.format("Place '%s' has likelihood: %f",
								placeLikelihood.getPlace().getName(),
								placeLikelihood.getLikelihood()));
					}
				} else {
					Exception exception = task.getException();
					if (exception instanceof ApiException) {
						ApiException apiException = (ApiException) exception;
						Log.e(TAG, "Place not found: " + apiException.getStatusCode());
					}
				}
			});
		} else {
			getLocationPermission();
		}
	}
}
