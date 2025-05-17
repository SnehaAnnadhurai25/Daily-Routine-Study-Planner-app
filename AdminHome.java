package com.ajt.sss_school;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ajt.sss_school.Utill.sessionManager;

public class AdminHome extends AppCompatActivity {
    RelativeLayout tvStudent_enrolments,tvStaff_enrolments,tvAnnouncements;
    com.ajt.sss_school.Utill.sessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        tvStudent_enrolments= findViewById(R.id.tvStudent_enrolments);
        tvStaff_enrolments= findViewById(R.id.tvStaff_enrolments);
        tvAnnouncements= findViewById(R.id.tvAnnouncements);
        sessionManager=new sessionManager(getApplicationContext());
        tvAnnouncements.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Announcements.class);
            startActivity(intent);

        });  tvStudent_enrolments.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), StudentEntroll.class);
            startActivity(intent);

        });tvStaff_enrolments.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), StaffEntrolment.class);
            startActivity(intent);

        });
   }

    public void goback(View view) {
        Intent intent = new Intent(AdminHome.this, SelectUser.class);
        sessionManager.setUserSessionLogout();
        startActivity(intent);
    }
}