import unittest
from car import Car


class TestCar(unittest.TestCase):

    def test_constructor_sets_fields_correctly(self):
        car = Car(1, "Toyota", "Camry", 2022, 26000.0)
        self.assertEqual(1, car.id)
        self.assertEqual("Toyota", car.make)
        self.assertEqual("Camry", car.model)
        self.assertEqual(2022, car.year)
        self.assertEqual(26000.0, car.price)

    def test_constructor_new_car_is_available_by_default(self):
        car = Car(1, "Toyota", "Camry", 2022, 26000.0)
        self.assertTrue(car.available)

    def test_constructor_throws_on_blank_make(self):
        with self.assertRaises(ValueError):
            Car(1, "  ", "Camry", 2022, 26000.0)

    def test_constructor_throws_on_blank_model(self):
        with self.assertRaises(ValueError):
            Car(1, "Toyota", "", 2022, 26000.0)

    def test_constructor_throws_on_invalid_year(self):
        with self.assertRaises(ValueError):
            Car(1, "Toyota", "Camry", 1800, 26000.0)

    def test_constructor_throws_on_negative_price(self):
        with self.assertRaises(ValueError):
            Car(1, "Toyota", "Camry", 2022, -1.0)

    def test_set_price_updates_price(self):
        car = Car(1, "Toyota", "Camry", 2022, 26000.0)
        car.price = 30000.0
        self.assertEqual(30000.0, car.price)

    def test_set_price_throws_on_negative(self):
        car = Car(1, "Toyota", "Camry", 2022, 26000.0)
        with self.assertRaises(ValueError):
            car.price = -500.0

    def test_set_available_changes_availability(self):
        car = Car(1, "Toyota", "Camry", 2022, 26000.0)
        car.available = False
        self.assertFalse(car.available)
        car.available = True
        self.assertTrue(car.available)

    def test_str_contains_key_fields(self):
        car = Car(1, "Toyota", "Camry", 2022, 26000.0)
        result = str(car)
        self.assertIn("Toyota", result)
        self.assertIn("Camry", result)
        self.assertIn("2022", result)


if __name__ == "__main__":
    unittest.main()
