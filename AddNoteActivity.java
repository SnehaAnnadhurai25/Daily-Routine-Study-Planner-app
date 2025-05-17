package com.ajt.sss_school;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ajt.sss_school.Adapter.NoteAdapter;
import com.ajt.sss_school.Model.Note;
import com.ajt.sss_school.Utill.DatabaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class AddNoteActivity extends AppCompatActivity {
   ImageView imageView;
    private static final int REQUEST_FILE_PICKER = 1;

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private List<Note> noteList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_note);
        imageView=findViewById(R.id.imageView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteList = new ArrayList<>();
        adapter = new NoteAdapter(noteList, this);
        recyclerView.setAdapter(adapter);
        databaseHelper = new DatabaseHelper(this);
        retrieveNotes();
        imageView.setOnClickListener(v -> {
           // launchFilePicker();
            BottomSheetDialog dialog = new BottomSheetDialog(AddNoteActivity.this);
            dialog.setContentView(R.layout.dialog_lead_source);
            AppCompatButton yes = dialog.findViewById(R.id.btnSave);
            AppCompatButton no = dialog.findViewById(R.id.btnCancel);
            AppCompatEditText etStatus = dialog.findViewById(R.id.ettitle);
            AppCompatEditText etlink = dialog.findViewById(R.id.etlink);
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            yes.setOnClickListener(v1 -> {
                if (etStatus.getText().toString().isEmpty()) {
                    Toast.makeText(AddNoteActivity.this, "Please Enter Title", Toast.LENGTH_SHORT).show();
                }
               else if (etlink.getText().toString().isEmpty()) {
                    Toast.makeText(AddNoteActivity.this, "Please Enter Link", Toast.LENGTH_SHORT).show();
                }
               else{
                    Note note = new Note();
                    note.setTitle(etStatus.getText().toString());
                    note.setFilePath(etlink.getText().toString());
                    long id = databaseHelper.addNote(note);

                    if (id != -1) {
                        Toast.makeText(this, "Note added successfully!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(this, "Failed to add note!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
        });

    }

    private void retrieveNotes() {
        noteList.clear();

        // Retrieve notes from the database
        List<Note> notes = databaseHelper.getAllNotes();

        // Add the retrieved notes to the list
        noteList.addAll(notes);

        // Notify the adapter that the dataset has changed
        adapter.notifyDataSetChanged();
    }

    private void launchFilePicker() {
        String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";
            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }
            intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
        }
        startActivityForResult(intent, REQUEST_FILE_PICKER);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_FILE_PICKER && resultCode == RESULT_OK && data != null) {
            // Get the selected file path
            String filePath = data.getData().getPath(); // Assuming the file path is obtained like this

            // Extract file name from the file path
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

            // Create a Note object
            Note note = new Note();
            note.setTitle(fileName);
            note.setFilePath(filePath);

            // Add the note to the database
            long id = databaseHelper.addNote(note);

            if (id != -1) {
                Toast.makeText(this, "Note added successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add note!", Toast.LENGTH_SHORT).show();
            }

            // Finish the activity
            finish();
        }
    }
    public void goback(View view) {
        onBackPressed();
    }
}