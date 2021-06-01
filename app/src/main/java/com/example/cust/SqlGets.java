package com.example.cust;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SqlGets {
    private ArrayList<AquaQuery> aquaArray;
    private ArrayList<Fish> fishArray;


    public SqlGets() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .header("Authorization", "dvaken_aquarium Nekochan123")
                .url("http://dvaken.beget.tech/get_aquas.php")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String body = response.body().string();
            Gson g = new GsonBuilder().registerTypeAdapter(Aquas.class, new AquaDeserializer()).create();
            Aquas aquas = g.fromJson(body, Aquas.class);
            ArrayList<AquaQuery> aquasArray = aquas.getAquas();
            aquaArray = aquasArray;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class AquaDeserializer implements JsonDeserializer<Aquas>
    {
        @Override
        public Aquas deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            ArrayList<AquaQuery> fishes = new ArrayList<>();
            JsonArray jsonArray = json.getAsJsonArray();
            for (JsonElement jsonElement: jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                AquaQuery aqua = new AquaQuery();
                aqua.setId(jsonObject.get("id").getAsInt());
                aqua.setPh(jsonObject.get("ph").getAsString());
                aqua.setDh(jsonObject.get("dh").getAsString());
                aqua.setTemp(jsonObject.get("temp").getAsString());
                aqua.setSize(jsonObject.get("size").getAsString());
                aqua.setName(jsonObject.get("name").getAsString());
                aqua.setFishes(jsonObject.get("fishes").getAsString());
                fishes.add(aqua);
            }
            Aquas aquasArray = new Aquas();
            aquasArray.setAquas(fishes);
            return aquasArray;
        }
    }

    static class ScheduleDeserializer implements JsonDeserializer<Schedule>
    {
        @Override
        public Schedule deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            JsonArray jsonArray = json.getAsJsonArray();
            Schedule schedule = null;
            if (jsonArray.size() != 0) {
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    schedule = new Schedule();
                    schedule.setId(jsonObject.get("id").getAsInt());
                    schedule.setName(jsonObject.get("name").getAsString());
                    schedule.setDescription(jsonObject.get("description").getAsString());
                    schedule.setMonday(jsonObject.get("monday").getAsString());
                    schedule.setTuesday(jsonObject.get("tuesday").getAsString());
                    schedule.setWednesday(jsonObject.get("wednesday").getAsString());
                    schedule.setThursday(jsonObject.get("thursday").getAsString());
                    schedule.setFriday(jsonObject.get("friday").getAsString());
                    schedule.setSaturday(jsonObject.get("saturday").getAsString());
                    schedule.setSunday(jsonObject.get("sunday").getAsString());
                }
            }
            else {
                schedule = new Schedule();
                schedule.setErrorFlag(false);
            }
            return schedule;
        }
    }

    static class UserDeserializer implements JsonDeserializer<User>
    {
        @Override
        public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            JsonArray jsonArray = json.getAsJsonArray();
            User user;
            if (jsonArray.size() != 0) {
                JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
                user = new User();
                user.setId(jsonObject.get("id").getAsInt());
                user.setName(jsonObject.get("name").getAsString());
                user.setEmail(jsonObject.get("email").getAsString());
                user.setLogin(jsonObject.get("login").getAsString());
                user.setPassword(jsonObject.get("password").getAsString());
                user.setAquas(jsonObject.get("aquas").getAsString());
                Aquas aquasArray = new Aquas();
            }
            else {
                user = new User();
                user.setErrorFlag(false);
            }
            return user;
        }
    }

    static class UsersDeserializer implements JsonDeserializer<Users>
    {
        @Override
        public Users deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            ArrayList<User> users = new ArrayList<>();
            JsonArray jsonArray = json.getAsJsonArray();
            User user;
            for (JsonElement jsonElement: jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                user = new User();
                user.setId(jsonObject.get("id").getAsInt());
                user.setName(jsonObject.get("name").getAsString());
                user.setEmail(jsonObject.get("email").getAsString());
                user.setLogin(jsonObject.get("login").getAsString());
                user.setPassword(jsonObject.get("password").getAsString());
                user.setAquas(jsonObject.get("aquas").getAsString());
                users.add(user);
            }
            Users users1 = new Users();
            users1.setUsers(users);
            return users1;
        }
    }

    static class FishDeserializer implements JsonDeserializer<Fishes>
    {
        @Override
        public Fishes deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            ArrayList<Fish> fishes = new ArrayList<>();
            JsonArray jsonArray = json.getAsJsonArray();
            for (JsonElement jsonElement: jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                Fish fish = new Fish();
                fish.setId(jsonObject.get("id").getAsInt());
                fish.setName(jsonObject.get("name").getAsString());
                fish.setDh(jsonObject.get("dh").getAsString());
                fish.setPh(jsonObject.get("ph").getAsString());
                fish.setTemp(jsonObject.get("temp").getAsString());
                fish.setFood(jsonObject.get("food").getAsString());
                fish.setSize(jsonObject.get("size").getAsString());
                fish.setBehaviour(jsonObject.get("behaviour").getAsString());
                fish.setReproduction(jsonObject.get("reproduction").getAsString());
                fish.setOxygen(jsonObject.get("oxygen").getAsString());
                fishes.add(fish);
            }
            Fishes fishesArray = new Fishes();
            fishesArray.setFishes(fishes);
            return fishesArray;
        }
    }

    public ArrayList<Fish> getFishArray() { return this.fishArray; }
}
