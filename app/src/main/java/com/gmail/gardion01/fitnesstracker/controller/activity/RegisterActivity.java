package com.gmail.gardion01.fitnesstracker.controller.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.fitnesstracker.R;
import com.gmail.gardion01.fitnesstracker.database.DatabaseUser;
import com.gmail.gardion01.fitnesstracker.model.User;
import com.gmail.gardion01.fitnesstracker.utility.ValidateText;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = RegisterActivity.this;

    private ValidateText validateText;
    private DatabaseUser databaseUser;
    private EditText usernameEditText, emailEditText, passwordEditText, ageEditText, weightEditText;
    private User user;
    private AppCompatButton buttonRegister;
    private AppCompatTextView buttonGoToLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);
        initObjects();
        initVariable();
        initListener();
    }

    private void initObjects() { //initialize all object
        databaseUser = new DatabaseUser(activity);
        validateText = new ValidateText(activity);
        user = new User();
    }

    private void initListener() { //initialize all listener
        buttonRegister.setOnClickListener(this);
        buttonGoToLogin.setOnClickListener(this);
    }

    @SuppressLint("WrongViewCast")
    private void initVariable() { //Initialize all variable
        usernameEditText = findViewById(R.id.registerUsername);
        emailEditText = findViewById(R.id.registerEmail);
        passwordEditText = findViewById(R.id.registerPassword);
        ageEditText = findViewById(R.id.registerAge);
        weightEditText = findViewById(R.id.registerWeight);
        buttonRegister = (AppCompatButton) findViewById(R.id.registerButton);
        buttonGoToLogin = (AppCompatTextView) findViewById(R.id.goToLogin);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerButton:
                verifyRegisterData(); //Check if input data is correct
                break;
            case R.id.goToLogin:
                goToLogin(); //Navigate to login
                break;
        }
    }

    private void verifyRegisterData() {
        if (validateText.validateEmailEditText(emailEditText, getString(R.string.error_message_email))
                && validateText.validateEditText(usernameEditText, getString(R.string.error_message_username))
                && validateText.validateEditText(passwordEditText, getString(R.string.error_message_password))
                && validateText.validateEditText(ageEditText, getString(R.string.error_message_age))
                && validateText.validateEditText(weightEditText, getString(R.string.error_message_weight))) { //Check all data
            return;
        }
        if (!databaseUser.checkUser(emailEditText.getText().toString().trim())) { //Verify duplicate email
            user.setUserName(usernameEditText.getText().toString().trim());
            user.setPassword(passwordEditText.getText().toString().trim());
            user.setEmail(emailEditText.getText().toString().trim());
            user.setAge(Integer.parseInt(ageEditText.getText().toString()));
            user.setWeight(Integer.parseInt(weightEditText.getText().toString()));
            databaseUser.addUser(user); //Add user data to database
            emptyEditText();
            goToLogin();//After the user is added then the screen redirect to the login
        } else {
            Toast.makeText(this, getString(R.string.error_message_regiter), Toast.LENGTH_SHORT);
        }
    }

    private void emptyEditText() {
        usernameEditText.setText(null);
        emailEditText.setText(null);
        passwordEditText.setText(null);
        ageEditText.setText(null);
        weightEditText.setText(null);
    }

    private void goToLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
