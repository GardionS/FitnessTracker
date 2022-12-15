package com.gmail.gardion01.fitnesstracker.controller.activity;

import static com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity.ID_KEY;
import static com.gmail.gardion01.fitnesstracker.enumeration.FitnessType.WALKING;
import static com.gmail.gardion01.fitnesstracker.service.ForegroundService.SERVICE_ID;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.fitnesstracker.R;
import com.gmail.gardion01.fitnesstracker.controller.fragment.ActivityFragment;
import com.gmail.gardion01.fitnesstracker.controller.fragment.HistoryFragment;
import com.gmail.gardion01.fitnesstracker.controller.fragment.HomeFragment;
import com.gmail.gardion01.fitnesstracker.controller.fragment.ProfileFragment;
import com.gmail.gardion01.fitnesstracker.database.DatabaseFitness;
import com.gmail.gardion01.fitnesstracker.service.ForegroundService;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Stack;

public class HomeActivity extends AppCompatActivity {
    public static String MAIN_STEP_COUNTER = "main_step_count";
    public static String MAIN_LAST_STEP_COUNTER = "main_last_step_count";
    public static String MAIN_DATE_STEP = "main_date_count";
    public static String MAIN_DAILY_QUEST = "main_daily_quest";
    public static String MAIN_DAILY_QUEST_TARGET = "main_daily_quest_target";
    public static String MAIN_DAILY_STEP_TARGET = "main_daily_step_target";
    public static String MAIN_DAILY_RUNNING_TARGET = "main_daily_running_target";
    public static NotificationManager notificationManager;
    private Stack stack = new Stack();
    public DatabaseFitness databaseFitness;
    NavigationBarView bottomNavigationView;
    private SharedPreferences sharedPreferences;
    private HomeFragment homeFragment = new HomeFragment();
    private ActivityFragment activityFragment;
    private HistoryFragment historyFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        initVariable();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, homeFragment).commit();
            stack.add("home");
        }
        initBottomNavigation();
    }

    private void initBottomNavigation() { //Select bottom navigation and replace the frame with the fragment.
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, homeFragment).addToBackStack(null).commit(); //Change the frame to homeFragment
                        stack.add("home"); //Add to the history
                        return true;
                    case R.id.activity:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, activityFragment).addToBackStack(null).commit(); //Change the frame to activityFragment
                        stack.add("activity");//Add to the history
                        return true;
                    case R.id.history:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, historyFragment).addToBackStack(null).commit(); //Change the frame to historyFragment
                        stack.add("history");//Add to the history
                        return true;
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, profileFragment).addToBackStack(null).commit(); //Change the frame to profileFragment
                        stack.add("profile");//Add to the history
                        return true;
                }

                selectMenu(item.getItemId());
                return false;
            }
        });
    }

    private void selectMenu(int id) { //Highlight selected menu
        Menu menu = bottomNavigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.getItemId() == id) {
                item.setChecked(true);
                return;
            }
        }
    }

    private void initVariable() { //Initialize all variable

        databaseFitness = new DatabaseFitness(this);
        sharedPreferences = getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        homeFragment = new HomeFragment();
        activityFragment = new ActivityFragment();
        historyFragment = new HistoryFragment();
        profileFragment = new ProfileFragment();

        startForegroundServices(); //Start the foreground service
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseFitness.updateFitness(sharedPreferences.getInt(ID_KEY, 0), WALKING.getValue(), sharedPreferences.getString(MAIN_DATE_STEP, ""), sharedPreferences.getInt(MAIN_STEP_COUNTER, 0)); //Update database when application is pause/closed
    }

    private void startForegroundServices() { //Start the foreground service
        Intent foregroundService = new Intent(this, ForegroundService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, foregroundService); //For upper android version
        } else {
            startService(foregroundService); //For lower android version
        }
    }


    @Override
    public void onBackPressed() { //Press back button and will go to the history to check which button was pressed last
        Menu menu = bottomNavigationView.getMenu();

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
            case "profile":
                menu.getItem(3).setChecked(true);
                break;
        }
    }
}
