package com.example.fitnesstracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.example.fitnesstracker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    MainScreen mainScreen = new MainScreen();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

    }


    public void goToMainLayoutButton(View view){

        setContentView(R.layout.main_screen);
        Intent intent = new Intent();
        intent.setClass(this, com.example.fitnesstracker.MainScreen.class);
        startActivity(intent);
    }
}