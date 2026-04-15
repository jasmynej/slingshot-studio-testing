from typing import Optional
from car import Car


class Showroom:
    def __init__(self, name: str):
        if not name or not name.strip():
            raise ValueError("Showroom name cannot be blank")
        self._name = name
        self._inventory: list[Car] = []
        self._next_id = 1

    @property
    def name(self):
        return self._name

    def add_car(self, make: str, model: str, year: int, price: float) -> Car:
        car = Car(self._next_id, make, model, year, price)
        self._next_id += 1
        self._inventory.append(car)
        return car

    def remove_car(self, car_id: int) -> bool:
        for car in self._inventory:
            if car.id == car_id:
                self._inventory.remove(car)
                return True
        return False

    def find_by_id(self, car_id: int) -> Optional[Car]:
        for car in self._inventory:
            if car.id == car_id:
                return car
        return None

    def sell_car(self, car_id: int) -> bool:
        car = self.find_by_id(car_id)
        if car is None:
            return False
        car.available = False
        return True

    def get_all_cars(self) -> list[Car]:
        return list(self._inventory)

    def get_available_cars(self) -> list[Car]:
        return [car for car in self._inventory if car.available]

    def search(self, make: Optional[str] = None, model: Optional[str] = None) -> list[Car]:
        result = self._inventory
        if make is not None:
            result = [car for car in result if car.make == make]
        if model is not None:
            result = [car for car in result if car.model == model]
        return result

    def filter_by_price(self, min_price: float, max_price: float) -> list[Car]:
        if min_price > max_price:
            raise ValueError("min_price cannot exceed max_price")
        return [car for car in self._inventory if min_price <= car.price < max_price]

    def total_cars(self) -> int:
        return len(self._inventory)

    def available_count(self) -> int:
        return sum(1 for car in self._inventory if car.available)
