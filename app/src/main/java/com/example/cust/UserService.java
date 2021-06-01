package com.example.cust;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Response;
import retrofit.http.GET;

public interface UserService {
    @GET("/aqua")
    Call<AquaQuery> getAquas();
}

class AquaQuery {
    int id;
    String name;
    String dh;
    String ph;
    String temp;
    String size;
    String fishes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDh() {return dh; }

    public void setDh(String dh) { this.dh = dh; }

    public String getPh() { return ph; }

    public void setPh(String ph) { this.ph = ph; }

    public String getTemp() { return temp; }

    public void setTemp(String temp) { this.temp = temp; }

    public String getSize() { return size; }

    public void setSize(String size) { this.size = size; }

    public String getFishes() { return fishes; }

    public void setFishes(String fishes) { this.fishes = fishes; }
}

class Aquas {
    ArrayList<AquaQuery> aquas = new ArrayList<>();

    public ArrayList<AquaQuery> getAquas() {
        return aquas;
    }

    public void setAquas(ArrayList<AquaQuery> aquas) {
        this.aquas = aquas;
    }

    public void addAqua(AquaQuery aquaQuery) {
        this.aquas.add(aquaQuery);
    }
}

class Schedule {
    private int id;
    private String name;
    private String description;
    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;
    private String saturday;
    private String sunday;
    private boolean errorFlag = true;

    public boolean isErrorFlag() {
        return errorFlag;
    }

    public void setErrorFlag(boolean errorFlag) {
        this.errorFlag = errorFlag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public String getFriday() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }

    public String getSaturday() {
        return saturday;
    }

    public void setSaturday(String saturday) {
        this.saturday = saturday;
    }

    public String getSunday() {
        return sunday;
    }

    public void setSunday(String sunday) {
        this.sunday = sunday;
    }
}

class Users {
    ArrayList<User> users = new ArrayList<>();

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        users.add(user);
    }
}

class Fish {
    private int id;
    private String name;
    private String dh;
    private String ph;
    private String temp;
    private String food;
    private String lifeCycle;
    private String size;
    private String behaviour;
    private String reproduction;
    private String oxygen;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDh() {
        return dh;
    }

    public void setDh(String dh) {
        this.dh = dh;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getLifeCycle() {
        return lifeCycle;
    }

    public void setLifeCycle(String lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBehaviour() {
        return behaviour;
    }

    public void setBehaviour(String behaviour) {
        this.behaviour = behaviour;
    }

    public String getReproduction() {
        return reproduction;
    }

    public void setReproduction(String reproduction) {
        this.reproduction = reproduction;
    }

    public String getOxygen() {
        return oxygen;
    }

    public void setOxygen(String oxygen) {
        this.oxygen = oxygen;
    }
}

class Fishes {
    private ArrayList<Fish> fishes;

    public ArrayList<Fish> getFishes() {
        return fishes;
    }

    public void setFishes(ArrayList<Fish> fishes) {
        this.fishes = fishes;
    }
}
