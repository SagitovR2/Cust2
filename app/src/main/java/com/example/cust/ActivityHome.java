package com.example.cust;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.os.SystemClock.*;

public class ActivityHome extends AppCompatActivity {
    private  NotificationPublisher alarm;

    private BottomNavigationView nav;

    private RecyclerView aquasList;

    private Users users;

    private ArrayList<AquaQuery> aquas;

    private User user;

    private AquaAdapter adapter;

    private OkHttpClient client;

    // Идентификатор уведомления
    private static final int NOTIFY_ID = 101;

    // Идентификатор канала
    private static String CHANNEL_ID = "Cat channel";

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleNotification(getNotification("5 second delay"), 5000);
            }
        });*/

        alarm = new NotificationPublisher();


        Bundle arguments = getIntent().getExtras();
        user = new User(arguments.getStringArray("user"));

        aquasList = findViewById(R.id.aquasList);
        RecyclerView.LayoutManager recyce = new
                LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        aquasList.setLayoutManager(recyce);

        SqlGetUsers task = new SqlGetUsers();
        task.execute();
        try {
            users = task.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        aquas = new ArrayList<>();
        for (User i: users.getUsers()) {
            String[] aquasUser = i.getAquas().split(";");
            for (String j: aquasUser) {
                SqlGetAqua sqlGetAqua = new SqlGetAqua();
                sqlGetAqua.execute(j);
                System.out.println(j + "/////////////////////////////////");
                try {
                    aquas.add(sqlGetAqua.get());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        adapter = new AquaAdapter(this, aquas);
        aquasList.setAdapter(adapter);

        nav = (BottomNavigationView) findViewById(R.id.bnav);
        nav.getMenu().findItem(R.id.action_aqua).setTitle("");
        nav.getMenu().findItem(R.id.action_home).setTitle(R.string.home_title);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case (R.id.action_home):
                        nav.getMenu().findItem(R.id.action_aqua).setTitle("");
                        nav.getMenu().findItem(R.id.action_home).setTitle(R.string.home_title);
                        nav.getMenu().findItem(R.id.action_info).setTitle("");
                        break;
                    case (R.id.action_aqua):
                        nav.getMenu().findItem(R.id.action_aqua).setTitle(R.string.aqua_title);
                        nav.getMenu().findItem(R.id.action_home).setTitle("");
                        nav.getMenu().findItem(R.id.action_info).setTitle("");
                        intent = new Intent(ActivityHome.this, ActivityAqua.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("user", user.convert());
                        startActivity(intent);
                        break;
                    case (R.id.action_info):
                        nav.getMenu().findItem(R.id.action_aqua).setTitle("");
                        nav.getMenu().findItem(R.id.action_info).setTitle("Info");
                        nav.getMenu().findItem(R.id.action_home).setTitle("");
                        intent = new Intent(ActivityHome.this, ActivityInfo.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("user", user.convert());
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

        nav.setSelectedItemId(R.id.action_home);

        ConstraintLayout.LayoutParams lpView = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        ConstraintLayout.LayoutParams linLayoutParam = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);

    }

    private void scheduleNotification(Notification notification, int delay) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_baseline_edit_24);
        return builder.build();
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

    private class SqlGetUsers extends AsyncTask<Void, Void, Users> {

        @Override
        protected Users doInBackground(Void... voids) {
            Request request = new Request.Builder()
                    .header("Authorization", "dvaken_aquarium Nekochan123")
                    .url("http://dvaken.beget.tech/get_users.php")
                    .build();
            try {
                client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                String body = response.body().string();
                Gson g = new GsonBuilder().registerTypeAdapter(Users.class, new SqlGets.UsersDeserializer()).create();
                System.out.println(body);
                Users users = g.fromJson(body, Users.class);
                return users;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

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
    public static String createNotificationChannel(Context context) {

        // NotificationChannels are required for Notifications on O (API 26) and above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // The id of the channel.
            String channelId = "Channel_id";

            // The user-visible name of the channel.
            CharSequence channelName = "Application_name";
            // The user-visible description of the channel.
            String channelDescription = "Application_name Alert";
            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            boolean channelEnableVibrate = true;
            //            int channelLockscreenVisibility = Notification.;

            // Initializes NotificationChannel.
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            notificationChannel.setDescription(channelDescription);
            notificationChannel.enableVibration(channelEnableVibrate);
            //            notificationChannel.setLockscreenVisibility(channelLockscreenVisibility);

            // Adds NotificationChannel to system. Attempting to create an existing notification
            // channel with its original values performs no operation, so it's safe to perform the
            // below sequence.
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);

            return channelId;
        } else {
            // Returns null for pre-O (26) devices.
            return null;
        }
    }
}