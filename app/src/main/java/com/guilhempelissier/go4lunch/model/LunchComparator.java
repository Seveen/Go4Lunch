package com.guilhempelissier.go4lunch.model;

import java.util.Comparator;

public class LunchComparator implements Comparator<User> {
	@Override
	public int compare(User user1, User user2) {
		if (user1.getLunch() == user2.getLunch()) {
			return 0;
		} else if (user1.getLunch() == "") {
			return 1;
		} else {
			return -1;
		}
	}
}
