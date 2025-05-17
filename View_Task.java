package com.ajt.sss_school;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ajt.sss_school.Adapter.TaskAdapter;
import com.ajt.sss_school.Model.Task;
import com.ajt.sss_school.Utill.DatabaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class View_Task extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private List<Task> taskList;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskList = new ArrayList<>();
        adapter = new TaskAdapter(taskList,getApplicationContext());
        recyclerView.setAdapter(adapter);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);
        retrieveTasks();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("cate_Event"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String taskId = intent.getStringExtra("id");
            String title = intent.getStringExtra("title");

            Log.e("taskId",taskId);
            Log.e("title",title);
            if (title.equals("delete")) {
                // Delete the task with the corresponding ID from the database
                int id = Integer.parseInt(taskId);
                deleteTask(id);

                // Reload RecyclerView after deleting the task
                retrieveTasks();
            }
            else if (title.equals("complete")) {
                // Update the status of the task with the corresponding ID to "complete"
                int id = Integer.parseInt(taskId);
                databaseHelper.updateTaskStatus(id, "2");

                // Reload RecyclerView after updating the task status
                retrieveTasks();
            }
            else if (title.equals("update")) {

                 update_task(taskId);
            }

        }
    };

    private void update_task(String taskId) {
        BottomSheetDialog dialog = new BottomSheetDialog(View_Task.this);
        dialog.setContentView(R.layout.dialog_update_task);
        AppCompatButton yes = dialog.findViewById(R.id.btnSave);
        AppCompatButton no = dialog.findViewById(R.id.btnCancel);
        AppCompatEditText subject = dialog.findViewById(R.id.subject);
        AppCompatEditText title = dialog.findViewById(R.id.title);
        AppCompatEditText event = dialog.findViewById(R.id.event);
        TextView dat = dialog.findViewById(R.id.etlink);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newSubject = subject.getText().toString().trim();
                String newTitle = title.getText().toString().trim();
                String newEvent = event.getText().toString().trim();
                String newDatetime = dat.getText().toString().trim();

                Task updatedTask = new Task();
                updatedTask.setId(Integer.parseInt(taskId));
                updatedTask.setSubject(newSubject);
                updatedTask.setTitle(newTitle);
                updatedTask.setEvent(newEvent);
                updatedTask.setDateTime(newDatetime);
                databaseHelper.updateTask(updatedTask);
                retrieveTasks();
                 dialog.dismiss();
            }
        });
        dialog.show();

        Task task = databaseHelper.getTask(Integer.parseInt(taskId));
        if (task != null) {
            String sub = task.getSubject();
            String tit = task.getTitle();
            String datetime = task.getDateTime();
            String events = task.getEvent();
            String status = task.getStatus();
            subject.setText(sub);
            title.setText(tit);
            event.setText(events);
            dat.setText(datetime);
        }
        dat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(View_Task.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Update the calendar with the selected date
                                calendar.set(year, month, dayOfMonth);

                                // Create and show a time picker dialog
                                TimePickerDialog timePickerDialog = new TimePickerDialog(View_Task.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                // Update the calendar with the selected time
                                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                                calendar.set(Calendar.MINUTE, minute);

                                                // Format the selected date and time and set it to datetimeEditText
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                                dat.setText(sdf.format(calendar.getTime()));
                                            }
                                        }, hourOfDay, minute, false);

                                timePickerDialog.show();
                            }
                        }, year, month, dayOfMonth);

                datePickerDialog.show();
            }
        });
    }


    private void retrieveTasks() {
        taskList.clear(); // Clear the list before adding items

        // Get all tasks from the database
        List<Task> tasks = databaseHelper.getAllTasks();

        // Add retrieved tasks to the list
        taskList.addAll(tasks);

        // Notify the adapter that the dataset has changed
        adapter.notifyDataSetChanged();
    }
    private void deleteTask(int taskId) {
        // Delete the task with the given ID from the database
        databaseHelper.deleteTask(taskId);
    }
    public void goback(View view) {
        onBackPressed();
    }
}