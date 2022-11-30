package com.example.fitnesstracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = RegisterActivity.this;

    private ValidateText validateText;
    private DatabaseUser databaseHelper;
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

    private void initObjects() {
        databaseHelper = new DatabaseUser(activity);
        validateText = new ValidateText(activity);
        user = new User();
    }

    private void initListener() {
        buttonRegister.setOnClickListener(this);
        buttonGoToLogin.setOnClickListener(this);
    }

    @SuppressLint("WrongViewCast")
    private void initVariable(){
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
                uploadDataToSQL();
                break;
            case R.id.goToLogin:
                // Navigate to Login
                goToLogin();
                break;
        }
    }
    private void uploadDataToSQL(){
        if(!validateText.validateEmailEditText(emailEditText, getString(R.string.error_message_email))
        ||!validateText.validateEditText(usernameEditText, getString(R.string.error_message_username))
        ||!validateText.validateEditText(passwordEditText, getString(R.string.error_message_password))
        ||!validateText.validateEditText(ageEditText, getString(R.string.error_message_age))
        ||!validateText.validateEditText(weightEditText, getString(R.string.error_message_weight))) {
            return;
        }
        if(!databaseHelper.checkUser(emailEditText.getText().toString().trim())) {
            user.setUserName(usernameEditText.getText().toString().trim());
            user.setPassword(passwordEditText.getText().toString().trim());
            user.setEmail(emailEditText.getText().toString().trim());
            databaseHelper.addUser(user);
            emptyEditText();
            goToLogin();//After the user is added then the screen redirect to the login
        } else{

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
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
