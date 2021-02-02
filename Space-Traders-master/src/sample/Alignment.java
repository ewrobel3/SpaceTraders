package sample;

import java.util.Random;

public enum Alignment {
    FRIENDLY(0),
    HARMLESS(5),
    NEUTRAL(10),
    SALTY(15),
    AGGRESSIVE(20);

    public int getPriceChange() {
        return priceChange;
    }

    private final int priceChange;
    Alignment(int priceChange) {
        this.priceChange = priceChange;
    }

    public static Alignment getRandomAlignment() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
