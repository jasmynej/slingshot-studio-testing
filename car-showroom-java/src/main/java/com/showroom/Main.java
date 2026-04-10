package com.showroom;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Showroom showroom = new Showroom("Prestige Motors");

        System.out.println("=== " + showroom.getName() + " ===\n");

        // Add cars to inventory
        showroom.addCar("Toyota", "Camry", 2024, 26_000);
        showroom.addCar("Honda", "Accord", 2023, 28_500);
        showroom.addCar("BMW", "3 Series", 2024, 45_000);
        showroom.addCar("Ford", "Mustang", 2023, 35_000);
        showroom.addCar("Toyota", "Corolla", 2024, 22_000);

        System.out.println("--- Full Inventory (" + showroom.totalCars() + " cars) ---");
        showroom.getAllCars().forEach(System.out::println);

        // Sell a car
        System.out.println("\nSelling car with id=2...");
        boolean sold = showroom.sellCar(2);
        System.out.println("Sale successful: " + sold);

        // Available cars
        System.out.println("\n--- Available Cars (" + showroom.availableCount() + ") ---");
        showroom.getAvailableCars().forEach(System.out::println);

        // Search by make
        System.out.println("\n--- Search: make='Toyota' ---");
        List<Car> toyotas = showroom.search("Toyota", null);
        toyotas.forEach(System.out::println);

        // Filter by price range
        System.out.println("\n--- Cars between $25,000 and $40,000 ---");
        showroom.filterByPrice(25_000, 40_000).forEach(System.out::println);

        // Remove a car
        System.out.println("\nRemoving car with id=4...");
        showroom.removeCar(4);
        System.out.println("Inventory size: " + showroom.totalCars());
    }
}
