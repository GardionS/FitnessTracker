package com.example.fitnesstracker;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class ValidateText {
    Context context;
    public ValidateText(Context context){
        this.context = context;
    }
    public boolean validateEditText(EditText editText) {
        String text = editText.getText().toString().trim();
        if(text.isEmpty()) {
            editText.setError("This field cannot be blank");
            return false;
        }
        return true;
    }
    public boolean validateEmailEditText(EditText emailEditText) {
        CharSequence email = emailEditText.getText().toString();
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}