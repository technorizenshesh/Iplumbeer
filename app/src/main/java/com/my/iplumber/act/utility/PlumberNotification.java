package com.my.iplumber.act.utility;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.my.iplumber.R;
import com.my.iplumber.act.HomePlumberAct;
import com.my.iplumber.act.HomeUserAct;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class PlumberNotification extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private String notificationType, title, meetupId, messageId;
    private String message;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        try
        {
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "payload:" + remoteMessage.getData());
                Map<String,String> map = remoteMessage.getData();
                try {
                    sendNotification("","",map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e)
        {
            Log.d(TAG, "onMessageReceived: "+e);
        }

        Log.d(TAG, "onMessageReceived for FCM");
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "c: " + remoteMessage.getData());
            try {
                sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), remoteMessage.getData());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendNotification(String message, String title,Map<String,String> map) throws JSONException {

        JSONObject jsonObject = null;
        jsonObject = new JSONObject(map);
        String key = jsonObject.getString("key");
        Intent intent = new Intent();
        String key1 = jsonObject.getString("message");
        boolean showText = true;

        if(key.equalsIgnoreCase("Plumber Available"))
        {
            intent = new Intent(this, HomeUserAct.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("key","notification");
            if(Util.appInForeground(this))
            {
                Intent intent1 = new Intent("filter_string");
                intent1.putExtra("key", "My Data");
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
            }
        } else  if(key.equalsIgnoreCase("call purchased"))
        {
            try {
                intent = new Intent(this, HomePlumberAct.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("key","notification");
                if(Util.appInForeground(this))
                {
                    Intent intent1 = new Intent("filter_string");
//                  intent.putExtra("key", "My Data");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
                }
            }catch (Exception e)
            {
            }

        }else  if(key.equalsIgnoreCase("Notification To User"))
        {
            intent = new Intent(this, HomePlumberAct.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("key","notification");
            if(Util.appInForeground(this))
            {
                Intent intent1 = new Intent("filter_string");
//                intent.putExtra("key", "My Data");
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
            }
        }else  if(key.equalsIgnoreCase("New Video Call Invitation"))
        {
            showText = false;
            String userName = jsonObject.getString("username");
            String channelName = jsonObject.getString("channel");
            String token = jsonObject.getString("token");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                boolean result= Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT_WATCH&&powerManager.isInteractive()|| Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT_WATCH&&powerManager.isScreenOn();

                if (!result){
                    PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MH24_SCREENLOCK");
                    wl.acquire(10000);
                    PowerManager.WakeLock wl_cpu = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MH24_SCREENLOCK");
                    wl_cpu.acquire(10000);
                }

                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                boolean isScreenOn = Build.VERSION.SDK_INT >= 20 ? pm.isInteractive() : pm.isScreenOn(); // check if screen is on
                if (!isScreenOn) {
                    PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "myApp:notificationLock");
                    wl.acquire(3000); //set your time in milliseconds
                }

                Intent serviceIntent = new Intent(getApplicationContext(), HeadsUpNotificationService.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("inititator", userName);
                mBundle.putString("call_type","Video");
                mBundle.putString("channelName",channelName);
                mBundle.putString("token",token);
                serviceIntent.putExtras(mBundle);
                ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);

            }
            else {

                PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                boolean result= Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT_WATCH && powerManager.isInteractive()|| Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT_WATCH&&powerManager.isScreenOn();

                if (!result){
                    PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MH24_SCREENLOCK");
                    wl.acquire(10000);
                    PowerManager.WakeLock wl_cpu = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MH24_SCREENLOCK");
                    wl_cpu.acquire(10000);
                }

//                PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MH24_SCREENLOCK");
//                wl.acquire(10000);
//                PowerManager.WakeLock wl_cpu = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MH24_SCREENLOCK");
//                wl_cpu.acquire(10000);

//                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//                boolean isScreenOn = Build.VERSION.SDK_INT >= 20 ? pm.isInteractive() : pm.isScreenOn(); // check if screen is on
//                if (!isScreenOn) {
//                    PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "myApp:notificationLock");
//                    wl.acquire(3000); //set your time in milliseconds
//                }

                Intent serviceIntent = new Intent(getApplicationContext(), HeadsUpNotificationService.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("inititator", userName);
                mBundle.putString("call_type","Video");
                mBundle.putString("channelName",channelName);
                mBundle.putString("token",token);
                serviceIntent.putExtras(mBundle);
                ContextCompat.startForegroundService(getApplicationContext(), serviceIntent);
            }
        }

        if(showText)
        {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_IMMUTABLE);
            String channelId = getString(R.string.default_notification_channel_id);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.mipmap.logo)
                            .setContentTitle(title)
                            .setContentText(key1)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }
}
