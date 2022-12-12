package com.gmail.gardion01.fitnesstracker.controller.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.fitnesstracker.R;
import com.gmail.gardion01.fitnesstracker.controller.activity.HomeActivity;
import com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity;
import com.gmail.gardion01.fitnesstracker.database.DatabaseFitness;
import com.gmail.gardion01.fitnesstracker.model.Fitness;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HistoryFragment extends Fragment implements View.OnClickListener {
    private final FragmentActivity fragment = getActivity();
    private DatabaseFitness databaseFitness;
    private TextView historyCaloriesWalking, historyStepsWalking, historyDate;
    private SharedPreferences sharedPreferences;
    private ImageButton historyPreviousDay, historyNextDay;
    private Date date;
    private ProgressBar historyWalkingBar;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVariable();
        initValue();
        initListener();
    }

    private void initVariable() { //Initialize all variable
        historyWalkingBar = getView().findViewById(R.id.historyWalkingBar);
        historyPreviousDay = getView().findViewById(R.id.historyPreviousDay);
        historyNextDay = getView().findViewById(R.id.historyNextDay);
        historyCaloriesWalking = getView().findViewById(R.id.historyCaloriesWalking);
        historyStepsWalking = getView().findViewById(R.id.historyStepsWalking);
        historyDate = getView().findViewById(R.id.historyDate);
        date = new Date();
    }

    private void initValue() { //Initialize all value
        Fitness fitnessToday = new Fitness();
        fitnessToday.setWalk(sharedPreferences.getInt(HomeActivity.MAIN_STEP_COUNTER, 0));
        setFitnessValue(fitnessToday);
    }

    private int convertStepsToCalorie(int steps) { //Convert steps to calories
        return (int) Math.round(steps * 0.4);
    }

    private void getFitness(boolean dateStatus) { //true: next day | false: previous day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, dateStatus ? 1 : -1);
        date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        Fitness fitness = databaseFitness.getFitness(sharedPreferences.getInt(LoginActivity.ID_KEY, 0), format.format(date));
        setFitnessValue(fitness);
    }

    private void setFitnessValue(Fitness fitness) { //Initialize all the fitness value
        SimpleDateFormat format = new SimpleDateFormat("E, MMM dd yyyy");
        historyDate.setText(format.format(date));
        historyStepsWalking.setText(fitness.getWalk() + " steps");
        historyCaloriesWalking.setText(convertStepsToCalorie(fitness.getWalk()) + " cal");
        historyWalkingBar.setProgress((int) Math.round(fitness.getWalk()));
    }

    private void initListener() { //Initialize all listener
        historyPreviousDay.setOnClickListener(this);
        historyNextDay.setOnClickListener(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        databaseFitness = new DatabaseFitness(context);
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View view) { //Select which button is pressed
        switch (view.getId()) {
            case (R.id.historyPreviousDay):
                getFitness(false);
                break;
            case (R.id.historyNextDay):
                getFitness(true);
                break;
        }
    }
}
