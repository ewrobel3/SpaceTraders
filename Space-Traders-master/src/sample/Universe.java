package sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Universe {

    private ArrayList<String> nameList = new ArrayList<String>(
            Arrays.asList("Xylon", "Croyle", "Harcrow",
        "Wynter", "Gammon", "Stoyer", "Nyseth", "Faulknen", "Bucanan", "Jann",
        "Colee", "Arrin", "Grighte", "Ander", "Richua", "Dersimm", "Robarn",
        "Artis", "Rodre", "Moory", "Rezal", "Cotte", "Willee", "Coopatt", "Warden",
        "V'Aelon", "L'Aunal", "Trani", "L'Olith", "H'Vaadwach", "K'Vani", "B'Delvi",
        "H'Rahu", "Woka", "Tr'Iken"));

    private static String[] geographyList = new String[] {"Mountainous", "Flatland", "Forested",
        "Rolling Hills", "Metropolitan", "Volcanic", "Islands"};

    private static String[] climateList = new String[] {"Rainy", "Desert/Dry",
        "Snowy", "Lush", "Temperate", "Swamp",
        "Tropical", "Sulfuric"};

    private static final int MIN_DISTANCE = 10;

    private Player player1;
    private ArrayList<Region> regions;
    private boolean hasWon;

    /**
     * Creates a new Universe in which players can play
     *
     * @param numRegions the number of regions/space ports in the universe
     * @param mode the game mode difficulty
     * @param points the point allocations the player selected
     */
    public Universe(int numRegions, Mode mode, Integer[] points) {
        Random random = new Random();
        regions = new ArrayList<>();
        for (int i = 0; i < numRegions; i++) {
            int nameIndex = random.nextInt(nameList.size());
            String name = nameList.get(nameIndex);
            nameList.remove(nameIndex);
            String geo = geographyList[(int) (random.nextDouble() * geographyList.length)];
            String climate = climateList[(int) (random.nextDouble() * climateList.length)];
            Alignment alignment = Alignment.getRandomAlignment();
            String description = "Geography: " + geo + "\n"
                    + "Climate: " + climate + "\n"
                    + "Alignment: " + alignment.toString();
            Coordinate position = placeNewRegion(regions);
            int techLevel = 1 + random.nextInt(5);
            regions.add(new Region(name, description, techLevel, position, this, alignment));
        }
        Region start = regions.get((int) (random.nextDouble() * numRegions));
        Region winRegion = regions.get((int) (random.nextDouble() * numRegions));
        winRegion.addToMarket(Good.WINITEM, Good.WINITEM.getBasePrice());
        this.player1 = new Player(mode, points, start, this);
        for (Region region : regions) {
            region.updateMarketPrices(-(player1.getMerchantPoints()));
        }
        this.hasWon = false;
    }

    private Coordinate placeNewRegion(ArrayList<Region> regions) {
        Random xyRandom = new Random();
        int x;
        int y;
        if (regions.isEmpty()) {
            x = xyRandom.nextInt(101); //bound is exclusive, lower is 0
            y = xyRandom.nextInt(101);
        } else {
            Region start = regions.get(xyRandom.nextInt(regions.size()));
            double angle = xyRandom.nextDouble() * 2 * Math.PI;
            double distance = MIN_DISTANCE + xyRandom.nextDouble() * 30;
            x = (int) (start.getPosition().getX() + (distance * Math.sin(angle)));
            y = (int) (start.getPosition().getY() + (distance * Math.cos(angle)));

            for (int i = 0; i < regions.size(); i++) {
                if (Math.sqrt(Math.pow(x - regions.get(i).getPosition().getX(), 2)
                        + Math.pow(y - regions.get(i).getPosition().getY(), 2)) < MIN_DISTANCE
                        || x > 100 || x < 0 || y > 100 || y < 0) {
                    return placeNewRegion(regions);
                }
            }
        }
        return new Coordinate(x, y);
    }

    public Player getPlayer() {
        return player1;
    }

    public ArrayList<Region> getRegions() {
        return regions;
    }

    public void win() {
        this.hasWon = true;
    }

    public boolean gameWon() {
        return hasWon;
    }
}
