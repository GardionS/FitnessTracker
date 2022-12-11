package com.example.fitnesstracker;


import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ForegroundService extends Service {
    public static final String SERVICE_ID = "ForegroundService";
    public static final int NOTIFICATION_ID = 1;
    public static NotificationCompat.Builder builder;
    //    private Context context;
    private final int count;
    private final String date;
    private ActivitySensor activitySensor;
    private SensorManager sensorManager;

    public ForegroundService() {
        this.count = MainScreen.getStepCount();
        this.date = MainScreen.getDate();
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

        return START_STICKY;
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
        Intent notificationIntent = new Intent(this, MainScreen.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentTitle("Fitness Tracker")
                .setContentText(text)
                .setPriority(Notification.PRIORITY_LOW)
                .setSound(null)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_logo);
        Notification notification = builder.build();
        return notification;
    }

    private void updateDatabase() {

    }
}
