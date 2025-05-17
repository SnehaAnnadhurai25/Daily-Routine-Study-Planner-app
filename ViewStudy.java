package com.ajt.sss_school;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.ajt.sss_school.Adapter.VideoAdapter;
import com.ajt.sss_school.Adapter.summeryAdapter;
import com.ajt.sss_school.Model.studyTech;
import com.ajt.sss_school.Model.summery;
import com.ajt.sss_school.Utill.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewStudy extends AppCompatActivity {
    private RecyclerView recyclerView;
    private VideoAdapter adapter;
    private List<studyTech> taskList;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_study);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskList = new ArrayList<>();
        adapter = new VideoAdapter(taskList);
        recyclerView.setAdapter(adapter);
        databaseHelper = new DatabaseHelper(this);
        retrieveTasks();
    }

    private void retrieveTasks() {
        taskList.clear();
        List<studyTech> tasks = databaseHelper.getStudy();
        taskList.addAll(tasks);

        // Notify the adapter that the dataset has changed
        adapter.notifyDataSetChanged();
    }

}