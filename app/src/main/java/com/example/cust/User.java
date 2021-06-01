package com.example.cust;

import android.content.Intent;

public class User {
    private int id;
    private String name, email, login, password, aquas;
    private boolean errorFlag = true;

    public User() {

    }

    public User(String[] us) {
        id = Integer.parseInt(us[0]);
        name = us[1];
        email = us[2];
        login = us[3];
        password = us[4];
        aquas = us[5];
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAquas() {
        return aquas;
    }

    public void setAquas(String aquas) {
        this.aquas = aquas;
    }

    public String[] convert() {
        return new String[]{
                Integer.toString(id), name, email, login, password, aquas
        };
    }
}
