package com.example.cust;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ActivityAqua extends AppCompatActivity {
    private ViewAquasDraw draw;
    private BottomNavigationView nav;
    Aquas sqlGets;

    private int page_number = 1;
    private int max_page_number;
    ArrayList<AquaQuery> aquaArray;
    //private int max_page_number = 1;

    private Button previous;
    private Button next;
    private ImageButton addAquaButton;
    private OkHttpClient client;
    private boolean secondAquaActive = false;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aqua);

        Bundle arguments = getIntent().getExtras();
        user = new User(arguments.getStringArray("user"));
        for (String i: user.convert()) {
            System.out.println(i + "222222222222222222222222222222");
        }

        Intent intentToAquasPage = new Intent(this, ActivityAquasPage.class);
        intentToAquasPage.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        addAquaButton = findViewById(R.id.addButton);
        Intent addAquaIntent = new Intent(this, AquaInsert.class);
        addAquaIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        addAquaIntent.putExtra("user", user.convert());
        addAquaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(addAquaIntent);
            }
        });


        draw = (ViewAquasDraw)findViewById(R.id.demo);
        draw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getY() < v.getHeight() / 2) {
                    intentToAquasPage.putExtra("id", aquaArray.get(page_number * 2 - 2).getId());
                    intentToAquasPage.putExtra("user", user.convert());
                    startActivity(intentToAquasPage);
                } else {
                    if (secondAquaActive) {
                        intentToAquasPage.putExtra("id", aquaArray.get(page_number * 2 - 1).getId());
                        intentToAquasPage.putExtra("user", user.convert());
                        startActivity(intentToAquasPage);
                    }
                }

                return true;
            }
        });
        sqlGets = new Aquas();
        for (String i: user.getAquas().split(";")) {
            SqlGetAqua sqlTask = new SqlGetAqua();
            sqlTask.execute(i);
            try {
                sqlGets.addAqua(sqlTask.get());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        countOfFishes();



        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        next = (Button) findViewById(R.id.next);

        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        /*OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .header("Authorization", "dvaken_aquarium Nekochan123")
                .url("http://dvaken.beget.tech/aqua.php")
                .build();
        System.out.println("ВЫВОДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДДД");
        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string() + "++++++++++++++++++++++++++++");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        previous = (Button) findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page_number--;
                if (page_number - 1 > 0) {
                    previous.setText(Integer.toString(page_number - 1));
                } else {
                    previous.setText("");
                    previous.setEnabled(false);
                }
                next.setText(Integer.toString(page_number + 1));
                next.setEnabled(true);
                countOfFishes();
                draw.listing();
            }
        });
        if (page_number == max_page_number) {
            next.setEnabled(false);
            next.setText("");
        }
        if (page_number == 1) {
            previous.setEnabled(false);
            previous.setText("");
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page_number++;
                if (page_number < max_page_number) {
                    if (!next.isEnabled()) next.setEnabled(true);
                    next.setText(Integer.toString(page_number + 1));
                } else {
                    next.setText("");
                    next.setEnabled(false);
                }
                previous.setEnabled(true);
                previous.setText(Integer.toString(page_number - 1));
                countOfFishes();
                draw.listing();
            }
        });



        nav = (BottomNavigationView) findViewById(R.id.bnav);
        nav.clearAnimation();
        nav.getMenu().findItem(R.id.action_aqua).setTitle("");
        nav.getMenu().findItem(R.id.action_home).setTitle(R.string.home_title);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case (R.id.action_home):
                        nav.getMenu().findItem(R.id.action_aqua).setTitle("");
                        nav.getMenu().findItem(R.id.action_info).setTitle("");
                        nav.getMenu().findItem(R.id.action_home).setTitle(R.string.home_title);
                        intent = new Intent(ActivityAqua.this, ActivityHome.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("user", user.convert());
                        startActivity(intent);
                        break;
                    case (R.id.action_aqua):
                        nav.getMenu().findItem(R.id.action_aqua).setTitle(R.string.aqua_title);
                        nav.getMenu().findItem(R.id.action_info).setTitle("");
                        nav.getMenu().findItem(R.id.action_home).setTitle("");
                        break;
                    case (R.id.action_info):
                        nav.getMenu().findItem(R.id.action_aqua).setTitle("");
                        nav.getMenu().findItem(R.id.action_home).setTitle("");
                        nav.getMenu().findItem(R.id.action_info).setTitle("Info");
                        intent = new Intent(ActivityAqua.this, ActivityInfo.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("user", user.convert());
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
        nav.setSelectedItemId(R.id.action_aqua);


    }

    private void countOfFishes() {
        aquaArray = sqlGets.getAquas();
        int countOfFishes = 0;
        String[] fishArray = aquaArray.get(page_number * 2 - 2).getFishes().split(";");
        draw.setCountOfFishes1();
        draw.setCountOfFishes2();
        if (fishArray.length != 0) {
            for (String i : fishArray) {
                if (!i.equals("")) {
                    for (String j : i.split(":")[1].split(",")) {
                        countOfFishes += Integer.parseInt(j);
                    }
                }
                draw.addToCountOfFishes1(i.split(":")[0] + ":" + countOfFishes);
                countOfFishes = 0;
            }
        }
        if (aquaArray.size() >= page_number * 2) {
            countOfFishes = 0;
            fishArray = aquaArray.get(page_number * 2 - 1).getFishes().split(";");

            if (fishArray.length != 0) {
                for (String i : fishArray) {
                    if (!i.equals("")) {
                        for (String j : i.split(":")[1].split(",")) {
                            countOfFishes += Integer.parseInt(j);
                        }
                    }
                    draw.addToCountOfFishes2(i.split(":")[0] + ":" + countOfFishes);
                    countOfFishes = 0;
                }
            }
            draw.aquaName2 = aquaArray.get(page_number * 2 - 1).getName();
            secondAquaActive = true;
        }
        else {
            countOfFishes = 0;
            draw.addToCountOfFishes2("1:0");
            draw.aquaName2 = "";
            secondAquaActive = false;
        }
        draw.aquaName1 = aquaArray.get(page_number * 2 - 2).getName();
        if (aquaArray.size() % 2 == 0) {
            max_page_number = aquaArray.size() / 2;
        }
        else {
            max_page_number = aquaArray.size() / 2 + 1;
        }
    }

    private class SqlTask extends AsyncTask<Void, Void, SqlGets> {

        @Override
        protected SqlGets doInBackground(Void... voids) {
            SqlGets sqlGets = new SqlGets();
            return sqlGets;
        }
    }

    private class SqlGetAqua extends AsyncTask<String, Void, AquaQuery> {

        @Override
        protected AquaQuery doInBackground(String... voids) {
            ArrayList<AquaQuery> aquasArray;
            Request request = new Request.Builder()
                    .header("Authorization", "dvaken_aquarium Nekochan123")
                    .url("http://dvaken.beget.tech/get_aqua.php?id=" + voids[0])
                    .build();

            try {
                client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                String body = response.body().string();
                Gson g = new GsonBuilder().registerTypeAdapter(Aquas.class, new SqlGets.AquaDeserializer()).create();
                Aquas aquas = g.fromJson(body, Aquas.class);
                aquasArray = aquas.getAquas();
                return aquasArray.get(0);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    private class SqlUpdateAqua extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... voids) {
            ArrayList<AquaQuery> aquasArray;
            Request request = new Request.Builder()
                    .header("Authorization", "dvaken_aquarium Nekochan123")
                    .url("http://dvaken.beget.tech/add_aqua.php?" + voids[0])
                    .build();
            try {
                client = new OkHttpClient();
                Response response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class SqlGetAquas extends AsyncTask<Void, Void, Aquas> {

        @Override
        protected Aquas doInBackground(Void... voids) {
            Request request = new Request.Builder()
                    .header("Authorization", "dvaken_aquarium Nekochan123")
                    .url("http://dvaken.beget.tech/get_aquas.php")
                    .build();

            try {
                client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                String body = response.body().string();
                Gson g = new GsonBuilder().registerTypeAdapter(Aquas.class, new SqlGets.AquaDeserializer()).create();
                Aquas aquas = g.fromJson(body, Aquas.class);
                return aquas;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
    }
}