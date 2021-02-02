package sample;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public class Ship {

    private String name;
    private int health;
    private int maxHealth;
    private int fuel;
    private int fuelCapacity;
    private Region location;
    private Map<Good, Integer> inventory = Collections.synchronizedMap(new EnumMap<>(Good.class));
    private final int cargoCapacity;
    private int cargoSpace;

    public Ship(Region location, ShipType type) {
        this.name = type.getName();
        this.fuelCapacity = type.getFuelCapacity();
        this.fuel = type.getFuelCapacity();
        this.maxHealth = type.getHealth();
        this.health = type.getHealth();
        this.location = location;
        this.cargoCapacity = type.getCargoCapacity();
        this.cargoSpace = cargoCapacity;
    }

    public boolean canFlyTo(Region destination) {
        int fuelCost = (int) location.getPosition().distance(destination.getPosition());
        return fuelCost <= fuel;
    }

    public void flyTo(Region destination, boolean success) {
        int fuelCost = (int) location.getPosition().distance(destination.getPosition());
        fuel -= fuelCost;
        if (success) {
            this.location = destination;
        }
    }

    public String getName() {
        return name;
    }

    public int getCargoSpace() {
        return cargoSpace;
    }

    public int getCargoCapacity() {
        return cargoCapacity;
    }

    public Map<Good, Integer> getInventory() {
        return inventory;
    }

    public int getFuelCapacity() {
        return fuelCapacity;
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public Region getLocation() {
        return location;
    }

    void addItem(Good item) {
        if (inventory.containsKey(item)) {
            inventory.put(item, inventory.get(item) + 1);
        } else {
            inventory.put(item, 1);
        }
        cargoSpace--;
    }

    boolean removeItem(Good item) {
        if (inventory.containsKey(item)) {
            if (inventory.get(item) == 1) {
                inventory.remove(item);
            } else {
                inventory.put(item, inventory.get(item) - 1);
            }
            cargoSpace++;
            return true;
        }
        return false;
    }

    public void loseAllItems() {
        inventory.clear();
        cargoSpace = cargoCapacity;
    }

    public void loseHealth(int healthLost) {
        if (healthLost > health) {
            health = 0;
        } else {
            health = health - healthLost;
        }
    }

    public Good getRandomInventoryItem() {
        if (inventory.size() == 0) {
            return null;
        }
        Random random = new Random();
        return (Good) inventory.keySet().toArray()[random.nextInt(inventory.keySet().size())];
    }

    public boolean repairSlow() {
        if (health < maxHealth) {
            health++;
            return true;
        }
        return false;
    }

    public void repairFast() {
        health = maxHealth;
    }
}
