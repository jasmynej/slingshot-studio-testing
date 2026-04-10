import unittest
from showroom import Showroom


class TestShowroom(unittest.TestCase):

    def setUp(self):
        self.showroom = Showroom("Test Motors")
        self.showroom.add_car("Toyota", "Camry", 2022, 26000.0)
        self.showroom.add_car("Honda", "Accord", 2021, 28000.0)
        self.showroom.add_car("BMW", "3 Series", 2023, 45000.0)
        self.showroom.add_car("Toyota", "Corolla", 2020, 22000.0)

    # --- Constructor ---

    def test_constructor_sets_name(self):
        s = Showroom("My Showroom")
        self.assertEqual("My Showroom", s.name)

    def test_constructor_throws_on_blank_name(self):
        with self.assertRaises(ValueError):
            Showroom("   ")

    # --- add_car ---

    def test_add_car_increases_inventory_size(self):
        before = self.showroom.total_cars()
        self.showroom.add_car("Ford", "Focus", 2019, 18000.0)
        self.assertEqual(before + 1, self.showroom.total_cars())

    def test_add_car_returns_car_with_unique_id(self):
        car1 = self.showroom.add_car("Ford", "Focus", 2019, 18000.0)
        car2 = self.showroom.add_car("Chevy", "Malibu", 2020, 20000.0)
        self.assertNotEqual(car1.id, car2.id)

    # --- remove_car ---

    def test_remove_car_returns_true_and_reduces_size(self):
        result = self.showroom.remove_car(1)
        self.assertTrue(result)
        self.assertEqual(3, self.showroom.total_cars())

    def test_remove_car_returns_false_for_nonexistent_id(self):
        result = self.showroom.remove_car(999)
        self.assertFalse(result)

    # --- find_by_id ---

    def test_find_by_id_returns_car_when_present(self):
        car = self.showroom.find_by_id(1)
        self.assertIsNotNone(car)
        self.assertEqual(1, car.id)

    def test_find_by_id_returns_none_for_missing_id(self):
        car = self.showroom.find_by_id(999)
        self.assertIsNone(car)

    # --- sell_car ---

    def test_sell_car_marks_car_as_unavailable(self):
        result = self.showroom.sell_car(1)
        self.assertTrue(result)
        self.assertFalse(self.showroom.find_by_id(1).available)

    def test_sell_car_returns_false_for_already_sold_car(self):
        self.showroom.sell_car(1)
        result = self.showroom.sell_car(1)
        self.assertFalse(result)

    def test_sell_car_returns_false_for_nonexistent_car(self):
        result = self.showroom.sell_car(999)
        self.assertFalse(result)

    # --- get_all_cars ---

    def test_get_all_cars_returns_all_cars(self):
        cars = self.showroom.get_all_cars()
        self.assertEqual(4, len(cars))

    # --- get_available_cars ---

    def test_get_available_cars_excludes_sold_cars(self):
        self.showroom.sell_car(2)
        available = self.showroom.get_available_cars()
        ids = [car.id for car in available]
        self.assertNotIn(2, ids)
        self.assertEqual(3, len(available))

    # --- search ---

    def test_search_by_make_returns_matching_cars(self):
        results = self.showroom.search(make="Toyota")
        self.assertEqual(2, len(results))

    def test_search_by_make_and_model_returns_exact_match(self):
        results = self.showroom.search(make="Honda", model="Accord")
        self.assertEqual(1, len(results))
        self.assertEqual("Honda", results[0].make)

    def test_search_is_case_insensitive(self):
        results = self.showroom.search(make="toyota")
        self.assertEqual(2, len(results))

    def test_search_with_none_filters_returns_all(self):
        results = self.showroom.search(make=None, model=None)
        self.assertEqual(4, len(results))

    def test_search_no_match_returns_empty_list(self):
        results = self.showroom.search(make="Lamborghini")
        self.assertEqual(0, len(results))

    # --- filter_by_price ---

    def test_filter_by_price_returns_cars_in_range(self):
        results = self.showroom.filter_by_price(25000, 30000)
        self.assertEqual(2, len(results))

    def test_filter_by_price_inclusive_bounds(self):
        results = self.showroom.filter_by_price(26000, 26000)
        self.assertEqual(1, len(results))

    def test_filter_by_price_throws_when_min_exceeds_max(self):
        with self.assertRaises(ValueError):
            self.showroom.filter_by_price(50000, 10000)

    # --- available_count ---

    def test_available_count_decreases_after_sale(self):
        before = self.showroom.available_count()
        self.showroom.sell_car(1)
        self.assertEqual(before - 1, self.showroom.available_count())


if __name__ == "__main__":
    unittest.main()
