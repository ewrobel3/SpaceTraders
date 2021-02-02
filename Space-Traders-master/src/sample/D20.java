package sample;

import java.util.Random;

public class D20 {

    private static Random d20 = new Random();
    public static boolean skillCheck(int difficultyClass, int modifier) {
        int roll = d20.nextInt(20) + modifier;
        return roll > difficultyClass;
    }

    public static int roll() {
        return d20.nextInt(20);
    }
}
