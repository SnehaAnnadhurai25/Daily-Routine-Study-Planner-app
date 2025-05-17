package com.ajt.sss_school;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ajt.sss_school.Model.Task;
import com.ajt.sss_school.Utill.DatabaseHelper;
import com.ajt.sss_school.Utill.ReminderScheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Todo extends AppCompatActivity {
    private EditText subjectEditText;
    private EditText titleEditText;
    private TextView datetimeEditText;
    private EditText eventEditText;
    Button viewTask,btnSubmit;
    // SQLite Database Helper
    private ReminderScheduler reminderScheduler;

    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        subjectEditText = findViewById(R.id.subject);
        titleEditText = findViewById(R.id.title);
        datetimeEditText = findViewById(R.id.datetime);
        eventEditText = findViewById(R.id.event);
        btnSubmit = findViewById(R.id.btnSubmit);
        viewTask = findViewById(R.id.viewTask);
        databaseHelper = new DatabaseHelper(this);
        reminderScheduler = new ReminderScheduler(databaseHelper);
        datetimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        }); viewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), View_Task.class);
                startActivity(intent);
            }
        });btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });
    }

    public void goback(View view) {
        onBackPressed();
    }
    public void addTask() {
        String subject = subjectEditText.getText().toString().trim();
        String title = titleEditText.getText().toString().trim();
        String datetime = datetimeEditText.getText().toString().trim();
        String event = eventEditText.getText().toString().trim();

        // Validate input fields
        if (subject.isEmpty() || title.isEmpty() || datetime.isEmpty() || event.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a Task object
        Task task = new Task();
        task.setSubject(subject);
        task.setTitle(title);
        task.setDateTime(datetime);
        task.setEvent(event);
        task.setStatus("1");

        // Add task to the database
        long id = databaseHelper.addTask(task);

        if (id != -1) {
            Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show();
            reminderScheduler.scheduleReminder(this, System.currentTimeMillis() + 10000, (int) id); // Schedule reminder after 10 seconds
        } else {
            Toast.makeText(this, "Failed to add task", Toast.LENGTH_SHORT).show();
        }
    }
    private void showDateTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create and show a date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update the calendar with the selected date
                        calendar.set(year, month, dayOfMonth);

                        // Create and show a time picker dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(Todo.this,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        // Update the calendar with the selected time
                                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        calendar.set(Calendar.MINUTE, minute);

                                        // Format the selected date and time and set it to datetimeEditText
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                        datetimeEditText.setText(sdf.format(calendar.getTime()));
                                    }
                                }, hourOfDay, minute, false);

                        timePickerDialog.show();
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }


}