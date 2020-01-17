package com.guilhempelissier.go4lunch.view.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.guilhempelissier.go4lunch.BuildConfig;
import com.guilhempelissier.go4lunch.R;
import com.guilhempelissier.go4lunch.databinding.ActivityMainBinding;
import com.guilhempelissier.go4lunch.databinding.MenuHeaderBinding;
import com.guilhempelissier.go4lunch.viewmodel.AuthViewModel;
import com.guilhempelissier.go4lunch.viewmodel.MainViewModel;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

	private static final int PERMISSIONS_ACCESS_CODE = 126;
	private static final int RC_SIGN_IN = 123;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	private BottomNavigationView bottomNavigationView;
	private NavController navController;
	private DrawerLayout drawerLayout;

	private MainViewModel mainViewModel;
	private AuthViewModel authViewModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidThreeTen.init(getApplicationContext());

		ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

		mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
		mainViewModel.getNeedsPermission().observe(this, isPermissionNeeded -> {
			if(isPermissionNeeded) {
				getLocationPermission();
			}
		});

		authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

		Toolbar toolbar = mainBinding.toolbar;
		setSupportActionBar(toolbar);

		drawerLayout = mainBinding.drawerLayout;
		actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
		drawerLayout.addDrawerListener(actionBarDrawerToggle);
		actionBarDrawerToggle.syncState();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		navController = Navigation.findNavController(this, R.id.navigation_container);

		bottomNavigationView = mainBinding.navigationMenu;
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

		NavigationView navView = mainBinding.navView;
		MenuHeaderBinding menuHeaderBinding = MenuHeaderBinding.bind(navView.getHeaderView(0));
		authViewModel.getCurrentUser().observe(this, user -> {
			menuHeaderBinding.setUser(user);
		});
		authViewModel.isUserConnected().observe(this, isConnected -> {
			if (isConnected) {
				navView.getMenu().setGroupVisible(R.id.menu_group_1, true);
				navView.getMenu().setGroupVisible(R.id.menu_group_2, false);
			} else {
				navView.getMenu().setGroupVisible(R.id.menu_group_1, false);
				navView.getMenu().setGroupVisible(R.id.menu_group_2, true);
				startSignInActivity();
			}
		});
		authViewModel.updateCurrentUser();

		navView.setNavigationItemSelectedListener(menuItem -> {
			switch (menuItem.getItemId()) {
				case R.id.menu_logout:
					authViewModel.disconnectCurrentUser();
					break;
				case R.id.menu_settings:
					break;
				case R.id.menu_lunch:
					break;
				case R.id.menu_login:
					startSignInActivity();
					break;
			}
			return true;
		});

		Places.initialize(this, BuildConfig.PLACES_KEY);
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
			Toast.makeText(this, "Connecté", Toast.LENGTH_SHORT).show();
		} else {
			if (response == null) {
				Toast.makeText(this, "La connexion a été annulée", Toast.LENGTH_SHORT).show();
			} else {
				switch (response.getError().getErrorCode()) {
					case ErrorCodes.NO_NETWORK: Toast.makeText(this, "Pas de connexion internet", Toast.LENGTH_SHORT).show();
					case ErrorCodes.UNKNOWN_ERROR: Toast.makeText(this, "Erreur inconnue", Toast.LENGTH_SHORT).show();
					default:
				}
			}
		}
		authViewModel.updateCurrentUser();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(actionBarDrawerToggle.onOptionsItemSelected(item))
			return true;

		return super.onOptionsItemSelected(item);
	}

	public void getLocationPermission() {
		mainViewModel.setNeedsPermission(false);
		ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_ACCESS_CODE);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == PERMISSIONS_ACCESS_CODE) {
			for (int i = 0; i < permissions.length; i++) {
				String permission = permissions[i];
				int grantResult = grantResults[i];

				if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
					if (grantResult == PackageManager.PERMISSION_GRANTED) {
						mainViewModel.setNeedsPermission(false);
					} else {
						getLocationPermission();
					}
				}
			}
		}
	}
}
