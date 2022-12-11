package com.example.fitnesstracker;

import static com.example.fitnesstracker.MainScreen.MAIN_DATE_STEP;
import static com.example.fitnesstracker.MainScreen.MAIN_STEP_COUNTER;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateDatabase extends BroadcastReceiver {
    private DatabaseFitness databaseFitness;
    private SharedPreferences sharedPreferences;
    private Context context;

    private void initVariable() {
        databaseFitness = new DatabaseFitness(context);
        sharedPreferences = context.getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
    }
    private void updateDatabase() {
        databaseFitness.updateFitnessWalk(sharedPreferences.getInt(LoginActivity.ID_KEY,0), sharedPreferences.getString(MAIN_DATE_STEP, ""), sharedPreferences.getInt(MAIN_STEP_COUNTER, 0));
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        initVariable();
        updateDatabase();
    }
}
