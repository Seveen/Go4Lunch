package com.guilhempelissier.go4lunch.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthService {
	FirebaseAuth auth;

	public AuthService(FirebaseAuth auth) {
		this.auth = auth;
	}

	public FirebaseUser getCurrentUser() {
		return auth.getCurrentUser();
	}

	public Boolean isUserConnected() {
		return getCurrentUser() != null;
	}

	public void disconnectCurrentUser() {
		auth.signOut();
	}
}
