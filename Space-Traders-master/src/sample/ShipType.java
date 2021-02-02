package sample;

public enum ShipType {
    BASIC("MyFirstShip", 20, 100, 30);

    public String getName() {
        return name;
    }

    public int getCargoCapacity() {
        return cargoCapacity;
    }

    public int getFuelCapacity() {
        return fuelCapacity;
    }

    public int getHealth() {
        return health;
    }

    private final String name;
    private final int cargoCapacity;
    private final int fuelCapacity;
    private final int health;
    ShipType(String name, int cargoCapacity, int fuelCapacity, int health) {
        this.name = name;
        this.cargoCapacity = cargoCapacity;
        this.fuelCapacity = fuelCapacity;
        this.health = health;
    }
}
