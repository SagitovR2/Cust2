package com.example.cust;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;

public class ActivityInfo extends AppCompatActivity {
    private BottomNavigationView nav;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Bundle arguments = getIntent().getExtras();
        user = new User(arguments.getStringArray("user"));
        nav = (BottomNavigationView) findViewById(R.id.bnav);
        nav.getMenu().findItem(R.id.action_aqua).setTitle("");
        nav.getMenu().findItem(R.id.action_home).setTitle(R.string.home_title);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                nav.clearAnimation();
                Intent intent;
                switch (item.getItemId()) {
                    case (R.id.action_home):
                        nav.getMenu().findItem(R.id.action_info).setTitle("");
                        nav.getMenu().findItem(R.id.action_aqua).setTitle(R.string.aqua_title);
                        nav.getMenu().findItem(R.id.action_home).setTitle("");
                        intent = new Intent(ActivityInfo.this, ActivityHome.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("user", user.convert());
                        startActivity(intent);
                        break;
                    case (R.id.action_aqua):
                        nav.getMenu().findItem(R.id.action_aqua).setTitle(R.string.aqua_title);
                        nav.getMenu().findItem(R.id.action_home).setTitle("");
                        intent = new Intent(ActivityInfo.this, ActivityAqua.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("user", user.convert());
                        startActivity(intent);
                        break;
                    case (R.id.action_info):
                        nav.getMenu().findItem(R.id.action_info).setTitle("Info");
                        nav.getMenu().findItem(R.id.action_home).setTitle("");
                        nav.getMenu().findItem(R.id.action_aqua).setTitle("");
                        break;
                }
                return true;
            }
        });
        nav.setSelectedItemId(R.id.action_info);

    }
}