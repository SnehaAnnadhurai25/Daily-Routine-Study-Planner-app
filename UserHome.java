package com.ajt.sss_school;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ajt.sss_school.Model.Task;
import com.ajt.sss_school.Utill.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserHome extends AppCompatActivity {
    DataBaseHandler dataBaseHandler;
    private final Handler handler = new Handler();
    private static final long CHECK_INTERVAL = 60 * 500; // 1 minute
    private static final String CHANNEL_ID = "todo_task";
    private static final int NOTIFICATION_ID = 1;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        Button todoButton = findViewById(R.id.todo);
        Button notesButton = findViewById(R.id.notes);
        Button summaryButton = findViewById(R.id.summery);
        Button studyTechniqueButton = findViewById(R.id.studytechnique);
        dataBaseHandler=new DataBaseHandler(this);
        textToSpeech = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.US); // Set the language for speech
            }
        });
        todoButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Todo.class);
            startActivity(intent);
        }); notesButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
            startActivity(intent);
        });summaryButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddSummery.class);
            startActivity(intent);
        });studyTechniqueButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), StudyTechnique.class);
            startActivity(intent);
        });
        handler.post(taskCheckerRunnable);
    }

    public void goback(View view) {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }

    private final Runnable taskCheckerRunnable = new Runnable() {
        @Override
        public void run() {
            checkTasksAndNotify();
            handler.postDelayed(this, CHECK_INTERVAL); // Schedule next execution
        }
    };

    @SuppressLint("Range")
    private void checkTasksAndNotify() {
        String currentTime = getCurrentTime();

        // Query the database to get tasks whose datetime matches the current time
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_TODO + " WHERE " + DatabaseHelper.COLUMN_DATETIME + " = ?", new String[]{currentTime});

        // If any tasks found, notify the user using Log (replace with your notification mechanism)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
                task.setSubject(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_SUBJECT)));
                task.setTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE)));
                task.setDateTime(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATETIME)));
                task.setEvent(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EVENT)));

                // Notify the user about the task (for demonstration, using Log)
                Log.d("TaskNotification", "Task Found: " + task.getTitle());
                String taskTitle = "Title is "+task.getTitle()+" Subject is "+task.getSubject()+" Event is "+task.getEvent();
                createNotification(getApplicationContext(), task.getTitle(), task.getSubject(), task.getEvent());
                speakTask(taskTitle);
            } while (cursor.moveToNext());

            cursor.close();
        }
        db.close();
    }

    private void createNotification(Context context, String title, String subject, String event) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Task Reminders", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity
                    (context, 0, intent, PendingIntent.FLAG_MUTABLE);
        }
        else
        {
            pendingIntent = PendingIntent.getActivity
                    (context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT  );
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle(title)
                .setContentText(subject + ": " + event)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @SuppressLint("SimpleDateFormat")
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date());
    }
    private void speakTask(String taskTitle) {
        if (textToSpeech != null) {
            textToSpeech.speak(taskTitle, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }
}