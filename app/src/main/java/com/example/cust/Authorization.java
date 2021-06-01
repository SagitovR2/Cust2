package com.example.cust;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mysql.cj.x.protobuf.Mysqlx;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Authorization extends AppCompatActivity {
    Button authorize;
    EditText nameEdit, passwordEdit;
    OkHttpClient client;
    User user = null;
    TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        client = new OkHttpClient();

        nameEdit = findViewById(R.id.loginEdit);
        passwordEdit = findViewById(R.id.passwordEdit);

        resultView = findViewById(R.id.resultView);

        authorize = findViewById(R.id.authorizeButton);
        authorize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SqlGetUser sqlGetUser = new SqlGetUser();
                sqlGetUser.execute(nameEdit.getText().toString());
                try {
                    user = sqlGetUser.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (user != null && user.isErrorFlag()) {
                    if (user.getPassword().equals(passwordEdit.getText().toString())) {
                        Intent authorized = new Intent(Authorization.this, ActivityHome.class);
                        authorized.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        authorized.putExtra("user", user.convert());
                        startActivity(authorized);
                    }
                    else {
                        resultView.setText("Неверный пароль");
                    }
                }
                else {
                    resultView.setText("Неверный логин");
                }
            }
        });
    }

    private class SqlGetUser extends AsyncTask<String, Void, User> {

        @Override
        protected User doInBackground(String... voids) {
            Request request = new Request.Builder()
                    .header("Authorization", "dvaken_aquarium Nekochan123")
                    .url("http://dvaken.beget.tech/get_user.php?login='" + voids[0] + "'")
                    .build();
            System.out.println("http://dvaken.beget.tech/get_user.php?login='" + voids[0] + "'");
            try {
                client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                String body = response.body().string();
                Gson g = new GsonBuilder().registerTypeAdapter(User.class, new SqlGets.UserDeserializer()).create();
                System.out.println(body + voids[0]);
                User user = g.fromJson(body, User.class);
                return user;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
    }
}