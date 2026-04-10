from showroom import Showroom


def main():
    showroom = Showroom("Prestige Motors")

    showroom.add_car("Toyota", "Camry", 2022, 26000)
    showroom.add_car("Honda", "Accord", 2021, 28000)
    showroom.add_car("BMW", "3 Series", 2023, 45000)
    showroom.add_car("Ford", "Mustang", 2020, 35000)
    showroom.add_car("Toyota", "Corolla", 2022, 22000)

    print(f"=== {showroom.name} ===")
    print(f"Total cars: {showroom.total_cars()}")

    showroom.sell_car(2)
    print(f"\nAfter selling car #2 (Honda Accord):")
    print(f"Available cars: {showroom.available_count()}")

    print("\n--- Available Cars ---")
    for car in showroom.get_available_cars():
        print(car)

    print("\n--- Toyota Search ---")
    for car in showroom.search(make="Toyota"):
        print(car)

    print("\n--- Cars $25,000 - $40,000 ---")
    for car in showroom.filter_by_price(25000, 40000):
        print(car)

    showroom.remove_car(4)
    print(f"\nAfter removing car #4 (Ford Mustang):")
    print(f"Total cars: {showroom.total_cars()}")


if __name__ == "__main__":
    main()
