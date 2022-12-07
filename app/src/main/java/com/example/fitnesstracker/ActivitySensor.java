package com.example.fitnesstracker;

import static com.example.fitnesstracker.ForegroundService.NOTIFICATION_ID;
import static com.example.fitnesstracker.ForegroundService.SERVICE_ID;
import static com.example.fitnesstracker.ForegroundService.builder;
import static com.example.fitnesstracker.LoginActivity.ID_KEY;
import static com.example.fitnesstracker.MainScreen.MAIN_DATE_STEP;
import static com.example.fitnesstracker.MainScreen.MAIN_LAST_STEP_COUNTER;
import static com.example.fitnesstracker.MainScreen.MAIN_STEP_COUNTER;
import static com.example.fitnesstracker.MainScreen.notificationManager;

import android.app.Activity;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ActivitySensor implements SensorEventListener {
    private SensorManager sensorManager;
    private SharedPreferences sharedPreferences;
    private Sensor stepSensor;
    private Context context;
    private int stepCount;
    public ActivitySensor(Context context) {
        this.context = context;
        initVariable();
    }

    public void initVariable() {
        sharedPreferences = context.getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);
        stepCount = 0;
    }

    public int getStepCount() {
        return stepCount;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int currentStep = (int) sensorEvent.values[0];
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.getString(MAIN_DATE_STEP, "").equals(DatabaseMain.getCurrentDate("dd-MM-yyyy"))) { //data from today
            if (sharedPreferences.getInt(MAIN_LAST_STEP_COUNTER, 0) > currentStep) {//Phone has reboot
                stepCount = sharedPreferences.getInt(MAIN_STEP_COUNTER, 0) + currentStep; //The data  show it's from the same day so just add with current sensor value
                editor.putInt(MAIN_LAST_STEP_COUNTER, currentStep);
                editor.putInt(MAIN_STEP_COUNTER, stepCount);
            } else { //Phone has not reboot
                stepCount = sharedPreferences.getInt(MAIN_STEP_COUNTER, 0) + currentStep - sharedPreferences.getInt(MAIN_LAST_STEP_COUNTER, 0);
                editor.putInt(MAIN_STEP_COUNTER, stepCount);
                editor.putInt(MAIN_LAST_STEP_COUNTER, currentStep);
            }
        } else { //data from yesterday
            editor.putString(MAIN_DATE_STEP, DatabaseMain.getCurrentDate("dd-MM-yyyy"));
            DatabaseFitness databaseFitness = new DatabaseFitness(context);
            databaseFitness.updateFitnessValue(sharedPreferences.getInt(ID_KEY, 0), sharedPreferences.getString(MAIN_DATE_STEP, ""), sharedPreferences.getInt(MAIN_STEP_COUNTER, 0));
            if (currentStep > sharedPreferences.getInt(MAIN_LAST_STEP_COUNTER, 0)) { //Phone not reboot
                stepCount = currentStep - sharedPreferences.getInt(MAIN_LAST_STEP_COUNTER, 0);
                editor.putInt(MAIN_LAST_STEP_COUNTER, currentStep);
                editor.putInt(MAIN_STEP_COUNTER, stepCount);
            } else { //phone has reboot
                stepCount = currentStep;
                editor.putInt(MAIN_LAST_STEP_COUNTER, currentStep);
                editor.putInt(MAIN_STEP_COUNTER, currentStep);
            }
        }
        editor.apply();
        updateNotification();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //Ignore regardless the accuracy
    }
    private void updateNotification() {
        builder.setContentText("You have walk" + stepCount + " steps");
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
