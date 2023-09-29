package com.manager.hamster.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.manager.hamster.R;
import com.manager.hamster.activity.MainActivity;

public class FirebaseMessageReceiver extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        if(message.getNotification() !=null){
            showNotification(message.getNotification().getTitle(),message.getNotification().getBody());
        }
      
    }

    private void showNotification(String title, String body) {
        Intent  intent = new Intent(this , MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String channelID= "noti";
        //cho nay pendingItent dung one shot
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_MUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),channelID)
                .setSmallIcon(R.drawable.baseline_add_24)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000,1000,1000,1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);
        builder= builder.setContent(customView(title,body));
        NotificationManager notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel= new NotificationChannel(channelID,"web_app",NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(0,builder.build());
    }

    private  RemoteViews customView(String title, String body){
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.imgnotification_title,title);
        remoteViews.setTextViewText(R.id.imgnotification_body,body);
        remoteViews.setImageViewResource(R.id.imgnotification,R.drawable.ic_gcm_laucer_background);
        return remoteViews;
    }
}
