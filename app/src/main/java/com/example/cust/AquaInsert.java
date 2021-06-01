package com.example.cust;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AquaInsert extends AppCompatActivity {
    private int aquaId;

    private EditText editName, editPh, editDh, editTemp, editSize;

    private int fishId;

    private Dialog dialog;

    private OkHttpClient client;

    private AquaQuery aquarium;

    private Button accept;
    private Button addFish;
    private Button back;
    private TextView errorView;

    private RecyclerView aquaFishList;
    private ArrayList<FishesInAqua> fishes;

    FishAdapter adapter;

    private int addId;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aqua_redactor);
        Bundle arguments = getIntent().getExtras();
        user = new User(arguments.getStringArray("user"));
        errorView = findViewById(R.id.errorView);

        SqlGetLastAqua sqlGetLastAqua = new SqlGetLastAqua();
        sqlGetLastAqua.execute();
        try {
            addId = sqlGetLastAqua.get().getId() + 1;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        accept = findViewById(R.id.accept);
        accept.setText("ДОБАВИТЬ");

        OkHttpClient client = new OkHttpClient();

        editName = findViewById(R.id.editName);

        editDh = findViewById(R.id.editDh);

        editPh = findViewById(R.id.editPh);

        editSize = findViewById(R.id.editSize);

        editTemp = findViewById(R.id.editTemp);

        fishes = new ArrayList<>();

        aquaFishList = findViewById(R.id.aquaFishList);
        RecyclerView.LayoutManager recyce = new
                LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        aquaFishList.setLayoutManager(recyce);
        aquaFishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        adapter = new FishAdapter(this, fishes) {
            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteFish(position);
                    }
                });
            }
        };
        aquaFishList.setAdapter(adapter);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (errorCheck()) {
                    user.setAquas(user.getAquas() + ";" + addId);
                    SqlUpdateUser sqlUpdateUser = new SqlUpdateUser();
                    sqlUpdateUser.execute(dataConverterUser());
                    fishes = adapter.getFishesInAqua();
                    SqlInsertAqua sqlUpdateAqua = new SqlInsertAqua();
                    sqlUpdateAqua.execute(dataConverterAqua());
                }
            }
        });

        addFish = findViewById(R.id.addFish);
        addFish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        dialog = new Dialog(this);
        dialog.setTitle("Заголовок");
        dialog.setContentView(R.layout.dialog_layout);
        Button cancelButton = dialog.findViewById(R.id.closeButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        RecyclerView fishAddListView = dialog.findViewById(R.id.fishAddListView);

        ArrayList<FishesInAqua> allFishes = new ArrayList<FishesInAqua>();
        if (allFishes.size() == 0) {
            String[][] convertedFishes = allFishesConverter();
            for (String[] convertedFish: convertedFishes) {
                allFishes.add(new FishesInAqua(Integer.parseInt(convertedFish[0]),
                        convertedFish[1],
                        Integer.parseInt(convertedFish[2]),
                        Integer.parseInt(convertedFish[3]),
                        Integer.parseInt(convertedFish[4])));
            }

        }
        AllFishAdapter allFishesAdapter = new AllFishAdapter(this, allFishes) {
            @Override
            public void onBindViewHolder(FishAdapter.ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                holder.fishName.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        FishesInAqua fish = getFishesAll().get(position);
                        addFish(fish.getId(), fish.getName(), fish.getCountOfFemales(), fish.getCountOfMales(), fish.getCountOfFries());
                        return true;
                    }
                });
            }
        };
        RecyclerView.LayoutManager recyce2 = new
                LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        fishAddListView.setLayoutManager(recyce2);
        fishAddListView.setAdapter(allFishesAdapter);
        Intent intentBack = new Intent(this, ActivityAqua.class);
        intentBack.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        back = findViewById(R.id.backButtonRedactor);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentBack.putExtra("user", user.convert());
                startActivity(intentBack);
            }
        });
    }

    private class SqlTask extends AsyncTask<String, Void, Fish> {

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

    private class SqlUpdateUser extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... voids) {
            ArrayList<AquaQuery> aquasArray;
            Request request = new Request.Builder()
                    .header("Authorization", "dvaken_aquarium Nekochan123")
                    .url("http://dvaken.beget.tech/update_user.php?" + voids[0])
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

    private class SqlGetFishes extends AsyncTask<Void, Void, Fishes> {

        @Override
        protected Fishes doInBackground(Void... voids) {
            ArrayList<Fish> aquasArray;
            Request request = new Request.Builder()
                    .header("Authorization", "dvaken_aquarium Nekochan123")
                    .url("http://dvaken.beget.tech/get_fishes.php")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String body = response.body().string();
                Gson g = new GsonBuilder().registerTypeAdapter(Fishes.class, new SqlGets.FishDeserializer()).create();
                Fishes aquas = g.fromJson(body, Fishes.class);
                return aquas;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    private class SqlInsertAqua extends AsyncTask<String, Void, Void> {

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

    private class SqlGetLastAqua extends AsyncTask<Void, Void, AquaQuery> {

        @Override
        protected AquaQuery doInBackground(Void... voids) {
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
                return aquas.getAquas().get(aquas.getAquas().size() - 1);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
    }

    private String[] fishesConverter(String fishes) {

        String[] end = null;
        if (!fishes.equals("")) {
            int id = Integer.parseInt(fishes.split(":")[0]);
            String[] fishesCount = fishes.split(":")[1].split(",");
            String females = fishesCount[0];
            String males = fishesCount[1];
            String fry = fishesCount[2];

            Fish fish;

            client = new OkHttpClient();


            SqlTask sqlTask = new SqlTask();
            sqlTask.execute(Integer.toString(id));
            try {
                fish = sqlTask.get();
                end = new String[]{Integer.toString(fish.getId()), fish.getName(), females, males, fry};
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return end;
    }

    private String[][] allFishesConverter() {
        String females = Integer.toString(0);
        String males = Integer.toString(0);
        String fry = Integer.toString(0);

        String[][] end = null;

        Fishes fishes1;

        client = new OkHttpClient();



        SqlGetFishes sqlTask = new SqlGetFishes();
        sqlTask.execute();
        try {
            fishes1 = sqlTask.get();
            end = new String[fishes1.getFishes().size()][];
            int i = 0;
            for (Fish fish: fishes1.getFishes()) {
                end[i] = new String[]{Integer.toString(fish.getId()), fish.getName(), females, males, fry};
                i++;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return end;
    }

    private String dataConverterAqua() {
        String resp = "";
        for (FishesInAqua i: fishes) {
            resp += fishDeConverter(i);
        }
        String response = "id='" + addId + "'&name='" + editName.getText()
                + "'&ph='" + editPh.getText() + "'&dh='" + editDh.getText() + "'&temp='" + editTemp.getText() + "'&size='" + editSize.getText() + "'&fishes='" + resp + "'";
        return response;
    }

    private String dataConverterUser() {
        String response = "id='" + user.getId() + "'&name='" + user.getName() + "'&email='" + user.getEmail() + "'&login='" + user.getLogin() + "'&password='" + user.getPassword() + "'&aquas='" + user.getAquas() + "'";
        return response;
    }

    public void addFish(int id, String name, int countOfFemales, int countOfMales, int countOfFries) {
        FishesInAqua fishInAqua = new FishesInAqua(id, name, countOfFemales, countOfMales, countOfFries);
        boolean found = false;
        for (FishesInAqua i: fishes) {
            if (i.getId() == fishInAqua.getId()) {
                found = true;
                i.setCountOfFemales(countOfFemales);
                i.setCountOfMales(countOfMales);
                i.setCountOfFries(countOfFries);
                break;
            }
        }
        if (!found) {
            fishes.add(fishInAqua);
        }
        adapter.notifyDataSetChanged();
    }

    public void deleteFish(int position) {
        fishes.remove(position);
        adapter.notifyDataSetChanged();
    }

    public String fishDeConverter(FishesInAqua fish) {
        String resp = "";
        resp += Integer.toString(fish.getId()) + ":" + fish.getCountOfFemales() + "," + fish.getCountOfMales() + "," + fish.getCountOfFries() + ";";
        return resp;
    }

    private boolean errorCheck() {
        if (editName.getText().toString().length() > 250) {
            errorView.setText("Слишком длинное название(>250)");
            return false;
        }

        if (!editDh.getText().toString().equals("")) {
            try {
                Double.parseDouble(editDh.getText().toString());
            } catch (NumberFormatException e) {
                errorView.setText("Неверный формат dH");
                return false;
            }
        }

        if (!editPh.getText().toString().equals("")) {
            try {
                Double.parseDouble(editPh.getText().toString());
            } catch (NumberFormatException e) {
                errorView.setText("Неверный формат pH");
                return false;
            }
        }

        if (!editTemp.getText().toString().equals("")) {
            try {
                Double.parseDouble(editTemp.getText().toString());
            } catch (NumberFormatException e) {
                errorView.setText("Неверный формат температуры");
                return false;
            }
        }
        if (!editSize.getText().toString().equals("")) {
            String[] s = editSize.getText().toString().split("\\*");
            if (s.length != 3) {
                errorView.setText("Неверный формат размера(x*y*z)");
                return false;
            }
            else {
                try {
                    Double.parseDouble(s[0]);
                    Double.parseDouble(s[1]);
                    Double.parseDouble(s[2]);
                } catch (NumberFormatException e) {
                    errorView.setText("Размер записывается через числа и '*'");
                    return false;
                }
            }
        }
        return true;
    }
}