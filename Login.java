package com.ajt.sss_school;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ajt.sss_school.Model.login;
import com.ajt.sss_school.Model.slist;
import com.ajt.sss_school.Utill.ApiClient;
import com.ajt.sss_school.Utill.ApiInterface;
import com.ajt.sss_school.Utill.sessionManager;
import com.google.gson.Gson;

import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    EditText email, pwd;
    ProgressDialog loadingBar;
    Button loginBtn;
    DataBaseHandler dataBaseHandler;
    Bundle extras;
    SQLiteDatabase db;
    TextView login_text_haveaccount;
    com.ajt.sss_school.Utill.sessionManager sessionManager;
    String Roll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.login_email);
        pwd = findViewById(R.id.login_password);
        login_text_haveaccount = findViewById(R.id.login_text_haveaccount);
        loginBtn = findViewById(R.id.login_btnlogin);
        dataBaseHandler=new DataBaseHandler(getApplicationContext());
        sessionManager=new sessionManager(getApplicationContext());
        login_text_haveaccount.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Register.class);
            startActivity(intent);
            finish();
        });
        loginBtn.setOnClickListener(v -> {
            String emailInput = email.getText().toString();
            String pwdInput = pwd.getText().toString();

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<login> call=apiInterface.user_login(emailInput,pwdInput);
            call.enqueue(new Callback<login>() {
                @Override
                public void onResponse(Call<login> call, Response<login> response) {
                    if(response.isSuccessful()){
                        login ls=response.body();
                        Gson gson = new Gson();
                        String json = gson.toJson(ls);
                        Log.e("ls", json);
                        Log.e("ls", json);
                        if(ls.getStatus().equalsIgnoreCase("1")){
                            Intent intent = new Intent(Login.this, UserHome.class);
                            sessionManager.setUserSession(ls.getStfId(),ls.getScode(),ls.getSuname(),"parent");
                            startActivity(intent);
                            finish();

                        }
                        else{
                            email.setText("");
                            pwd.setText("");
                            Toast.makeText(getApplicationContext(), "Username or Password is Incorrect",Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<login> call, Throwable t) {

                }
            });
        });


    }

    public boolean checkemail()
    {
        String emailInput = email.getText().toString();
        String EmalFormat = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if(Pattern.compile(EmalFormat).matcher(emailInput).matches())
        {
            return true;
        }
        else {
            return false;
        }

    }
}