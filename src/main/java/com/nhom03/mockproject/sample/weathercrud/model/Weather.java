package com.nhom03.mockproject.sample.weathercrud.model;

public class Weather {
    private int id;
    private String city;
    private double temp;

    public Weather(int id, String city, double temp) {
        this.id = id;
        this.city = city;
        this.temp = temp;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public double getTemp() { return temp; }
    public void setTemp(double temp) { this.temp = temp; }
}