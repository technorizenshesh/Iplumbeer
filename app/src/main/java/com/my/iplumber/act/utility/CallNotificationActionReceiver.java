package com.my.iplumber.act.utility;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

import com.my.iplumber.VideoCallingAct;
import com.my.iplumber.act.HomePlumberAct;
import com.my.iplumber.act.PaymentSuccessAct;

/**
 * Created by Ravindra Birla on 04,July,2022
 */
public class CallNotificationActionReceiver extends BroadcastReceiver {

    Context mContext;

    String channelName="",token="",plumberId="";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext=context;
        if (intent != null && intent.getExtras() != null) {

            String action ="";
            action=intent.getStringExtra("ACTION_TYPE");

            if (action != null&& !action.equalsIgnoreCase("")) {

                if(action.equalsIgnoreCase("RECEIVE_CALL"))
                {
                    channelName=intent.getStringExtra("channel");
                    token=intent.getStringExtra("token");
                    plumberId=intent.getStringExtra("plumberId");
                }

                performClickAction(context, action);
            }

// Close the notification after the click action is performed.

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S || "S".equals(Build.VERSION.CODENAME)) {
//                // Android 12 or Android 12 Beta
//                Intent iclose = new Intent();
//                context.sendBroadcast(iclose);
//                context.stopService(new Intent(context, HeadsUpNotificationService.class));

//            } else
//            {
//            Intent iclose = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//            context.sendBroadcast(iclose);
//            context.stopService(new Intent(context, HeadsUpNotificationService.class));

//            }

            Intent iclose = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(iclose);
            context.stopService(new Intent(context, HeadsUpNotificationService.class));

        }

    }
    private void performClickAction(Context context, String action) {
        if(action.equalsIgnoreCase("RECEIVE_CALL")) {

            boolean checkPer = checkAppPermissions();

            Intent intentCallReceive = new Intent(mContext, VideoCallingAct.class).putExtra("id",plumberId)
                    .putExtra("channel_name",channelName) .putExtra("token",token)
                    .putExtra("from","plumber");
            intentCallReceive.putExtra("Call", "incoming");
            intentCallReceive.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intentCallReceive);

//            if (checkPer) {
//                Intent intentCallReceive = new Intent(mContext, VideoCallingAct.class);
//                intentCallReceive.putExtra("Call", "incoming");
//                intentCallReceive.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                mContext.startActivity(intentCallReceive);
//            }
//            else{
//                Intent intent = new Intent(AppController.getContext(), HomePlumberAct.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra("CallFrom","call from push");
//                mContext.startActivity(intent);

//            }

        }
        else if(action.equalsIgnoreCase("DIALOG_CALL")){

            // show ringing activity when phone is locked
            Intent intent = new Intent(AppController.getContext(), VideoCallingAct.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);
        }

        else {

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S || "S".equals(Build.VERSION.CODENAME)) {
//                // Android 12 or Android 12 Beta
//                context.stopService(new Intent(context, HeadsUpNotificationService.class));
//                Intent it = new Intent();
//                context.sendBroadcast(it);
//            }else
//            {

//            context.stopService(new Intent(context, HeadsUpNotificationService.class));
//            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//            context.sendBroadcast(it);
//            }

            context.stopService(new Intent(context, HeadsUpNotificationService.class));
            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(it);

        }
    }

    private Boolean checkAppPermissions() {
        return hasReadPermissions() && hasWritePermissions() && hasCameraPermissions() && hasAudioPermissions();
    }

    private boolean hasAudioPermissions() {
        return (ContextCompat.checkSelfPermission(AppController.getContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(AppController.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(AppController.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }
    private boolean hasCameraPermissions() {
        return (ContextCompat.checkSelfPermission(AppController.getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }
}
