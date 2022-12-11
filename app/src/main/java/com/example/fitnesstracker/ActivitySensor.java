package com.example.fitnesstracker;

import static android.content.Context.ALARM_SERVICE;
import static com.example.fitnesstracker.ForegroundService.NOTIFICATION_ID;
import static com.example.fitnesstracker.ForegroundService.builder;
import static com.example.fitnesstracker.LoginActivity.EXP_KEY;
import static com.example.fitnesstracker.LoginActivity.ID_KEY;
import static com.example.fitnesstracker.MainScreen.MAIN_DAILY_QUEST;
import static com.example.fitnesstracker.MainScreen.MAIN_DATE_STEP;
import static com.example.fitnesstracker.MainScreen.MAIN_LAST_STEP_COUNTER;
import static com.example.fitnesstracker.MainScreen.MAIN_STEP_COUNTER;
import static com.example.fitnesstracker.MainScreen.notificationManager;
import static com.example.fitnesstracker.QuestType.DAILY_QUEST;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.HashMap;

public class ActivitySensor implements SensorEventListener {
    private final int MINUTE = 60 * 60;
    private final Context context;
    private SensorManager sensorManager;
    private SharedPreferences sharedPreferences;
    private Sensor stepSensor;
    private int stepCount;
    private DatabaseFitness databaseFitness;
    private DatabaseUser databaseUser;
    private AlarmManager alarmManager;
    private HashMap<Integer, Boolean> questList;

    public ActivitySensor(Context context) {
        this.context = context;
        initVariable();
    }

    public void initVariable() {
        questList = new HashMap<>();
        sharedPreferences = context.getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        databaseFitness = new DatabaseFitness(context);
        databaseUser = new DatabaseUser(context);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        stepCount = sharedPreferences.getInt(MAIN_STEP_COUNTER, 0);

        databaseFitness.getFitness(sharedPreferences.getInt(ID_KEY, 0), sharedPreferences.getString(MAIN_DATE_STEP, ""));//Just to check if the data exist in database

        builder.setPriority(Notification.PRIORITY_DEFAULT);
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);

        updateNotification();

        setAutoUpdating();
        questList.put(DAILY_QUEST.getValue(), sharedPreferences.getBoolean(MAIN_DAILY_QUEST, false));
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
            databaseFitness.addFitness(sharedPreferences.getInt(ID_KEY, 0));
            databaseFitness.updateFitnessWalk(sharedPreferences.getInt(ID_KEY, 0), sharedPreferences.getString(MAIN_DATE_STEP, ""), sharedPreferences.getInt(MAIN_STEP_COUNTER, 0));
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
        checkMission();
    }

    private void checkMission() {
        if (questList.get(DAILY_QUEST) && sharedPreferences.getInt(MAIN_STEP_COUNTER, 0) > 6000) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(MAIN_DAILY_QUEST, true);
            editor.putInt(EXP_KEY, sharedPreferences.getInt(EXP_KEY, 0) + 200);
            editor.commit();
            questList.put(DAILY_QUEST.getValue(), true);
            databaseUser.completeDailyQuest(sharedPreferences.getInt(ID_KEY, 0), sharedPreferences.getInt(EXP_KEY, 0));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //Ignore regardless the accuracy
    }

    private void updateNotification() {
        builder.setContentText("You have walk " + stepCount + " steps");
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void setAutoUpdating() {

        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, UpdateDatabase.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 12, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, 15 * MINUTE, 15 * MINUTE, pendingIntent);
    }
}
