package com.gmail.gardion01.fitnesstracker.service;


import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.fitnesstracker.R;

public class ForegroundService extends Service {
    public static final String SERVICE_ID = "ForegroundService";
    public static final int NOTIFICATION_ID = 1;

    public static NotificationCompat.Builder builder;
    private ActivitySensor activitySensor;
    private SensorManager sensorManager;

    public ForegroundService() {
        builder = new NotificationCompat.Builder(this, SERVICE_ID);
    }

    public static Boolean stopForegroundService() {
        try {
            stopForegroundService();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Notification notification = createNotification("Starting Service");
        startForeground(NOTIFICATION_ID, notification);
        startSensor();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = createNotification("Starting Service");
        startForeground(1, notification);

        return START_STICKY; //If the device killed the application due to low memory, it will restart the service
    }

    private void startSensor() {
        activitySensor = new ActivitySensor(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public Notification createNotification(String text) {
        builder.setContentTitle("Fitness Tracker")
                .setContentText(text)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setSound(null)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_logo);
        Notification notification = builder.build();
        return notification;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activitySensor.destroyAlarm();
    }
}
