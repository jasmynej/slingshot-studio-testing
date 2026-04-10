package com.showroom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Showroom {

    private final String name;
    private final List<Car> inventory = new ArrayList<>();
    private int nextId = 1;

    public Showroom(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Showroom name cannot be blank");
        this.name = name;
    }

    public String getName() { return name; }

    /** Adds a new car and returns the assigned car (with its generated id). */
    public Car addCar(String make, String model, int year, double price) {
        Car car = new Car(nextId++, make, model, year, price);
        inventory.add(car);
        return car;
    }

    /** Removes a car by id. Returns true if found and removed. */
    public boolean removeCar(int id) {
        return inventory.removeIf(c -> c.getId() == id);
    }

    /** Finds a car by id. */
    public Optional<Car> findById(int id) {
        return inventory.stream().filter(c -> c.getId() == id).findFirst();
    }

    /** Marks a car as sold (unavailable). Returns false if not found or already sold. */
    public boolean sellCar(int id) {
        Optional<Car> car = findById(id);
        if (car.isEmpty() || !car.get().isAvailable()) return false;
        car.get().setAvailable(false);
        return true;
    }

    /** Returns all cars in inventory. */
    public List<Car> getAllCars() {
        return Collections.unmodifiableList(inventory);
    }

    /** Returns only cars that are currently available for sale. */
    public List<Car> getAvailableCars() {
        return inventory.stream().filter(Car::isAvailable).toList();
    }

    /** Case-insensitive search by make and/or model (pass null to skip a filter). */
    public List<Car> search(String make, String model) {
        return inventory.stream()
                .filter(c -> make == null || c.getMake().equalsIgnoreCase(make))
                .filter(c -> model == null || c.getModel().equalsIgnoreCase(model))
                .toList();
    }

    /** Returns cars within an inclusive price range. */
    public List<Car> filterByPrice(double minPrice, double maxPrice) {
        if (minPrice > maxPrice) throw new IllegalArgumentException("minPrice must be <= maxPrice");
        return inventory.stream()
                .filter(c -> c.getPrice() >= minPrice && c.getPrice() <= maxPrice)
                .toList();
    }

    /** Total number of cars in inventory. */
    public int totalCars() { return inventory.size(); }

    /** Number of cars available for sale. */
    public int availableCount() { return (int) inventory.stream().filter(Car::isAvailable).count(); }
}
