class Car:
    def __init__(self, car_id: int, make: str, model: str, year: int, price: float):
        if not make or not make.strip():
            raise ValueError("Make cannot be blank")
        if not model or not model.strip():
            raise ValueError("Model cannot be blank")
        if year < 1900 or year > 2100:
            raise ValueError("Year must be between 1886 and 2100")
        if price < 0:
            raise ValueError("Price cannot be negative")

        self._id = car_id
        self._make = make
        self._model = model
        self._year = year
        self._price = price
        self._available = True

    @property
    def id(self):
        return self._id

    @property
    def make(self):
        return self._make

    @make.setter
    def make(self, value: str):
        if not value or not value.strip():
            raise ValueError("Make cannot be blank")
        self._make = value

    @property
    def model(self):
        return self._model

    @model.setter
    def model(self, value: str):
        if not value or not value.strip():
            raise ValueError("Model cannot be blank")
        self._model = value

    @property
    def year(self):
        return self._year

    @year.setter
    def year(self, value: int):
        if value < 1900 or value > 2100:
            raise ValueError("Year must be between 1886 and 2100")
        self._year = value

    @property
    def price(self):
        return self._price

    @price.setter
    def price(self, value: float):
        if value < 0:
            raise ValueError("Price cannot be negative")
        self._price = value

    @property
    def available(self):
        return self._available

    @available.setter
    def available(self, value: bool):
        self._available = value

    def __str__(self):
        return (
            f"Car[id={self._id}, make={self._make}, model={self._model}, "
            f"year={self._year}, price={self._price}, available={self._available}]"
        )
