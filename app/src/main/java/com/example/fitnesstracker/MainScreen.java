package com.example.fitnesstracker;

import static com.example.fitnesstracker.ForegroundService.SERVICE_ID;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationBarView;

import java.util.Iterator;
import java.util.List;

public class MainScreen extends AppCompatActivity implements SensorEventListener {
    NavigationBarView bottomNavigationView;

    public static String MAIN_STEP_COUNTER = "main_step_count";
    public static String MAIN_LAST_STEP_COUNTER = "main_last_step_count";
    public static String MAIN_DATE_STEP = "main_date_count";
    private SharedPreferences sharedPreferences;
    private HomeFragment homeFragment = new HomeFragment();
    private ActivityFragment activityFragment;
    private HistoryFragment historyFragment;
    private ProfileFragment profileFragment;
    public static NotificationManager notificationManager;
    private SensorManager sensorManager;
    private Sensor stepSensor;
    public static int stepCount;
    public static String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences =  getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        setContentView(R.layout.main_screen);
        initVariable();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, homeFragment).commit();
//        homeFragment.initUser();
        }
//        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);
//        Fragment fragment = null;
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
//                        startForegroundServices();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, homeFragment).commit();
                        return true;
                    case R.id.activity:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, activityFragment).commit();
                        return true;
                    case R.id.history:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, historyFragment).commit();
                        return true;
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, profileFragment).commit();
                }

                selectMenu(item.getItemId());
                return false;
            }
        });

        isServiceRunningInForeground(this, ForegroundService.class);
//        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    private void selectMenu(int id) {
        Menu menu = bottomNavigationView.getMenu();
        for (int i =0 ;i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if(item.getItemId() == id) {
                item.setChecked(true);
                return;
            }
        }
    }
    private void initVariable() {
//        foregroundService = new ForegroundService();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        homeFragment = new HomeFragment();
        activityFragment = new ActivityFragment(new DatabaseFitness(MainScreen.this));
        historyFragment = new HistoryFragment(new DatabaseFitness(MainScreen.this));
        profileFragment = new ProfileFragment();

        startForegroundServices();
    }
    public void onSensorChanged(SensorEvent sensorEvent) {
        int currentStep = (int) sensorEvent.values[0];
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(MAIN_STEP_COUNTER, 0);
        editor.putInt(MAIN_LAST_STEP_COUNTER, 0);
//        if(sharedPreferences.getString(MAIN_DATE_STEP,"").equals(DatabaseMain.getCurrentDate("dd-MM-yyyy"))) { //data from today
//            if(sharedPreferences.getInt(MAIN_LAST_STEP_COUNTER, 0) >= currentStep) {//Phone has reboot
//                editor.putInt(MAIN_LAST_STEP_COUNTER, currentStep);
//                editor.putInt(MAIN_STEP_COUNTER, currentStep);
//                stepCount = currentStep;
//            } else { //Phone has not reboot
//
//                editor.putInt(MAIN_STEP_COUNTER, sharedPreferences.getInt(MAIN_STEP_COUNTER,0) + stepCount);
//                editor.putInt(MAIN_LAST_STEP_COUNTER, currentStep);
//            }
//        } else { //data from yesterday
//            editor.putString(MAIN_DATE_STEP, DatabaseMain.getCurrentDate("dd-MM-yyyy"));
//            date = DatabaseMain.getCurrentDate("dd-MM-yyyy");
//            if(currentStep > sharedPreferences.getInt(MAIN_LAST_STEP_COUNTER, 0)) { //Phone not reboot
//                stepCount = currentStep - sharedPreferences.getInt(MAIN_LAST_STEP_COUNTER, 0);
//                editor.putInt(MAIN_LAST_STEP_COUNTER, currentStep);
//                editor.putInt(MAIN_STEP_COUNTER, stepCount);
//            } else { //phone has reboot
//                stepCount = currentStep;
//                editor.putInt(MAIN_LAST_STEP_COUNTER, currentStep);
//                editor.putInt(MAIN_STEP_COUNTER, currentStep);
//            }
//        }
        editor.apply();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //Nothing change regardless how not accurate it is
    }
    public static boolean isServiceRunningInForeground(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return service.foreground;
            }
        }
        return false;
    }

    private void startForegroundServices(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    SERVICE_ID, "TestingService", NotificationManager.IMPORTANCE_LOW
            );
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Intent foregroundService = new Intent(this, ForegroundService.class);
//        foregroundService.putExtra()
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(this, foregroundService);
//                startService(foregroundService);
            } else {
                startService(foregroundService);
            }
        } catch (RuntimeException e) {
        }
    }


    public static int getStepCount() {
        return stepCount;
    }

    public static String getDate() {
        return date;
    }
}