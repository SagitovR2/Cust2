package com.example.cust;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ActivityAquasPage extends AppCompatActivity {

    private ViewOneAquaDraw aqua;
    private Button backButton;

    private ImageButton redact;

    private TextView name, parametres, size, fishes;
    private int id;

    private OkHttpClient client;

    private AquaQuery aquarium;

    public int countOfFishes;

    private User user;

    private Schedule schedule;
    private int schedule_id;

    private TextView aquaSchedule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aquas_page);

        Bundle arguments = getIntent().getExtras();
        user = new User(arguments.getStringArray("user"));
        id = arguments.getInt("id");

        client = new OkHttpClient();

        SqlTask sqlTask = new SqlTask();
        sqlTask.execute();
        try {
            aquarium = sqlTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        aqua = (ViewOneAquaDraw) findViewById(R.id.oneAquaDraw);
        countOfFishes = 0;
        aqua.setCountOfFishes();
        String[] fishArray = aquarium.getFishes().split(";");
        for (String i: fishArray) {
            if (!i.equals("")) {
                for (String j : i.split(":")[1].split(",")) {
                    countOfFishes += Integer.parseInt(j);
                }
            }
            aqua.addToCountOfFishes(i.split(":")[0] + ":" + countOfFishes);
            countOfFishes = 0;
        }

        redact = (ImageButton) findViewById(R.id.redact);
        Intent redactIntent = new Intent(this, AquaRedactor.class);
        redactIntent.putExtra("user", user.convert());
        redactIntent.putExtra("id", aquarium.getId());
        redact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redactIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(redactIntent);
            }
        });


        Intent back = new Intent(this, ActivityAqua.class);
        back.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        back.putExtra("user", user.convert());
        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(back);
            }
        });

        name = (TextView) findViewById(R.id.name);
        parametres = (TextView) findViewById(R.id.parametres);
        size = (TextView) findViewById(R.id.size);
        fishes = (TextView) findViewById(R.id.fishes);

        name.setText(aquarium.getName());
        parametres.setText("Параметры воды:" + aquarium.getDh() + " " + aquarium.getPh() + " " + aquarium.temp);
        size.setText("Размер: " + aquarium.getSize());
        String[] aquaFishes = aquarium.getFishes().split(";");
        for (String i: aquaFishes) {
            if (!fishes.getText().equals("")) {
                fishes.setText(fishes.getText() + "\n" + fishesBeauty(i));
            }
            else {
                fishes.setText(fishesBeauty(i));
            }
        }
        SqlGetAquasSchedule sqlGetAquasSchedule = new SqlGetAquasSchedule();
        sqlGetAquasSchedule.execute();
        try {
            schedule_id = sqlGetAquasSchedule.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SqlGetSchedule sqlGetSchedule = new SqlGetSchedule();
        sqlGetSchedule.execute();
        aquaSchedule = findViewById(R.id.aquaSchedule);
        try {
            schedule = sqlGetSchedule.get();
            aquaSchedule.setText("Понедельник: " + schedule.getMonday() + "\n" +
                    "Вторник: " + schedule.getTuesday() + "\n" +
                    "Среда: " + schedule.getWednesday() + "\n" +
                    "Четверг: " + schedule.getThursday() + "\n" +
                    "Пятница: " + schedule.getFriday() + "\n" +
                    "Суббота: " + schedule.getSaturday() + "\n" +
                    "Воскресенье: " + schedule.getSunday());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private String fishesBeauty(String fishes) {
        String resp = "";
        Fish fish;

        String[] fishesSplitted = fishes.split(":");

        if (fishesSplitted[0].length() != 0) {
            int fishId = Integer.parseInt(fishesSplitted[0]);


            String[] fishesCountSplitted = fishesSplitted[1].split(",");
            int females = Integer.parseInt(fishesCountSplitted[0]);
            int males = Integer.parseInt(fishesCountSplitted[1]);
            int frys = Integer.parseInt(fishesCountSplitted[2]);


            SqlGetFish sqlTask = new SqlGetFish();
            sqlTask.execute(Integer.toString(fishId));
            try {
                fish = sqlTask.get();
                if ((Double.parseDouble(fish.getDh()) >= Double.parseDouble(aquarium.getDh()) - 2 && Double.parseDouble(fish.getDh()) <= Double.parseDouble(aquarium.getDh()) + 2) &&
                        (Double.parseDouble(fish.getPh()) >= Double.parseDouble(aquarium.getPh()) - 2 && Double.parseDouble(fish.getPh()) <= Double.parseDouble(aquarium.getPh()) + 2) &&
                        (Double.parseDouble(fish.getTemp()) >= Double.parseDouble(aquarium.getTemp()) - 4 && Double.parseDouble(fish.getTemp()) <= Double.parseDouble(aquarium.getTemp()) + 4)) {
                    resp += fish.getName() + ":\n" + "Женского рода: " + females + "\nМужского рода: " + males + "\nМальков: " + frys;
                }
                else {
                    resp += fish.getName() + " - " + "неподходящие условия" + ":\n" + "Женского рода: " + females + "\nМужского рода: " + males + "\nМальков: " + frys;
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return resp;
    }

    private class SqlGetFish extends AsyncTask<String, Void, Fish> {

        @Override
        protected Fish doInBackground(String... voids) {
            ArrayList<Fish> aquasArray;
            Request request = new Request.Builder()
                    .header("Authorization", "dvaken_aquarium Nekochan123")
                    .url("http://dvaken.beget.tech/get_fish.php?id=" + voids[0])
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String body = response.body().string();
                Gson g = new GsonBuilder().registerTypeAdapter(Fishes.class, new SqlGets.FishDeserializer()).create();
                Fishes aquas = g.fromJson(body, Fishes.class);
                aquasArray = aquas.getFishes();
                return aquasArray.get(0);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    private class SqlTask extends AsyncTask<Void, Void, AquaQuery> {

        @Override
        protected AquaQuery doInBackground(Void... voids) {
            Request request = new Request.Builder()
                    .header("Authorization", "dvaken_aquarium Nekochan123")
                    .url("http://dvaken.beget.tech/get_aqua.php?id=" + id)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String body = response.body().string();
                Gson g = new GsonBuilder().registerTypeAdapter(Aquas.class, new SqlGets.AquaDeserializer()).create();
                Aquas aquas = g.fromJson(body, Aquas.class);
                ArrayList<AquaQuery> aquasArray = aquas.getAquas();
                aquarium = aquasArray.get(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return aquarium;
        }
    }

    private class SqlGetSchedule extends AsyncTask<Void, Void, Schedule> {

        @Override
        protected Schedule doInBackground(Void... voids) {
            Schedule schedule = null;
            Request request = new Request.Builder()
                    .header("Authorization", "dvaken_aquarium Nekochan123")
                    .url("http://dvaken.beget.tech/get_schedule.php?id=" + schedule_id)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String body = response.body().string();
                System.out.println(body + ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
                Gson g = new GsonBuilder().registerTypeAdapter(Schedule.class, new SqlGets.ScheduleDeserializer()).create();
                schedule = g.fromJson(body, Schedule.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return schedule;
        }
    }

    private class SqlGetAquasSchedule extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            Request request = new Request.Builder()
                    .header("Authorization", "dvaken_aquarium Nekochan123")
                    .url("http://dvaken.beget.tech/get_aquas_schedule.php?id=" + aquarium.getId())
                    .build();
            int sched = 0;
            try {
                Response response = client.newCall(request).execute();
                String body = response.body().string();
                sched = Integer.parseInt(body.split(":")[1].substring(1, body.split(":")[1].length() - 3));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sched;
        }
    }
}
