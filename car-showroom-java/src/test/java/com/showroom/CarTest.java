package com.showroom;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarTest {

    @Test
    void constructor_setsFieldsCorrectly() {
        Car car = new Car(1, "Toyota", "Camry", 2024, 26000.0);
        assertEquals(1, car.getId());
        assertEquals("Toyota", car.getMake());
        assertEquals("Camry", car.getModel());
        assertEquals(2024, car.getYear());
        assertEquals(26000.0, car.getPrice());
        assertTrue(car.isAvailable());
    }

    @Test
    void constructor_newCarIsAvailableByDefault() {
        Car car = new Car(1, "Honda", "Civic", 2023, 22000.0);
        assertTrue(car.isAvailable());
    }

    @Test
    void constructor_throwsOnBlankMake() {
        assertThrows(IllegalArgumentException.class,
                () -> new Car(1, " ", "Civic", 2023, 22000.0));
    }

    @Test
    void constructor_throwsOnBlankModel() {
        assertThrows(IllegalArgumentException.class,
                () -> new Car(1, "Honda", "", 2023, 22000.0));
    }

    @Test
    void constructor_throwsOnInvalidYear() {
        assertThrows(IllegalArgumentException.class,
                () -> new Car(1, "Honda", "Civic", 1800, 22000.0));
    }

    @Test
    void constructor_throwsOnNegativePrice() {
        assertThrows(IllegalArgumentException.class,
                () -> new Car(1, "Honda", "Civic", 2023, -1.0));
    }

    @Test
    void setPrice_updatesPrice() {
        Car car = new Car(1, "Toyota", "Camry", 2024, 26000.0);
        car.setPrice(28000.0);
        assertEquals(28000.0, car.getPrice());
    }

    @Test
    void setPrice_throwsOnNegative() {
        Car car = new Car(1, "Toyota", "Camry", 2024, 26000.0);
        assertThrows(IllegalArgumentException.class, () -> car.setPrice(-100.0));
    }

    @Test
    void setAvailable_changesAvailability() {
        Car car = new Car(1, "Toyota", "Camry", 2024, 26000.0);
        car.setAvailable(false);
        assertFalse(car.isAvailable());
    }

    @Test
    void toString_containsKeyFields() {
        Car car = new Car(1, "BMW", "3 Series", 2024, 45000.0);
        String result = car.toString();
        assertTrue(result.contains("BMW"));
        assertTrue(result.contains("3 Series"));
        assertTrue(result.contains("2024"));
    }
}
