package sample;

import java.util.Random;

public enum Good {
    SPACEFRUIT("Mysterious Space Fruit", 60, 0, "Fighter", 1),
    PROSTHETIC("Additional Prosthetic Limb", 100, 4, "Fighter", 3),
    LIGHT("Canned Light", 75, 3, "Pilot", 2),
    WHEEL("Wheel", 25, 1),
    ICD("Interstellar Communication Device", 120, 3, "Merchant", 1),
    OXYGEN("Canned Oxygen", 45, 1),
    LIVER("Duck Livers", 20, 0),
    EMOJIPILLOW("Smiling Emoji Pillow", 35, 1),
    ALARMCLOCK("Alarm Clocks", 40, 1),
    DIRT("Dirt", 10, -1),
    TROUT("Space Trout", 20, 0),
    GPUS("Discount GPUs", 60, 2),
    WRENCH("Magic Wrench", 50, 2, "Engineer", 1),
    WINITEM("Very Mysterious Glowing Orb", 300, 20);


    public String getName() {
        return name;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public int getMinTech() {
        return minTech;
    }

    public boolean isUpgrade() {
        return isUpgrade;
    }

    public String getUpgradeType() {
        return upgradeType;
    }

    public int getUpgradeLevel() {
        return upgradeLevel;
    }

    private final String name;
    private final int basePrice;
    private final int minTech;
    private final boolean isUpgrade;
    private final String upgradeType;
    private final int upgradeLevel;
    Good(String name, int basePrice, int minTech, String upgradeType, int upgradeLevel) {
        this.name = name;
        this.basePrice = basePrice;
        this.minTech = minTech;
        this.isUpgrade = true;
        this.upgradeType = upgradeType;
        this.upgradeLevel = upgradeLevel;
    }

    Good(String name, int basePrice, int minTech) {
        this.name = name;
        this.basePrice = basePrice;
        this.minTech = minTech;
        this.isUpgrade = false;
        this.upgradeType = "";
        this.upgradeLevel = 0;
    }

    public static Good getRandomGood() {
        Random random = new Random();
        return values()[random.nextInt(values().length) - 1];
    }
}