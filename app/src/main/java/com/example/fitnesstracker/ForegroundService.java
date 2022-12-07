package com.example.fitnesstracker;


import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ForegroundService extends Service {
    private ActivitySensor activitySensor;
    private SensorManager sensorManager;
    //    private Context context;
    private final int count;
    private final String date;
    public static NotificationCompat.Builder builder;
    public static final String SERVICE_ID = "ForegroundService";
    public static final int NOTIFICATION_ID = 3;
    public ForegroundService() {
        this.count = MainScreen.getStepCount();
        this.date = MainScreen.getDate();
        builder = new NotificationCompat.Builder(this, SERVICE_ID);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Notification notification = createNotification("Starting Service");
        startForeground(1, notification);
        startSensor();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("te", "startCom");
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

    public static Boolean stopForegroundService() {
        try {
            stopForegroundService();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Notification createNotification(String text) {
        Intent notificationIntent = new Intent(this, MainScreen.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentTitle("Fitness Tracker")
                .setContentText(text)
                .setPriority(1)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_logo);
        Notification notification = builder.build();
        return notification;
    }
}
