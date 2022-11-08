package com.example.fitnesstracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

public class MainActivity extends AppCompatActivity {

    MainScreen mainScreen = new MainScreen();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        Intent intent = new Intent();
        intent.setClass(this, com.example.fitnesstracker.MainScreen.class);
        startActivity(intent);
//        setContentView(R.layout.splash_screen);

    }


    public void goToMainLayoutButton(View view){

        setContentView(R.layout.main_screen);
        Intent intent = new Intent();
        intent.setClass(this, com.example.fitnesstracker.MainScreen.class);
        startActivity(intent);
    }
}