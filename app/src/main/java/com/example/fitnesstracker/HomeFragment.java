package com.example.fitnesstracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HomeFragment extends Fragment {
    SharedPreferences sharedPreferences;
    TextView username, lvl;
    ProgressBar barExp;
//    String username, exp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initVariable();
        initUser();
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    private void initVariable() {
        sharedPreferences =  getActivity().getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        username = getView().findViewById(R.id.homeUsername);
        lvl = getView().findViewById(R.id.homeLevel);
        barExp = getView().findViewById(R.id.homeExpBar);
    }
    private void initUser() {
        username.setText(sharedPreferences.getString(LoginActivity.USERNAME_KEY, ""));
        int exp = sharedPreferences.getInt(LoginActivity.EXP_KEY, 0);
        lvl.setText(Math.floorDiv(exp, 1000));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            barExp.setProgress(exp % 1000, true);
        } else{
            barExp.setProgress(exp % 1000);
        }
    }
}