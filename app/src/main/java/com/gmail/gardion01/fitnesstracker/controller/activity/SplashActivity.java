package com.gmail.gardion01.fitnesstracker.controller.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.fitnesstracker.R;
import com.gmail.gardion01.fitnesstracker.database.DatabaseMain;


public class SplashActivity extends AppCompatActivity {
    private Handler handler;
    private int ACTIVITY_PERMISSION = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        checkPermissionActivity();
    }
    private void checkPermissionActivity()  { //Check the activity permission
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACTIVITY_RECOGNITION) !=
                PackageManager.PERMISSION_GRANTED) { //Check whether the permission granted or not
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACTIVITY_RECOGNITION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission Needed")
                        .setMessage("Permission Needed")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() { //Show user the OK to request permission
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, ACTIVITY_PERMISSION); //App request permission
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() { //Show user the Cancel button to exit
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create().show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, ACTIVITY_PERMISSION); //App request permission
            }
        }  else { //Run the app if the app already has permission

            handler = new Handler();
            handler.postDelayed(new Runnable() { //Use splash activity for a better UX
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { //Check permission result and if accepted, continue
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTIVITY_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT);
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                    }
                }, 2000);
            } else {
                Toast.makeText(this, " Permission Denied", Toast.LENGTH_SHORT);
                finish();
                System.exit(0);
            }
        }
    }
}
