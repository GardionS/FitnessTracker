package com.example.fitnesstracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryFragment extends Fragment {
    private FragmentActivity fragment = getActivity();
    private DatabaseFitness databaseFitness;
    private TextView historyCaloriesWalking, historyStepsWalking, historyDate;
    private SharedPreferences sharedPreferences;

    public HistoryFragment(DatabaseFitness databaseFitness) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVariable();
        initValue();
    }

    private void initVariable() {
//        this.databaseFitness = new DatabaseFitness(fragment);
        historyCaloriesWalking = getView().findViewById(R.id.historyCaloriesWalking);
        historyStepsWalking = getView().findViewById(R.id.historyStepsWalking);
        historyDate = getView().findViewById(R.id.historyDate);
    }
    private void initValue(){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        Fitness fitnessToday = databaseFitness.getFitness(sharedPreferences.getInt(LoginActivity.ID_KEY, 0), format.format(date));
        historyStepsWalking.setText(Integer.toString(fitnessToday.getWalk()));
        historyCaloriesWalking.setText(Integer.toString(convertStepsCalorie(fitnessToday.getWalk())));
        format = new SimpleDateFormat("E MMM dd yyyy");
        historyDate.setText(format.format(date));
    }
    private int convertStepsCalorie(int steps) {
        return (int) Math.round(steps * 0.4);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        databaseFitness = new DatabaseFitness(getActivity());
        sharedPreferences =  getActivity().getApplicationContext().getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
    }
}
