package com.ajt.sss_school;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.ajt.sss_school.Utill.sessionManager;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
   sessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager=new sessionManager(getApplicationContext());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Thread timer= new Thread()
        {
            public void run()
            {
                try
                {
                    //Display for 3 seconds
                    sleep(4000);
                }
                catch (InterruptedException e)
                {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                finally
                {
                    checkPermissions();


                }
            }
        };
        timer.start();
    }
    private void checkPermissions() {
        String[] permissions = new String[0];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            permissions = new String[]{

                    android.Manifest.permission.READ_MEDIA_IMAGES,
                    android.Manifest.permission.READ_MEDIA_AUDIO,
                    android.Manifest.permission.READ_MEDIA_VIDEO
            };
        }
        else {
            permissions = new String[]{

                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
            };
        }
        Permissions.check(this, permissions, null, null, new PermissionHandler() {
            public void onGranted() {
                checkUpdate();
            }
            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                Toast.makeText(getApplicationContext(), "All Permission Must Need.", Toast.LENGTH_SHORT);
                finish();
            }
        });
    }
    private void checkUpdate() {


        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }
}