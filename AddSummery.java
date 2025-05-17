package com.ajt.sss_school;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ajt.sss_school.Model.summery;
import com.ajt.sss_school.Utill.DatabaseHelper;

public class AddSummery extends AppCompatActivity {
    private EditText subjectEditText;
    private EditText titleEditText;
    Button viewTask,btnSubmit;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_summery);
        subjectEditText = findViewById(R.id.subject);
        titleEditText = findViewById(R.id.title);
        databaseHelper=new DatabaseHelper(this);
        btnSubmit = findViewById(R.id.btnSubmit);
        viewTask = findViewById(R.id.viewTask);
        viewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), View_Summery.class);
                startActivity(intent);
            }
        });btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });
    }

    private void addTask() {
        String subject = subjectEditText.getText().toString().trim();
        String title = titleEditText.getText().toString().trim();
        if (subject.isEmpty() || title.isEmpty()  ) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        summery sy=new summery();
        sy.setTitle(subject);
        sy.setDesc(title);

        long id = databaseHelper.addSummery(sy);

        if (id != -1) {
            Toast.makeText(this, "Task Summery successfully", Toast.LENGTH_SHORT).show();
            // Schedule reminder after 10 seconds
        } else {
            Toast.makeText(this, "Failed to add Summery", Toast.LENGTH_SHORT).show();
        }

    }

    public void goback(View view) {
        onBackPressed();
    }
}