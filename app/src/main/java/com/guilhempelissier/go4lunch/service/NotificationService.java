package com.guilhempelissier.go4lunch.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.guilhempelissier.go4lunch.R;
import com.guilhempelissier.go4lunch.di.DI;
import com.guilhempelissier.go4lunch.model.Restaurant;
import com.guilhempelissier.go4lunch.model.User;
import com.guilhempelissier.go4lunch.repository.PlacesRepository;
import com.guilhempelissier.go4lunch.repository.UsersRepository;
import com.guilhempelissier.go4lunch.view.ui.RestaurantActivity;

import java.util.ArrayList;
import java.util.List;

public class NotificationService extends FirebaseMessagingService {

	private final int NOTIFICATION_ID = 145;
	private final String NOTIFICATION_TAG = "FIREBASENOTIF";

	private String notificationMessage;

	@Override
	public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
		if (remoteMessage.getNotification() != null) {
			sendVisualNotification();
		}
	}

	private void sendVisualNotification() {
		PlacesRepository placesRepository = DI.getPlacesRepository(getApplicationContext());
		UsersRepository usersRepository = DI.getUsersRepository();

		User currentUser = usersRepository.getCurrentUser().getValue();

		if (currentUser != null && !currentUser.getLunch().equals("") && currentUser.isNotifyMe()) {
			List<User> workmatesComingToo = new ArrayList<>();
			placesRepository.setCurrentRestaurantId(currentUser.getLunch());

			placesRepository.getCurrentRestaurant().subscribe(restaurant -> updateNotificationMessage(restaurant));
			List<User> workmates = usersRepository.getWorkmates().getValue();
			if (workmates != null) {
				for (User workmate : workmates) {
					if (workmate.getLunch().equals(currentUser.getLunch())) {
						workmatesComingToo.add(workmate);
					}
				}
			}

			Intent intent = new Intent(this, RestaurantActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

			NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
			inboxStyle.setBigContentTitle(getString(R.string.notification_title));
			inboxStyle.addLine(notificationMessage);
			inboxStyle.addLine(getString(R.string.with) + " " + workmatesComingToo.size() + " " + getString(R.string.workmate_maybe_plural));

			String channelId = getString(R.string.default_notification_channel_id);

			NotificationCompat.Builder notificationBuilder =
					new NotificationCompat.Builder(this, channelId)
							.setSmallIcon(R.drawable.ic_restaurant_menu_white_24dp)
							.setContentTitle(getString(R.string.app_name))
							.setContentText(getString(R.string.notification_title))
							.setAutoCancel(true)
							.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
							.setContentIntent(pendingIntent)
							.setStyle(inboxStyle);

			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				CharSequence channelName = getString(R.string.channel_name);
				int importance = NotificationManager.IMPORTANCE_HIGH;
				NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
				notificationManager.createNotificationChannel(mChannel);
			}

			notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
		}
	}

	private void updateNotificationMessage(Restaurant restaurant) {
		notificationMessage = getString(R.string.youre_eating_at) + "  " + restaurant.getName() + ", " + restaurant.getVicinity();
	}
}
