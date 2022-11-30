package com.example.fitnesstracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;

public class MainScreen extends AppCompatActivity{
    NavigationBarView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    SecondFragment secondFragment = new SecondFragment();
    HistoryFragment historyFragment = new HistoryFragment();
    EditProfileFragment editProfileFragment = new EditProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,homeFragment).commit();
                        return true;
                    case R.id.activity:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,secondFragment).commit();
                        return true;
                    case R.id.history:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,historyFragment).commit();
                        return true;
                    case R.id.editProfile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, editProfileFragment).commit();
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.home);
    }
}