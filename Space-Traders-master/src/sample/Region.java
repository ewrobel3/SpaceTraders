package sample;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public class Region {

    private String name;

    private String description;

    private int techLevel;

    private Coordinate position;

    private boolean visited;

    private Map<Good, Integer> marketplace;

    private Universe home;

    private Alignment alignment;

    public Region(String name, String description, int techLevel, Coordinate position,
                  Universe home, Alignment alignment) {
        this.name = name;
        this.description = description;
        this.techLevel = techLevel;
        this.position = position;
        this.visited = false;
        this.home = home;
        this.alignment = alignment;
        this.marketplace = initiateMarketplace();
    }

    public Coordinate getPosition() {
        return position;
    }

    public void visit() {
        this.visited = true;
    }

    public boolean isVisited() {
        return visited;
    }

    public String getName() {
        return name;
    }

    public int getTechLevel() {
        return techLevel;
    }

    public String getDescription() {
        return description;
    }

    //Shows buying prices - price change for selling is handled by the sell method
    private Map<Good, Integer> initiateMarketplace() {
        Random noise = new Random();
        Map<Good, Integer> map = Collections.synchronizedMap(
                new EnumMap<Good, Integer>(Good.class));
        for (Good item : Good.values()) {
            if (item.getMinTech() <= techLevel) {
                Integer price = Math.max(item.getBasePrice()
                                - (techLevel - item.getMinTech()) + alignment.getPriceChange()
                                + noise.nextInt(5),
                        5);
                map.put(item, price);
            }
        }
        return map;
    }

    public Map<Good, Integer> getMarketplace() {
        if (marketplace == null) {
            marketplace = initiateMarketplace();
        }
        return marketplace;
    }

    public void addToMarket(Good item, Integer price) {
        this.marketplace.put(item, price);
    }

    public void updateMarketPrices(int change) {
        for (Good item : marketplace.keySet()) {
            marketplace.put(item, marketplace.get(item) + change);
        }
    }

    public int getSellPrice(Good good) {
        if (getMarketplace().get(good) != null) {
            return marketplace.get(good);
        } else {
            return (int) (good.getBasePrice()
                    * Math.pow(1.1, techLevel - good.getMinTech()));
        }
    }
}
