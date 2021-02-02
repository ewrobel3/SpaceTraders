package sample;

public class Coordinate {

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private final int x;
    private final int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double distance(Coordinate other) {
        float xd = x - other.getX();
        float yd = y - other.getY();
        return Math.sqrt(Math.pow(xd, 2) + Math.pow(yd, 2));
    }
}
