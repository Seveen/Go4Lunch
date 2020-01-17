package com.guilhempelissier.go4lunch.service;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.guilhempelissier.go4lunch.model.User;

import java.util.List;

public class FirebaseService {
	private static final String COLLECTION_NAME = "users";

	public static CollectionReference getUsersCollection(){
		return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
	}

	public static Task<QuerySnapshot> getAllUsers() {
		return getUsersCollection().get();
	}

	public static Task<Void> createUser(String uid, String username, String imageUrl) {
		User userToCreate = new User(uid, username, imageUrl);
		return getUsersCollection().document(uid).set(userToCreate);
	}

	public static Task<DocumentSnapshot> getUser(String uid){
		return getUsersCollection().document(uid).get();
	}

	public static Task<Void> updateUsername(String uid, String username) {
		return getUsersCollection().document(uid).update("username", username);
	}

	public static Task<Void> updateLunch(String uid, String lunch) {
		return getUsersCollection().document(uid).update("lunch", lunch);
	}

	public static Task<Void> updateLikes(String uid, List<String> likes) {
		return getUsersCollection().document(uid).update("likedRestaurants", likes);
	}

	public static Task<Void> deleteUser(String uid) {
		return getUsersCollection().document(uid).delete();
	}
}
