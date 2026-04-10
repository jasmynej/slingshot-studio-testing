package com.showroom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShowroomTest {

    private Showroom showroom;

    @BeforeEach
    void setUp() {
        showroom = new Showroom("Test Motors");
        showroom.addCar("Toyota", "Camry", 2024, 26000);
        showroom.addCar("Honda", "Accord", 2023, 28500);
        showroom.addCar("BMW", "3 Series", 2024, 45000);
        showroom.addCar("Toyota", "Corolla", 2024, 22000);
    }

    // --- Construction ---

    @Test
    void constructor_setsName() {
        assertEquals("Test Motors", showroom.getName());
    }

    @Test
    void constructor_throwsOnBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new Showroom(""));
    }

    // --- addCar ---

    @Test
    void addCar_increasesInventorySize() {
        int before = showroom.totalCars();
        showroom.addCar("Ford", "Mustang", 2023, 35000);
        assertEquals(before + 1, showroom.totalCars());
    }

    @Test
    void addCar_returnsCarWithUniqueId() {
        Car a = showroom.addCar("Ford", "Mustang", 2023, 35000);
        Car b = showroom.addCar("Chevrolet", "Camaro", 2023, 37000);
        assertNotEquals(a.getId(), b.getId());
    }

    // --- removeCar ---

    @Test
    void removeCar_returnsTrueAndReducesSize() {
        int before = showroom.totalCars();
        assertTrue(showroom.removeCar(1));
        assertEquals(before - 1, showroom.totalCars());
    }

    @Test
    void removeCar_returnsFalseForNonexistentId() {
        assertFalse(showroom.removeCar(999));
    }

    // --- findById ---

    @Test
    void findById_returnsCarWhenPresent() {
        assertTrue(showroom.findById(1).isPresent());
        assertEquals("Toyota", showroom.findById(1).get().getMake());
    }

    @Test
    void findById_returnsEmptyForMissingId() {
        assertTrue(showroom.findById(999).isEmpty());
    }

    // --- sellCar ---

    @Test
    void sellCar_marksCarAsUnavailable() {
        assertTrue(showroom.sellCar(1));
        assertFalse(showroom.findById(1).get().isAvailable());
    }

    @Test
    void sellCar_returnsFalseForAlreadySoldCar() {
        showroom.sellCar(1);
        assertFalse(showroom.sellCar(1));
    }

    @Test
    void sellCar_returnsFalseForNonexistentCar() {
        assertFalse(showroom.sellCar(999));
    }

    // --- getAllCars ---

    @Test
    void getAllCars_returnsAllCars() {
        assertEquals(4, showroom.getAllCars().size());
    }

    @Test
    void getAllCars_isUnmodifiable() {
        List<Car> cars = showroom.getAllCars();
        assertThrows(UnsupportedOperationException.class, () -> cars.add(null));
    }

    // --- getAvailableCars ---

    @Test
    void getAvailableCars_excludesSoldCars() {
        showroom.sellCar(1);
        List<Car> available = showroom.getAvailableCars();
        assertEquals(3, available.size());
        assertTrue(available.stream().noneMatch(c -> c.getId() == 1));
    }

    // --- search ---

    @Test
    void search_byMakeReturnsMatchingCars() {
        List<Car> results = showroom.search("Toyota", null);
        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(c -> c.getMake().equals("Toyota")));
    }

    @Test
    void search_byMakeAndModelReturnsExactMatch() {
        List<Car> results = showroom.search("Honda", "Accord");
        assertEquals(1, results.size());
        assertEquals("Accord", results.get(0).getModel());
    }

    @Test
    void search_isCaseInsensitive() {
        List<Car> results = showroom.search("toyota", "camry");
        assertEquals(1, results.size());
    }

    @Test
    void search_withNullFiltersReturnsAll() {
        List<Car> results = showroom.search(null, null);
        assertEquals(4, results.size());
    }

    @Test
    void search_noMatchReturnsEmptyList() {
        List<Car> results = showroom.search("Ferrari", null);
        assertTrue(results.isEmpty());
    }

    // --- filterByPrice ---

    @Test
    void filterByPrice_returnsCarsinRange() {
        List<Car> results = showroom.filterByPrice(25000, 30000);
        assertEquals(2, results.size());
    }

    @Test
    void filterByPrice_inclusiveBounds() {
        List<Car> results = showroom.filterByPrice(26000, 26000);
        assertEquals(1, results.size());
        assertEquals("Camry", results.get(0).getModel());
    }

    @Test
    void filterByPrice_throwsWhenMinExceedsMax() {
        assertThrows(IllegalArgumentException.class, () -> showroom.filterByPrice(50000, 10000));
    }

    // --- counts ---

    @Test
    void availableCount_decreasesAfterSale() {
        int before = showroom.availableCount();
        showroom.sellCar(1);
        assertEquals(before - 1, showroom.availableCount());
    }
}
