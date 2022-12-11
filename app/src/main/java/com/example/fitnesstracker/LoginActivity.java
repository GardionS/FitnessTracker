package com.example.fitnesstracker;

import static com.example.fitnesstracker.MainScreen.MAIN_DAILY_QUEST;
import static com.example.fitnesstracker.MainScreen.MAIN_DATE_STEP;
import static com.example.fitnesstracker.MainScreen.MAIN_STEP_COUNTER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = LoginActivity.this;

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";
    public static final String USERNAME_KEY = "username_key";
    public static final String EXP_KEY = "exp_key";
    public static final String AGE_KEY = "age_key";
    public static final String WEIGHT_KEY = "weight_key";
    public static final String ID_KEY = "id_key";

    protected SharedPreferences sharedPreferences;
    private String email, password;
    private EditText emailEdit, passwordEdit;
    private DatabaseUser databaseUser;
    private ValidateText validateText;
    private AppCompatButton buttonLogin;
    private AppCompatTextView buttonGoToRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        initObjects();
        initVariable();
        checkLogin();
        initListener();
    }

    private void checkLogin(){
        if(!sharedPreferences.getString(EMAIL_KEY, "").equals("")){
            goToMainScreen();
        }
    }
    private void initObjects() {
        databaseUser = new DatabaseUser(activity);
        validateText = new ValidateText(activity);
    }
    private void verifyDataSQL() {
        if (!validateText.validateEmailEditText(emailEdit, getString(R.string.error_message_email))) {
            return;
        }
        if (!validateText.validateEditText(passwordEdit, getString(R.string.error_message_password))) {
            return;
        }if (databaseUser.checkUser(emailEdit.getText().toString().trim()
                , passwordEdit.getText().toString().trim())) {
            initSharedPreferences(emailEdit.getText().toString().trim());
            goToMainScreen();
        } else {
            Toast.makeText(this, getString(R.string.error_message_login),Toast.LENGTH_SHORT).show();
        }
    }
    private void goToMainScreen(){
        Intent mainScreenIntent = new Intent(activity, MainScreen.class);
        startActivity(mainScreenIntent);
        finish();
    }
    private void initSharedPreferences(String email) {

        User user = databaseUser.getUser(email);
        DatabaseFitness databaseFitness = new DatabaseFitness(this);

        Fitness fitness = databaseFitness.getFitness(user.getId(), new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(MAIN_STEP_COUNTER, fitness.getWalk());
        editor.putString(MAIN_DATE_STEP, fitness.getDate());
        editor.putString(EMAIL_KEY, user.getEmail());
        editor.putString(USERNAME_KEY, user.getUserName());
        editor.putInt(AGE_KEY, user.getAge());
        editor.putInt(WEIGHT_KEY, user.getWeight());
        editor.putInt(EXP_KEY, user.getExp());
        editor.putInt(ID_KEY, user.getId());
        editor.putBoolean(MAIN_DAILY_QUEST, user.getDailyQuest());
        editor.apply();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (email != null && password != null) {
            Intent intent = new Intent(LoginActivity.this, MainScreen.class);
            startActivity(intent);
        }
    }
    @SuppressLint("WrongViewCast")
    private void initVariable() {
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        emailEdit = findViewById(R.id.loginEmail);
        passwordEdit = findViewById(R.id.loginPassword);
        email = sharedPreferences.getString("EMAIL_KEY", null);
        password = sharedPreferences.getString("PASSWORD_KEY", null);
        buttonLogin = (AppCompatButton) findViewById(R.id.loginButton);
        buttonGoToRegister = (AppCompatTextView) findViewById(R.id.goToRegister);
    }

    private void initListener() {
        buttonLogin.setOnClickListener(this);
        buttonGoToRegister.setOnClickListener(this);
    }

    private void emptyEditText() {
        emailEdit.setText(null);
        passwordEdit.setText(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                verifyDataSQL();
                break;
            case R.id.goToRegister:
                goToRegister();
        }
    }
    private void goToRegister() {
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }
}
