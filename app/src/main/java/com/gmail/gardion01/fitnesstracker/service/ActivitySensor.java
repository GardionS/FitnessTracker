package com.gmail.gardion01.fitnesstracker.service;

import static android.content.Context.ALARM_SERVICE;
import static com.gmail.gardion01.fitnesstracker.controller.activity.HomeActivity.MAIN_DAILY_QUEST;
import static com.gmail.gardion01.fitnesstracker.controller.activity.HomeActivity.MAIN_DAILY_QUEST_TARGET;
import static com.gmail.gardion01.fitnesstracker.controller.activity.HomeActivity.MAIN_DATE_STEP;
import static com.gmail.gardion01.fitnesstracker.controller.activity.HomeActivity.MAIN_LAST_STEP_COUNTER;
import static com.gmail.gardion01.fitnesstracker.controller.activity.HomeActivity.MAIN_STEP_COUNTER;
import static com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity.EXP_KEY;
import static com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity.ID_KEY;
import static com.gmail.gardion01.fitnesstracker.enumeration.FitnessType.WALKING;
import static com.gmail.gardion01.fitnesstracker.enumeration.QuestType.DAILY_QUEST;
import static com.gmail.gardion01.fitnesstracker.service.ForegroundService.SERVICE_ID;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;

import com.gmail.gardion01.fitnesstracker.controller.activity.HomeActivity;
import com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity;
import com.gmail.gardion01.fitnesstracker.database.DatabaseFitness;
import com.gmail.gardion01.fitnesstracker.database.DatabaseMain;
import com.gmail.gardion01.fitnesstracker.database.DatabaseQuest;
import com.gmail.gardion01.fitnesstracker.database.DatabaseUser;
import com.gmail.gardion01.fitnesstracker.receiver.UpdateDatabaseReceiver;

import java.util.HashMap;

public class ActivitySensor implements SensorEventListener {
    private final int MINUTE = 60 * 1000;
    private final Context context;
    private SensorManager sensorManager;
    private SharedPreferences sharedPreferences;
    private Sensor stepSensor;
    private int stepCount;
    private DatabaseFitness databaseFitness;
    private DatabaseUser databaseUser;
    private AlarmManager alarmManager;
    private HashMap<Integer, Boolean> questList; //If there is multiple quest
    private NotificationManager notificationManager;
    public ActivitySensor(Context context) {
        this.context = context;
        initVariable();
    }

    public void initVariable() { //Initialize all variable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(SERVICE_ID, "FitnessTracker", NotificationManager.IMPORTANCE_LOW);
            notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel); //Create notification
        }
        questList = new HashMap<>();
        sharedPreferences = context.getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        databaseFitness = new DatabaseFitness(context);

        databaseUser = new DatabaseUser(context);

        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        stepCount = sharedPreferences.getInt(HomeActivity.MAIN_STEP_COUNTER, 0);

        databaseFitness.getFitness(sharedPreferences.getInt(ID_KEY, 0), WALKING.getValue(), sharedPreferences.getString(MAIN_DATE_STEP, ""));//Just to check if the data exist in database
        ForegroundService.builder.setPriority(Notification.PRIORITY_DEFAULT);
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);
        updateNotification();
        setAutoUpdating();
        questList.put(DAILY_QUEST.getValue(), sharedPreferences.getBoolean(HomeActivity.MAIN_DAILY_QUEST, false));
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) { //Sensor value updated
        /*
         * 1. Check if the date stored is today or not. This will show how old the data stored in the phone since it use sharedPreference
         * 2. If date store today, then check whether the value is zero since it means it's a new user or a new login
         * 3. If not then check whether the last step counter is bigger then current value show up in the sensor, if yes means that the phone just restarted because the sensor will reset every restart
         * 4. If not then check whether the last step counter is 0, because if the user just login, the last step counter is zero
         * 5. If not, the phone has not yet been restarted and it will calculate the value based on dataStore + current - lastStep
         * 6. If not continue from point 1. That means the data is from yesterday, check if the currentStep is higher meaning Phone not yet reboot *Note, the user can also workout before starting the app which can make this false
         * 7. If not then that means the phone has been restarted and will store based on new data
         * */
        int currentStep = (int) sensorEvent.values[0];
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.getString(MAIN_DATE_STEP, "").equals(DatabaseMain.getCurrentDate("dd-MM-yyyy"))) { //data from today
            if (sharedPreferences.getInt(MAIN_STEP_COUNTER, 0) == 0) { //New user or application just started
                editor.putInt(MAIN_STEP_COUNTER, 1); //Because sensor trigger so the user must have at least walk 1 step
                editor.putInt(MAIN_LAST_STEP_COUNTER, currentStep);
            } else if (sharedPreferences.getInt(MAIN_LAST_STEP_COUNTER, 0) > currentStep) {//Phone has reboot because step will reset to 0 after reboot
                stepCount = sharedPreferences.getInt(MAIN_STEP_COUNTER, 0) + currentStep; //The data  show it's from the same day so just add with current sensor value
                editor.putInt(MAIN_LAST_STEP_COUNTER, currentStep);
                editor.putInt(MAIN_STEP_COUNTER, stepCount);
            } else if (sharedPreferences.getInt(MAIN_LAST_STEP_COUNTER, 0) == 0) {//User just log in
                editor.putInt(MAIN_LAST_STEP_COUNTER, currentStep);
            } else { //Phone has not reboot
                stepCount = sharedPreferences.getInt(HomeActivity.MAIN_STEP_COUNTER, 0) + currentStep - sharedPreferences.getInt(HomeActivity.MAIN_LAST_STEP_COUNTER, 0);
                editor.putInt(MAIN_STEP_COUNTER, stepCount);
                editor.putInt(MAIN_LAST_STEP_COUNTER, currentStep);
            }
        } else { //data from yesterday
            editor.putString(MAIN_DATE_STEP, DatabaseMain.getCurrentDate("dd-MM-yyyy"));
            databaseFitness.addFitness(sharedPreferences.getInt(ID_KEY, 0), WALKING.getValue());
            databaseFitness.updateFitness(sharedPreferences.getInt(ID_KEY, 0), WALKING.getValue(), sharedPreferences.getString(MAIN_DATE_STEP, ""), sharedPreferences.getInt(HomeActivity.MAIN_STEP_COUNTER, 0));
            if (currentStep > sharedPreferences.getInt(MAIN_LAST_STEP_COUNTER, 0)) { //Phone not reboot
                stepCount = currentStep - sharedPreferences.getInt(MAIN_LAST_STEP_COUNTER, 0);
                editor.putInt(MAIN_STEP_COUNTER, stepCount);
                editor.putInt(MAIN_LAST_STEP_COUNTER, currentStep);
            } else { //phone has reboot
                stepCount = currentStep;
                editor.putInt(MAIN_LAST_STEP_COUNTER, currentStep);
                editor.putInt(MAIN_STEP_COUNTER, currentStep);
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
            databaseQuest.completeQuest(sharedPreferences.getInt(ID_KEY, 0), DAILY_QUEST.getValue(), DatabaseMain.getCurrentDate("dd-MM-yyyy"), sharedPreferences.getBoolean(MAIN_DAILY_QUEST, false));
            databaseUser.addExp(sharedPreferences.getInt(ID_KEY, 0), sharedPreferences.getInt(EXP_KEY, 0));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //Ignore regardless the accuracy
    }

    private void updateNotification() { //Update notification to indicate live
        ForegroundService.builder.setContentText("You have walk " + stepCount + " steps");
        notificationManager.notify(ForegroundService.NOTIFICATION_ID, ForegroundService.builder.build());
    }

    private void setAutoUpdating() { //Set updating for the database
        alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, UpdateDatabaseReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 12, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, 15 * MINUTE, 15 * MINUTE, pendingIntent); //Update database very 15 mintues
    }

    public void destroyAlarm() { //Destroy the alarm
        Intent intent = new Intent(context, UpdateDatabaseReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 12, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
    }
}
