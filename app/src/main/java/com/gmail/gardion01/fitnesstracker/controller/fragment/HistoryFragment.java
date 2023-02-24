package com.gmail.gardion01.fitnesstracker.controller.fragment;

import static com.gmail.gardion01.fitnesstracker.controller.activity.LoginActivity.ID_KEY;
import static com.gmail.gardion01.fitnesstracker.enumeration.FitnessType.RUNNING;
import static com.gmail.gardion01.fitnesstracker.enumeration.FitnessType.WALKING;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private TextView historyCaloriesWalking, historyStepsWalking, historyDate, historyStepsRunning, historyCaloriesRunning;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }
    private void initVariable() { //Initialize all variable
        historyWalkingBar = getView().findViewById(R.id.historyWalkingBar);
        historyPreviousDay = getView().findViewById(R.id.historyPreviousDay);
        historyNextDay = getView().findViewById(R.id.historyNextDay);
        historyCaloriesWalking = getView().findViewById(R.id.historyCaloriesWalking);
        historyStepsWalking = getView().findViewById(R.id.historyStepsWalking);
        historyDate = getView().findViewById(R.id.historyDate);
        historyStepsRunning = getView().findViewById(R.id.historyStepsRunning);
        historyCaloriesRunning = getView().findViewById(R.id.historyCaloriesRunning);
        date = new Date();
    }

    private void initValue() { //Initialize all value
        getFitness(null);
    }

    private int convertStepsToCalorie(int steps) { //Convert steps to calories
        return (int) Math.round(steps * 0.4);
    }

    private void getFitness(Boolean dateStatus) { //true: next day | false: previous day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, dateStatus == null ? 0 : dateStatus ? 1 : -1);
        date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        Fitness fitness = databaseFitness.getFitness(sharedPreferences.getInt(ID_KEY, 0), WALKING.getValue(), format.format(date));
        setFitnessWalkingValue(fitness);
        fitness = databaseFitness.getFitness(sharedPreferences.getInt(ID_KEY, 0), RUNNING.getValue(), format.format(date));
        setFitnessRunningValue(fitness);
    }

    private void setFitnessWalkingValue(Fitness fitness) { //Initialize all the fitness value
        SimpleDateFormat format = new SimpleDateFormat("E, MMM dd yyyy");
        historyDate.setText(format.format(date));
        historyStepsWalking.setText(fitness.getValue() + " steps");
        historyCaloriesWalking.setText(convertStepsToCalorie(fitness.getValue()) + " cal");
        historyWalkingBar.setProgress((int) Math.round(fitness.getValue()));
    }
    private void setFitnessRunningValue(Fitness fitness) { //Initialize all the fitness value
        SimpleDateFormat format = new SimpleDateFormat("E, MMM dd yyyy");
        historyDate.setText(format.format(date));
        historyStepsRunning.setText(fitness.getValue() + " steps");
        historyCaloriesRunning.setText(convertStepsToCalorie(fitness.getValue()) + " cal");
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
