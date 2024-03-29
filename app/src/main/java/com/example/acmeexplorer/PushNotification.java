package com.example.acmeexplorer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class PushNotification extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        createNotificationChannel();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification customNotification;
        if (remoteMessage.getData().isEmpty()) {
            customNotification = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                    .setSmallIcon(R.drawable.acme_explorer_circle_logo)
                    .setStyle(new NotificationCompat.InboxStyle())
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("Próximamente podrás disfrutar de todas las ofertas"))
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.acme_explorer_circle_logo))
                    .setContentTitle("OFERTAS EN ACME EXPLORER")
                    .setContentText("Pulsa para ver")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
        } else {
            customNotification = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                    .setSmallIcon(R.drawable.acme_explorer_circle_logo)
                    .setStyle(new NotificationCompat.InboxStyle())
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(remoteMessage.getData().get("big")))
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.acme_explorer_circle_logo))
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("content"))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(new Random().nextInt(), customNotification);
    }

    @Override
    public void onNewToken(String refreshedToken) {
        super.onNewToken(refreshedToken);
        Log.e("ERROR", "Refreshed token: " + refreshedToken);
        FirebaseMessaging.getInstance().subscribeToTopic("general");
        sendRegistrationToServer(refreshedToken, this);
    }


    public static void sendRegistrationToServer(final String refreshedToken, final Context context) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference myRef = database.getReference("users/" + currentUserID + "/token_push");
            myRef.setValue(refreshedToken).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.e("ERROR", "Refreshed token: " + refreshedToken);
                    //Toast.makeText(context, "Token de notificaiones guardado", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.default_notification_channel_id), name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
