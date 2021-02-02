package sample;

import java.util.ArrayList;

class Player {

    private Mode mode;
    private int credits;
    private int pilotPoints;
    private int fighterPoints;
    private int merchantPoints;
    private int engineerPoints;
    private Region location;
    private Ship currentShip;
    private ArrayList<Good> equipment = new ArrayList<>();
    private final int maxEquipment = 5;
    private Universe universe;

    Player(Mode mode, Integer[] points, Region location, Universe universe) {
        this.mode = mode;
        this.pilotPoints = points[0];
        this.fighterPoints = points[1];
        this.merchantPoints = points[2];
        this.engineerPoints = points[3];
        this.location = location;
        location.visit();
        this.currentShip = new Ship(location, ShipType.BASIC);
        if (this.mode == Mode.HARD) {
            this.credits = 400;
        } else if (this.mode == Mode.MEDIUM) {
            this.credits = 600;
        } else {
            this.credits = 800;
        }
        this.universe = universe;
    }

    Encounter tryToTravel(Region destination) {
        boolean cargoEmpty = currentShip.getCargoSpace() == currentShip.getCargoCapacity();
        int roll = D20.roll();
        if (!cargoEmpty && roll > mode.getCutoffs()[0]) {
            return new PoliceEncounter(this);
        } else if (roll > (cargoEmpty ? mode.getCutoffs()[0] : mode.getCutoffs()[1])) {
            return new BanditEncounter(this);
        } else if (roll > (cargoEmpty ? mode.getCutoffs()[1] : mode.getCutoffs()[2])) {
            return new TraderEncounter(this);
        } else {
            return null;
        }
    }

    void travelTo(Region destination, boolean success) {
        currentShip.flyTo(destination, success);
        if (success) {
            destination.visit();
            this.location = destination;
        }
    }

    public Region getLocation() {
        return location;
    }

    public boolean canTravelTo(Region region) {
        return region != location && currentShip.canFlyTo(region);
    }

    public int getMerchantPoints() {
        return merchantPoints;
    }

    public ArrayList<Good> getEquipment() {
        return equipment;
    }

    boolean buy(Good item) {
        int price = location.getMarketplace().get(item);
        if (currentShip.getCargoSpace() > 0 && credits >= price) {
            credits = credits - price;
            currentShip.addItem(item);
            if (item.equals(Good.WINITEM)) {
                universe.win();
            }
            return true;
        }
        return false;
    }

    boolean sell(Good item) {
        int sellPrice = location.getSellPrice(item) + merchantPoints - 10;
        if (currentShip.getInventory().containsKey(item)) {
            credits = credits + sellPrice;
            currentShip.removeItem(item);
            return true;
        }
        return false;
    }

    boolean equip(Good item) {
        if (currentShip.getInventory().containsKey(item) && item.isUpgrade()
                && equipment.size() < maxEquipment) {
            currentShip.removeItem(item);
            equipment.add(item);
            if (item.getUpgradeType().equals("Pilot")) {
                pilotPoints += item.getUpgradeLevel();
            } else if (item.getUpgradeType().equals("Fighter")) {
                fighterPoints += item.getUpgradeLevel();
            } else if (item.getUpgradeType().equals("Merchant")) {
                merchantPoints += item.getUpgradeLevel();
                for (Region region : this.universe.getRegions()) {
                    region.updateMarketPrices(-item.getUpgradeLevel());
                }
            } else if (item.getUpgradeType().equals("Engineer")) {
                engineerPoints += item.getUpgradeLevel();
            } else {
                return false;
            }
            return true;
        }
        return false;
    }

    boolean unequip(Good item) {
        if (equipment.contains(item) && currentShip.getCargoSpace() > 0) {
            equipment.remove(item);
            currentShip.addItem(item);
            if (item.getUpgradeType().equals("Pilot")) {
                pilotPoints -= item.getUpgradeLevel();
            } else if (item.getUpgradeType().equals("Fighter")) {
                fighterPoints -= item.getUpgradeLevel();
            } else if (item.getUpgradeType().equals("Merchant")) {
                merchantPoints -= item.getUpgradeLevel();
                for (Region region : this.universe.getRegions()) {
                    region.updateMarketPrices(item.getUpgradeLevel());
                }
            } else if (item.getUpgradeType().equals("Engineer")) {
                engineerPoints -= item.getUpgradeLevel();
            }
            return true;
        }
        return false;
    }

    public int getSlowRepairPrice() {
        return (12 - engineerPoints);
    }

    public int getFastRepairPrice() {
        return (12 - engineerPoints)
                * (currentShip.getMaxHealth() - currentShip.getHealth());
    }

    public boolean repairShipSlow() {
        if (credits >= getSlowRepairPrice()) {
            if (this.currentShip.repairSlow()) {
                credits -= getSlowRepairPrice();
                return true;
            }
        }
        return false;
    }

    public boolean repairShipFast() {
        if (credits >= getFastRepairPrice()) {
            this.currentShip.repairFast();
            credits -= getFastRepairPrice();
            return true;
        }
        return false;
    }

    public int getCredits() {
        return credits;
    }

    public Ship getCurrentShip() {
        return currentShip;
    }

    public void loseCredits(int creditsLost) {
        credits = credits - creditsLost;
    }

    public void addCredits(int creditsGained) {
        credits = credits + creditsGained;
    }

    public int getPilotPoints() {
        return pilotPoints;
    }

    public int getFighterPoints() {
        return fighterPoints;
    }
}
