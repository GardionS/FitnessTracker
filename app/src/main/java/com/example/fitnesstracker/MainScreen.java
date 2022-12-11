package com.example.fitnesstracker;

import static com.example.fitnesstracker.ForegroundService.SERVICE_ID;
import static com.example.fitnesstracker.LoginActivity.ID_KEY;

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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.navigation.NavigationBarView;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class MainScreen extends AppCompatActivity {
    NavigationBarView bottomNavigationView;

    public static String MAIN_STEP_COUNTER = "main_step_count";
    public static String MAIN_LAST_STEP_COUNTER = "main_last_step_count";
    public static String MAIN_DATE_STEP = "main_date_count";
    public static String MAIN_DAILY_QUEST = "main_daily_quest";
    private SharedPreferences sharedPreferences;
    public static DatabaseFitness databaseFitnessMain;
    private HomeFragment homeFragment = new HomeFragment();
    private ActivityFragment activityFragment;
    private HistoryFragment historyFragment;
    private ProfileFragment profileFragment;
    public static NotificationManager notificationManager;
    private SensorManager sensorManager;
    private Sensor stepSensor;
    public static int stepCount;
    public static String date;
    private Stack stack = new Stack();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseFitnessMain = new DatabaseFitness(this);
        sharedPreferences =  getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        setContentView(R.layout.main_screen);
        initVariable();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, homeFragment).commit();
//        homeFragment.initUser();
            stack.add("home");
        }
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, homeFragment).addToBackStack(null).commit();
                        stack.add("home");
                        return true;
                    case R.id.activity:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, activityFragment).addToBackStack(null).commit();
                        stack.add("activity");
                        return true;
                    case R.id.history:
                        stack.add("history");
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, historyFragment).addToBackStack(null).commit();
                        return true;
                    case R.id.profile:
                        stack.add("profile");
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, profileFragment).addToBackStack(null).commit();
                        return true;
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

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        homeFragment = new HomeFragment();
        activityFragment = new ActivityFragment(new DatabaseFitness(this));
        historyFragment = new HistoryFragment(new DatabaseFitness(this));
        profileFragment = new ProfileFragment();

        startForegroundServices();
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseFitnessMain.updateFitnessWalk(sharedPreferences.getInt(ID_KEY, 0), sharedPreferences.getString(MAIN_DATE_STEP, ""), sharedPreferences.getInt(MAIN_STEP_COUNTER, 0));
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

    @Override
    public void onBackPressed() {
        Menu menu = bottomNavigationView.getMenu();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.popBackStackImmediate();
//        int fragmentId = getCurrentFragment().getId();
//        if(fragmentId == homeFragment.getId()) {
//            menu.getItem(0).setChecked(true);
//        } else if(fragmentId == activityFragment.getId()) {
//            menu.getItem(1).setChecked(true);
//        } else if (fragmentId == historyFragment.getId()) {
//            menu.getItem(2).setChecked(true);
//        } else if (fragmentId == profileFragment.getId()) {
//            menu.getItem(3).setChecked(true);
//        }

        String backFragment = (String) stack.pop();
        switch (backFragment) {
            case "home":
                menu.getItem(0).setChecked(true);
                break;
            case "activity":
                menu.getItem(1).setChecked(true);
                break;
            case "history":
                menu.getItem(2).setChecked(true);
                break;
            case "profile" :
                menu.getItem(3).setChecked(true);
                break;
        }
//        final Fragment currentFragment = getFragmentManager().getPrimaryNavigationFragment().getChildFragmentManager().getFragments().get(0);
//        final NavController controller = Navigation.findNavController(this, R.id.nav_host_fragment);
//        if (currentFragment instanceof IOnBackPressed)
//            ((OnBackPressedListener) currentFragment).onBackPressed();
//        else if (!controller.popBackStack())
//            finish();
    }
    public Fragment getCurrentFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }
}
