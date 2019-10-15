package com.map4d.notificationapptest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String CHANEL_1_ID = "CHANEL1";
    NotificationManager manager;
    private String tokenz;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            hienThiThongBao(remoteMessage.getNotification().getBody());
        }
        hienThiThongBao(remoteMessage.getData().get("body"), remoteMessage.getData().get("title"));
    }

    private void hienThiThongBao(String body, String title) {
        Intent intent = new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANEL_1_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(pendingIntent);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chanel1 = new NotificationChannel(CHANEL_1_ID, "CHANEL 1", NotificationManager.IMPORTANCE_HIGH);
            chanel1.setDescription(body);
            manager.createNotificationChannel(chanel1);
        }
    }

    @Override

    public void onNewToken(String s) {
        String token;
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener((Executor) this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken",newToken);
                tokenz = newToken;
                luuTokenVaoCSDLRieng(tokenz);
                registerToken(newToken);
            }
        });
        Log.e("DEVICE TOKEN:", tokenz);
        luuTokenVaoCSDLRieng(tokenz);
    }
    private void luuTokenVaoCSDLRieng(String token) {
        new FireBaseIDTask().execute(token);
    }
    private void registerToken(String token) {
        Log.e("hihinacac",token);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token",token)
                .build();

        Request request = new Request.Builder()
                .url("http://127.0.0.1/fcm/register.php")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void hienThiThongBao(String body) {
        hienThiThongBao(body, "google");
    }
}