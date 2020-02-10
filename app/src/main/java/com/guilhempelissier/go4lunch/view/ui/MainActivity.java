package com.guilhempelissier.go4lunch.view.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.guilhempelissier.go4lunch.R;
import com.guilhempelissier.go4lunch.databinding.ActivityMainBinding;
import com.guilhempelissier.go4lunch.databinding.MenuHeaderBinding;
import com.guilhempelissier.go4lunch.model.User;
import com.guilhempelissier.go4lunch.repository.PlacesRepository;
import com.guilhempelissier.go4lunch.viewmodel.AuthViewModel;
import com.guilhempelissier.go4lunch.viewmodel.ListViewModel;
import com.guilhempelissier.go4lunch.viewmodel.MainViewModel;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = "Main Activity";

	private static final int PERMISSIONS_ACCESS_CODE = 126;
	private static final int SIGN_IN_REQUEST_CODE = 123;

	private ActionBarDrawerToggle actionBarDrawerToggle;
	private BottomNavigationView bottomNavigationView;
	private NavController navController;
	private DrawerLayout drawerLayout;

	private MainViewModel mainViewModel;
	private AuthViewModel authViewModel;
	private ListViewModel listViewModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidThreeTen.init(getApplicationContext());

		ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

		mainBinding.setSearchNotVisible(true);
		mainBinding.toolbarSearchToggleBtn.setOnClickListener(view -> {
			if (!mainBinding.getSearchNotVisible()) {
				mainViewModel.clearAutocompleteResults();
			}
			mainBinding.setSearchNotVisible(!mainBinding.getSearchNotVisible());
		});

		mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
		mainViewModel.getNeedsPermission().observe(this, isPermissionNeeded -> {
			if(isPermissionNeeded) {
				getLocationPermission();
			}
		});

		listViewModel = ViewModelProviders.of(this).get(ListViewModel.class);
		authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

		Toolbar toolbar = mainBinding.toolbar;
		setSupportActionBar(toolbar);

		drawerLayout = mainBinding.drawerLayout;
		actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
		actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
		toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
		drawerLayout.addDrawerListener(actionBarDrawerToggle);
		actionBarDrawerToggle.syncState();

		navController = Navigation.findNavController(this, R.id.navigation_container);
		bottomNavigationView = mainBinding.navigationMenu;
		bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
			switch (menuItem.getItemId()) {
				case R.id.menu_map_view:
					navController.navigate(R.id.action_global_mapViewFragment);
					mainBinding.setSortVisible(false);
					mainBinding.setSearchDisallowed(false);
					break;
				case R.id.menu_list_view:
					navController.navigate(R.id.action_global_listViewFragment);
					mainBinding.setSortVisible(true);
					mainBinding.setSearchDisallowed(false);
					break;
				case R.id.menu_workmates:
					navController.navigate(R.id.action_global_workmatesFragment);
					mainBinding.setSortVisible(false);
					mainBinding.setSearchDisallowed(true);
					break;
			}
			return true;
		});

		NavigationView navView = mainBinding.navView;
		MenuHeaderBinding menuHeaderBinding = MenuHeaderBinding.bind(navView.getHeaderView(0));
		authViewModel.getCurrentUser().observe(this, menuHeaderBinding::setUser);
		authViewModel.isUserConnected().observe(this, isConnected -> {
			if (!isConnected) {
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
					DialogFragment settingsDialog = new SettingsDialogFragment();
					settingsDialog.show(getSupportFragmentManager(), "settings");
					break;
				case R.id.menu_lunch:
					User user = mainViewModel.getCurrentUser().getValue();
					if (user != null && !user.getLunch().equals("")) {
						mainViewModel.setCurrentRestaurantId(user.getLunch());
						Intent intent = new Intent(this, RestaurantActivity.class);
						startActivity(intent);
					} else {
						Toast.makeText(this, getString(R.string.no_restaurant_selected), Toast.LENGTH_SHORT).show();
					}
					break;
			}
			return true;
		});

		EditText searchView = mainBinding.toolbarSearchEdittext;
		searchView.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if (charSequence.length() > 0) {
					mainViewModel.queryAutocompletePrediction(charSequence.toString());
				}
			}

			@Override
			public void afterTextChanged(Editable editable) {}
		});
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
		return super.onCreateView(name, context, attrs);
	}

	public void showSortingPopup(View v) {
		PopupMenu popupMenu = new PopupMenu(this, v);
		popupMenu.getMenuInflater().inflate(R.menu.sort_options_menu, popupMenu.getMenu());
		popupMenu.setOnMenuItemClickListener(menuItem -> {
			switch (menuItem.getItemId()) {
				case R.id.menu_sort_workmates_most:
					listViewModel.setSortingMethod(PlacesRepository.Sorting.WorkmatesMost);
					return true;
				case R.id.menu_sort_workmates_least:
					listViewModel.setSortingMethod(PlacesRepository.Sorting.WorkmatesLeast);
					return true;
				case R.id.menu_sort_distance_most:
					listViewModel.setSortingMethod(PlacesRepository.Sorting.DistanceMost);
					return true;
				case R.id.menu_sort_distance_least:
					listViewModel.setSortingMethod(PlacesRepository.Sorting.DistanceLeast);
					return true;
				case R.id.menu_sort_rating_most:
					listViewModel.setSortingMethod(PlacesRepository.Sorting.RatingMost);
					return true;
				case R.id.menu_sort_rating_least:
					listViewModel.setSortingMethod(PlacesRepository.Sorting.RatingLeast);
					return true;
				default:
					return false;
			}
		});
		popupMenu.show();
	}

	private void startSignInActivity() {
		AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
				.Builder(R.layout.login_view)
				.setGoogleButtonId(R.id.google_login_button)
				.setFacebookButtonId(R.id.fb_login_button)
				.build();
		startActivityForResult(
				AuthUI.getInstance()
				.createSignInIntentBuilder()
				.setTheme(R.style.AppTheme)
				.setAuthMethodPickerLayout(customLayout)
				.setAvailableProviders(
						Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build(), new AuthUI.IdpConfig.FacebookBuilder().build())
				)
				.setIsSmartLockEnabled(false, true)
				.build(),
				SIGN_IN_REQUEST_CODE
		);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SIGN_IN_REQUEST_CODE) {
			handleLoginResult(resultCode, data);
		}
	}

	private void handleLoginResult(int resultCode, Intent data) {
		IdpResponse response = IdpResponse.fromResultIntent(data);

		if (resultCode == RESULT_OK) {
			Toast.makeText(this, getString(R.string.connected), Toast.LENGTH_SHORT).show();
			authViewModel.updateCurrentUser();
		} else {
			if (response == null) {
				Toast.makeText(this, getString(R.string.connection_canceled), Toast.LENGTH_SHORT).show();
				finish();
			} else {
				switch (response.getError().getErrorCode()) {
					case ErrorCodes.NO_NETWORK: Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
					case ErrorCodes.UNKNOWN_ERROR: Toast.makeText(this, getString(R.string.unknown_error), Toast.LENGTH_SHORT).show();
					default:
				}
				authViewModel.updateCurrentUser();
			}
		}
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
