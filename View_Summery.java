package com.ajt.sss_school;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.ajt.sss_school.Adapter.TaskAdapter;
import com.ajt.sss_school.Adapter.summeryAdapter;
import com.ajt.sss_school.Model.Task;
import com.ajt.sss_school.Model.summery;
import com.ajt.sss_school.Utill.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class View_Summery extends AppCompatActivity {
    private RecyclerView recyclerView;
    private summeryAdapter adapter;
    private List<summery> taskList;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_summery);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskList = new ArrayList<>();
        adapter = new summeryAdapter(taskList);
        recyclerView.setAdapter(adapter);
        databaseHelper = new DatabaseHelper(this);
        retrieveTasks();
    }

    private void retrieveTasks() {
        taskList.clear();
        List<summery> tasks = databaseHelper.getsummry();

        // Add retrieved tasks to the list
        taskList.addAll(tasks);

        // Notify the adapter that the dataset has changed
        adapter.notifyDataSetChanged();
    }

    public void goback(View view) {
        onBackPressed();
    }
}