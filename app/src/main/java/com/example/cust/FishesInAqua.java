package com.example.cust;

public class FishesInAqua {
    private int id;
    private String name;
    private int countOfMales;
    private int countOfFemales;
    private int countOfFries;

    public FishesInAqua(int id, String name, int countOfFemales, int countOfMales, int countOfFries) {
        this.id = id;
        this.name = name;
        this.countOfMales = countOfMales;
        this.countOfFemales = countOfFemales;
        this.countOfFries = countOfFries;
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

    public int getCountOfMales() {
        return countOfMales;
    }

    public void setCountOfMales(int countOfMales) {
        this.countOfMales = countOfMales;
    }

    public int getCountOfFemales() {
        return countOfFemales;
    }

    public void setCountOfFemales(int countOfFemales) {
        this.countOfFemales = countOfFemales;
    }

    public int getCountOfFries() {
        return countOfFries;
    }

    public void setCountOfFries(int countOfFries) {
        this.countOfFries = countOfFries;
    }
}
