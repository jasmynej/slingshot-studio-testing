package com.showroom;

public class Car {

    private final int id;
    private String make;
    private String model;
    private int year;
    private double price;
    private boolean available;

    public Car(int id, String make, String model, int year, double price) {
        if (make == null || make.isBlank()) throw new IllegalArgumentException("Make cannot be blank");
        if (model == null || model.isBlank()) throw new IllegalArgumentException("Model cannot be blank");
        if (year < 1886 || year > 2100) throw new IllegalArgumentException("Invalid year: " + year);
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");

        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
        this.available = true;
    }

    public int getId() { return id; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getPrice() { return price; }
    public boolean isAvailable() { return available; }

    public void setMake(String make) {
        if (make == null || make.isBlank()) throw new IllegalArgumentException("Make cannot be blank");
        this.make = make;
    }

    public void setModel(String model) {
        if (model == null || model.isBlank()) throw new IllegalArgumentException("Model cannot be blank");
        this.model = model;
    }

    public void setYear(int year) {
        if (year < 1886 || year > 2100) throw new IllegalArgumentException("Invalid year: " + year);
        this.year = year;
    }

    public void setPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("Price cannot be negative");
        this.price = price;
    }

    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return String.format("Car{id=%d, make='%s', model='%s', year=%d, price=%.2f, available=%s}",
                id, make, model, year, price, available);
    }
}
