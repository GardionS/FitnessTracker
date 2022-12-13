package com.gmail.gardion01.fitnesstracker.receiver;

import static com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity.EMAIL_KEY;
import static com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity.SHARED_PREFS;
import static com.gmail.gardion01.fitnesstracker.service.ForegroundService.SERVICE_ID;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.content.ContextCompat;

import com.gmail.gardion01.fitnesstracker.service.ForegroundService;

public class StartOnBootUpReceiver extends BroadcastReceiver {
    private SharedPreferences sharedPreferences;
    private NotificationManager notificationManager;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) && !sharedPreferences.getString(EMAIL_KEY, "").equals("")) {
            startForegroundServices();
        }

    }

    private void startForegroundServices() { //Start the foreground service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(SERVICE_ID, "FitnessTracker", NotificationManager.IMPORTANCE_LOW);
            notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel); //Create notification
        }
        Intent foregroundService = new Intent(context, ForegroundService.class);
        foregroundService.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context, foregroundService); //For upper android version
        } else {
            context.startService(foregroundService); //For lower android version
        }
    }

}
