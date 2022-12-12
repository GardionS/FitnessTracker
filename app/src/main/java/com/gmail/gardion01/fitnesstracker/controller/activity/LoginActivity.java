package com.gmail.gardion01.fitnesstracker.controller.activity;

import static com.gmail.gardion01.fitnesstracker.controller.activity.HomeActivity.MAIN_DAILY_QUEST_TARGET;
import static com.gmail.gardion01.fitnesstracker.controller.activity.HomeActivity.MAIN_DAILY_RUNNING_TARGET;
import static com.gmail.gardion01.fitnesstracker.controller.activity.HomeActivity.MAIN_DAILY_STEP_TARGET;
import static com.gmail.gardion01.fitnesstracker.enumeration.FitnessType.RUNNING;
import static com.gmail.gardion01.fitnesstracker.enumeration.FitnessType.WALKING;
import static com.gmail.gardion01.fitnesstracker.enumeration.QuestType.DAILY_QUEST;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.fitnesstracker.R;
import com.gmail.gardion01.fitnesstracker.database.DatabaseFitness;
import com.gmail.gardion01.fitnesstracker.database.DatabaseFitnessType;
import com.gmail.gardion01.fitnesstracker.database.DatabaseMain;
import com.gmail.gardion01.fitnesstracker.database.DatabaseQuest;
import com.gmail.gardion01.fitnesstracker.database.DatabaseQuestType;
import com.gmail.gardion01.fitnesstracker.database.DatabaseUser;
import com.gmail.gardion01.fitnesstracker.model.Fitness;
import com.gmail.gardion01.fitnesstracker.model.FitnessType;
import com.gmail.gardion01.fitnesstracker.model.Quest;
import com.gmail.gardion01.fitnesstracker.model.QuestType;
import com.gmail.gardion01.fitnesstracker.model.User;
import com.gmail.gardion01.fitnesstracker.utility.ValidateText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";
    public static final String USERNAME_KEY = "username_key";
    public static final String EXP_KEY = "exp_key";
    public static final String AGE_KEY = "age_key";
    public static final String WEIGHT_KEY = "weight_key";
    public static final String ID_KEY = "id_key";
    private final AppCompatActivity activity = LoginActivity.this;
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

    private void checkLogin() {//Auto login since there has been share preference initialized
        if (!sharedPreferences.getString(EMAIL_KEY, "").equals("")) {
            goToMainScreen();
        }
    }

    private void initObjects() { //Initialize all object
        databaseUser = new DatabaseUser(activity);
        validateText = new ValidateText(activity);
    }

    private void verifyLoginInformation() { //Verify all the login information
        if (!validateText.validateEmailEditText(emailEdit, getString(R.string.error_message_email))) {
            return;
        }
        if (!validateText.validateEditText(passwordEdit, getString(R.string.error_message_password))) {
            return;
        }
        if (databaseUser.checkUser(emailEdit.getText().toString().trim()
                , passwordEdit.getText().toString().trim())) {
            initSharedPreferences(emailEdit.getText().toString().trim());
            goToMainScreen();
        } else {
            Toast.makeText(this, getString(R.string.error_message_login), Toast.LENGTH_SHORT).show();
        }
    }

    private void goToMainScreen() {
        Intent mainScreenIntent = new Intent(activity, HomeActivity.class);
        startActivity(mainScreenIntent);
        finish();
    }

    private void initSharedPreferences(String email) { //Initialize all the shared preferences data

        User user = databaseUser.getUser(email);
        DatabaseFitness databaseFitness = new DatabaseFitness(this);

        Fitness fitness = databaseFitness.getFitness(user.getId(), WALKING.getValue(), DatabaseMain.getCurrentDate("dd-MM-yyyy"));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(HomeActivity.MAIN_STEP_COUNTER, fitness.getValue());
        editor.putString(HomeActivity.MAIN_DATE_STEP, fitness.getDate());
        editor.putString(EMAIL_KEY, user.getEmail());
        editor.putString(USERNAME_KEY, user.getUserName());
        editor.putInt(AGE_KEY, user.getAge());
        editor.putInt(WEIGHT_KEY, user.getWeight());
        editor.putInt(EXP_KEY, user.getExp());
        editor.putInt(ID_KEY, user.getId());

        DatabaseQuestType databaseQuestType = new DatabaseQuestType(this);
        QuestType questType = databaseQuestType.getQuestType(DAILY_QUEST.getValue());
        editor.putInt(MAIN_DAILY_QUEST_TARGET, questType.getQuestTargetValue());
        DatabaseQuest databaseQuest = new DatabaseQuest(this);
        Quest quest = databaseQuest.getQuest(user.getId(), DAILY_QUEST.getValue(), DatabaseMain.getCurrentDate("dd-MM-yyyy"));
        editor.putBoolean(HomeActivity.MAIN_DAILY_QUEST, quest.getValue() > questType.getQuestTargetValue());

        DatabaseFitnessType databaseFitnessType = new DatabaseFitnessType(this);
        FitnessType fitnessType = databaseFitnessType.getFitnessType(WALKING.getValue());
        editor.putInt(MAIN_DAILY_STEP_TARGET, fitnessType.getTargetValue());
        fitnessType = databaseFitnessType.getFitnessType(RUNNING.getValue());
        editor.putInt(MAIN_DAILY_RUNNING_TARGET, fitnessType.getTargetValue());

        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (email != null && password != null) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    @SuppressLint("WrongViewCast")
    private void initVariable() { //Initialize all variable
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        emailEdit = findViewById(R.id.loginEmail);
        passwordEdit = findViewById(R.id.loginPassword);
        email = sharedPreferences.getString("EMAIL_KEY", null);
        password = sharedPreferences.getString("PASSWORD_KEY", null);
        buttonLogin = findViewById(R.id.loginButton);
        buttonGoToRegister = findViewById(R.id.goToRegister);
    }

    private void initListener() { //Initialize all listener
        buttonLogin.setOnClickListener(this);
        buttonGoToRegister.setOnClickListener(this);
    }

    private void emptyEditText() {
        emailEdit.setText(null);
        passwordEdit.setText(null);
    }

    @Override
    public void onClick(View view) { //check which button was clicked
        switch (view.getId()) {
            case R.id.loginButton:
                verifyLoginInformation();
                break;
            case R.id.goToRegister:
                goToRegister();
                emptyEditText();
        }
    }

    private void goToRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
