package com.example.fitnesstracker;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;

import com.example.fitnesstracker.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainScreen extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    ActivityMainBinding binding;
    BottomNavigationView bottomNavigationView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemReselectedListener((NavigationBarView.OnItemReselectedListener) this);
        bottomNavigationView.setSelectedItemId(R.id.home);
        }
        HomeFragment homeFragment = new HomeFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,homeFragment).commit();
                return true;
        }
        return false;
    }

}