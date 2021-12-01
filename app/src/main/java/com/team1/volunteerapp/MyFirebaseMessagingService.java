package com.team1.volunteerapp;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.util.Log;
import androidx.core.app.NotificationCompat.Builder;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.messaging.RemoteMessage.Notification;
import com.team1.volunteerapp.Auth.IntroActivity;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(
        mv = {1, 5, 1},
        k = 1,
        d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u0010\u0010\t\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u0004H\u0016J\u001a\u0010\u000b\u001a\u00020\u00062\b\u0010\f\u001a\u0004\u0018\u00010\u00042\u0006\u0010\r\u001a\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"},
        d2 = {"LMyFirebaseMessagingService;", "Lcom/google/firebase/messaging/FirebaseMessagingService;", "()V", "TAG", "", "onMessageReceived", "", "remoteMessage", "Lcom/google/firebase/messaging/RemoteMessage;", "onNewToken", "token", "sendNotification", "title", "body", "VolunteerApp.app"}
)
public final class MyFirebaseMessagingService extends FirebaseMessagingService {
    private final String TAG;

    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        Intrinsics.checkNotNullParameter(remoteMessage, "remoteMessage");
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            Notification var10001 = remoteMessage.getNotification();
            String var2 = var10001 != null ? var10001.getTitle() : null;
            Notification var10002 = remoteMessage.getNotification();
            Intrinsics.checkNotNull(var10002);
            Intrinsics.checkNotNullExpressionValue(var10002, "remoteMessage.notification!!");
            String var3 = var10002.getBody();
            Intrinsics.checkNotNull(var3);
            Intrinsics.checkNotNullExpressionValue(var3, "remoteMessage.notification!!.body!!");
            this.sendNotification(var2, var3);
        }

    }

    public void onNewToken(@NotNull String token) {
        Intrinsics.checkNotNullParameter(token, "token");
        Log.d(this.TAG, "Refreshed token : " + token);
        super.onNewToken(token);
    }

    private final void sendNotification(String title, String body) {
        Intent intent = new Intent((Context)this, IntroActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity((Context)this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "my_channel";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Builder var10000 = (new Builder((Context)this, channelId))
                .setContentTitle((CharSequence)title)
                .setContentText((CharSequence)body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        Intrinsics.checkNotNullExpressionValue(var10000, "NotificationCompat.Build…tentIntent(pendingIntent)");
        Builder notificationBuilder = var10000;
        @SuppressLint("WrongConstant")
        Object var10 = this.getSystemService("notification");
        if (var10 == null) {
            throw new NullPointerException("null cannot be cast to non-null type android.app.NotificationManager");
        } else {
            NotificationManager notificationManager = (NotificationManager)var10;
            if (VERSION.SDK_INT >= 26) {
                NotificationChannel channel = new NotificationChannel(channelId, (CharSequence)"Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0, notificationBuilder.build());
        }
    }

    public MyFirebaseMessagingService() {
        String var10001 = this.getClass().getSimpleName();
        Intrinsics.checkNotNullExpressionValue(var10001, "this.javaClass.simpleName");
        this.TAG = var10001;
    }
}
