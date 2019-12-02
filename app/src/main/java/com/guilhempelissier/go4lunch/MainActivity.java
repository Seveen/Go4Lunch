package com.guilhempelissier.go4lunch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

	private static final int RC_SIGN_IN = 123;
	private Button loginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		loginButton = findViewById(R.id.login_button_main);
		loginButton.setOnClickListener(view -> startSignInActivity());
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
				}
			}
		}
	}
}
