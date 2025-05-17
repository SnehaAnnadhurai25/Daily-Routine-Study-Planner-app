package com.ajt.sss_school;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ajt.sss_school.Model.StatusifInserted;
import com.ajt.sss_school.Utill.ApiClient;
import com.ajt.sss_school.Utill.ApiInterface;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    EditText etName, etEmail, etPhone, etDepartment, etUsername, etPassword;
    Button btnRegister;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etName = findViewById(R.id.etsname);
        etEmail = findViewById(R.id.etEmailId);
        etPhone = findViewById(R.id.etPhoneNum);
        etDepartment = findViewById(R.id.etdept);
        etUsername = findViewById(R.id.ettname);
        etPassword = findViewById(R.id.etpassword);
        btnRegister = findViewById(R.id.btnSubmit);

        dialog = new ProgressDialog(Register.this);
        dialog.setCancelable(true);
        dialog.setMessage("Please wait ...");
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String department = etDepartment.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || department.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();

        }
        else {
            dialog = ProgressDialog.show(Register.this, "Uploading ", "Please wait...", true);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<StatusifInserted> call = apiInterface.insert_user(name,email,phone,department,username,password);
            call.enqueue(new Callback<StatusifInserted>() {
                @Override
                public void onResponse(Call<StatusifInserted> call, Response<StatusifInserted> response) {
                    if (response.isSuccessful()) {
                        dialog.dismiss();
                        StatusifInserted status = response.body();
                        Gson gson = new Gson();
                        String json = gson.toJson(status);
                        Log.e("insert", json);
                        if (status.getResult().getStatus().equals("1")) {
                            Toast.makeText(getApplicationContext(), "Successfully Inserted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Not Inserted", Toast.LENGTH_SHORT).show();
                            etName.setText("");
                            etEmail.setText("");
                            etPhone.setText("");
                            etDepartment.setText("");
                            etUsername.setText("");
                            etPassword.setText("");

                        }
                    }
                }

                @Override
                public void onFailure(Call<StatusifInserted> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("verify err", t.getMessage());
                }
            });

        }
    }

    public void goback(View view) {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }
}