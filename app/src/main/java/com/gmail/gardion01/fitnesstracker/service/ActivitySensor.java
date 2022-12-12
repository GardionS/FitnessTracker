package com.gmail.gardion01.fitnesstracker.service;

import static android.content.Context.ALARM_SERVICE;
import static com.gmail.gardion01.fitnesstracker.controller.activity.HomeActivity.MAIN_DAILY_QUEST_TARGET;
import static com.gmail.gardion01.fitnesstracker.controller.activity.HomeActivity.MAIN_STEP_COUNTER;
import static com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity.EXP_KEY;
import static com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity.ID_KEY;
import static com.gmail.gardion01.fitnesstracker.enumeration.FitnessType.WALKING;
import static com.gmail.gardion01.fitnesstracker.enumeration.QuestType.DAILY_QUEST;

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

import com.gmail.gardion01.fitnesstracker.controller.activity.HomeActivity;
import com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity;
import com.gmail.gardion01.fitnesstracker.database.DatabaseFitness;
import com.gmail.gardion01.fitnesstracker.database.DatabaseMain;
import com.gmail.gardion01.fitnesstracker.database.DatabaseQuest;
import com.gmail.gardion01.fitnesstracker.database.DatabaseUser;
import com.gmail.gardion01.fitnesstracker.enumeration.QuestType;
import com.gmail.gardion01.fitnesstracker.service.ForegroundService;
import com.gmail.gardion01.fitnesstracker.service.UpdateDatabaseReceiver;

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
    private HashMap<Integer, Boolean> questList; //If there is multiple quest

    public ActivitySensor(Context context) {
        this.context = context;
        initVariable();
    }
    public void initVariable() { //Initialize all variable
        questList = new HashMap<>();
        sharedPreferences = context.getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        databaseFitness = new DatabaseFitness(context);
        databaseUser = new DatabaseUser(context);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        stepCount = sharedPreferences.getInt(HomeActivity.MAIN_STEP_COUNTER, 0);

        databaseFitness.getFitness(sharedPreferences.getInt(ID_KEY, 0), WALKING.getValue(), sharedPreferences.getString(HomeActivity.MAIN_DATE_STEP, ""));//Just to check if the data exist in database

        ForegroundService.builder.setPriority(Notification.PRIORITY_DEFAULT);
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);

        updateNotification();

        setAutoUpdating();
        questList.put(DAILY_QUEST.getValue(), sharedPreferences.getBoolean(HomeActivity.MAIN_DAILY_QUEST, false));
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) { //Sensor value updated
        int currentStep = (int) sensorEvent.values[0];
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.getString(HomeActivity.MAIN_DATE_STEP, "").equals(DatabaseMain.getCurrentDate("dd-MM-yyyy"))) { //data from today
            if (sharedPreferences.getInt(HomeActivity.MAIN_LAST_STEP_COUNTER, 0) > currentStep) {//Phone has reboot
                stepCount = sharedPreferences.getInt(HomeActivity.MAIN_STEP_COUNTER, 0) + currentStep; //The data  show it's from the same day so just add with current sensor value
                editor.putInt(HomeActivity.MAIN_LAST_STEP_COUNTER, currentStep);
                editor.putInt(HomeActivity.MAIN_STEP_COUNTER, stepCount);
            } else { //Phone has not reboot
                stepCount = sharedPreferences.getInt(HomeActivity.MAIN_STEP_COUNTER, 0) + currentStep - sharedPreferences.getInt(HomeActivity.MAIN_LAST_STEP_COUNTER, 0);
                editor.putInt(HomeActivity.MAIN_STEP_COUNTER, stepCount);
                editor.putInt(HomeActivity.MAIN_LAST_STEP_COUNTER, currentStep);
            }
        } else { //data from yesterday
            editor.putString(HomeActivity.MAIN_DATE_STEP, DatabaseMain.getCurrentDate("dd-MM-yyyy"));
            databaseFitness.addFitness(sharedPreferences.getInt(ID_KEY, 0), WALKING.getValue());
            databaseFitness.updateFitness(sharedPreferences.getInt(ID_KEY, 0), WALKING.getValue(), sharedPreferences.getString(HomeActivity.MAIN_DATE_STEP, ""), sharedPreferences.getInt(HomeActivity.MAIN_STEP_COUNTER, 0));
            if (currentStep > sharedPreferences.getInt(HomeActivity.MAIN_LAST_STEP_COUNTER, 0)) { //Phone not reboot
                stepCount = currentStep - sharedPreferences.getInt(HomeActivity.MAIN_LAST_STEP_COUNTER, 0);
                editor.putInt(HomeActivity.MAIN_STEP_COUNTER, stepCount);
            } else { //phone has reboot
                stepCount = currentStep;
                editor.putInt(HomeActivity.MAIN_LAST_STEP_COUNTER, currentStep);
                editor.putInt(HomeActivity.MAIN_STEP_COUNTER, currentStep);
            }
        }
        editor.apply();
        updateNotification(); //Update notification
        checkMission(); //Check if the user has completed mission
    }

    private void checkMission() { //Check if the mission is complete
        if (!questList.get(DAILY_QUEST.getValue()) && sharedPreferences.getInt(HomeActivity.MAIN_STEP_COUNTER, 0) > sharedPreferences.getInt(MAIN_DAILY_QUEST_TARGET, 6000)) { //Fast checker by using hashmap whether the task is completed
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(HomeActivity.MAIN_DAILY_QUEST, true);
            editor.putInt(EXP_KEY, sharedPreferences.getInt(EXP_KEY, 0) + 200);
            editor.commit();
            questList.put(DAILY_QUEST.getValue(), true);
            DatabaseQuest databaseQuest = new DatabaseQuest(context);
            databaseQuest.completeQuest(sharedPreferences.getInt(ID_KEY, 0), DAILY_QUEST.getValue(), DatabaseMain.getCurrentDate("dd-MM-yyyy"), sharedPreferences.getInt(MAIN_STEP_COUNTER, 0));
            databaseUser.addExp(sharedPreferences.getInt(ID_KEY, 0), sharedPreferences.getInt(EXP_KEY, 0));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //Ignore regardless the accuracy
    }

    private void updateNotification() { //Update notification to indicate live
        ForegroundService.builder.setContentText("You have walk " + stepCount + " steps");
        HomeActivity.notificationManager.notify(ForegroundService.NOTIFICATION_ID, ForegroundService.builder.build());
    }

    private void setAutoUpdating() { //Set updating for the database
        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, UpdateDatabaseReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 12, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, 15 * MINUTE, 15 * MINUTE, pendingIntent); //Update database very 15 mintues
    }
    public void destroyAlarm() { //Destroy the alarm
        Intent intent = new Intent(context, UpdateDatabaseReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 13, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
    }
}
