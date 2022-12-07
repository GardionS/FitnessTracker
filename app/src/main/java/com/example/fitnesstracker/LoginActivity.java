package com.example.fitnesstracker;

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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = LoginActivity.this;

    public static final String SHARED_PREFS = "shared_prefs_user";
    public static final String EMAIL_KEY = "email_key";
//    public static final String PASSWORD_KEY = "password_key";
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
        initListener();
//        initButton();
//        initGoToRegister();

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
            User user = databaseUser.getUser(emailEdit.getText().toString().trim());
            Intent accountsIntent = new Intent(activity, MainScreen.class);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(EMAIL_KEY, user.getEmail());
            editor.putString(USERNAME_KEY, user.getUserName());
            editor.putInt(AGE_KEY, user.getAge());
            editor.putInt(WEIGHT_KEY, user.getWeight());
            editor.putInt(EXP_KEY, user.getExp());
            editor.putInt(ID_KEY, user.getId());

            editor.apply();
//            accountsIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
//            emptyEditText();

            startActivity(accountsIntent);
        } else {
            // Snack Bar to show success message that record is wrong
//            Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }
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

        emailEdit.setText("asd@gmail.com");
        passwordEdit.setText("asdasd");
    }

    private void initListener() {
        buttonLogin.setOnClickListener(this);
        buttonGoToRegister.setOnClickListener(this);
    }
    private void initButton(){

        Button login = findViewById(R.id.loginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(emailEdit.getText().toString()) && TextUtils.isEmpty(passwordEdit.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "Please enter Email and Password", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(EMAIL_KEY, emailEdit.getText().toString());
//                    editor.putString(PASSWORD_KEY, passwordEdit.getText().toString());
//                    editor.putString(USERNAME_KEY, )
                    editor.apply();
//                    Intent intent = new Intent(LoginActivity.this, MainScreen.class);
//                    startActivity(intent);
                    Intent intent = new Intent(LoginActivity.this, ForegroundService.class);
                    startActivity(intent);

                }
            }
        });
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
                // Navigate to RegisterActivity
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
        }
    }
}
