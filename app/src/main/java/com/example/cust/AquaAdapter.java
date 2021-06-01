package com.example.cust;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

class AquaAdapter extends RecyclerView.Adapter<AquaAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<AquaQuery> aquas;
    Context context;

    AquaAdapter(Context context, ArrayList<AquaQuery> fishes) {
        this.aquas = fishes;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public AquaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item_aquas, parent, false);
        return new AquaAdapter.ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ViewOneAquaDraw viewOneAquaDraw;
        TextView aquaNameView, aquaParametersView, aquaFishesView;

        ViewHolder(View view){
            super(view);
            viewOneAquaDraw = view.findViewById(R.id.oneAquaDraw);
            aquaNameView = view.findViewById(R.id.aquaNameView);
            aquaParametersView = view.findViewById(R.id.aquaParametersView);
            aquaFishesView = view.findViewById(R.id.aquaFishesView);
        }
    }

    @Override
    public void onBindViewHolder(AquaAdapter.ViewHolder holder, int position) {
        AquaQuery aqua = aquas.get(position);
        int countOfFishes = 0;
        holder.viewOneAquaDraw.setCountOfFishes();
        String[] fishArray = aqua.getFishes().split(";");
        for (String i: fishArray) {
            if (!i.equals("")) {
                for (String j : i.split(":")[1].split(",")) {
                    countOfFishes += Integer.parseInt(j);
                }
                holder.viewOneAquaDraw.addToCountOfFishes(i.split(":")[0] + ":" + countOfFishes);
                countOfFishes = 0;
            }
        }
        holder.aquaFishesView.setText("");
        holder.aquaNameView.setText(aqua.getName());
        holder.aquaParametersView.setText("ph:" + aqua.getPh() + "\ndh:" + aqua.getDh() + "\ntemp:" + aqua.getTemp() + "\nsize:" + aqua.getSize());
        for (String i: aqua.getFishes().split(";")) {
            if (!holder.aquaFishesView.getText().equals("")) {
                holder.aquaFishesView.setText(holder.aquaFishesView.getText() + "\n" + fishesBeauty(i));
            }
            else {
                holder.aquaFishesView.setText(fishesBeauty(i));
            }
        }
    }

    @Override
    public int getItemCount() {
        return aquas.size();
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
                resp += fish.getName() + ":\n" + "Женского рода: " + females + "\nМужского рода: " + males + "\nМальков: " + frys;
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
                OkHttpClient client = new OkHttpClient();
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
}
