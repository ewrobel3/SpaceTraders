package sample.tests;
import org.junit.*;
import static org.junit.Assert.*;
import sample.Coordinate;

public class CoordinateTest {
    @Test
    public void getterTest() {
        Coordinate a = new Coordinate(10, 20);
        assertEquals(a.getX(), 10);
        assertEquals(a.getY(), 20);
    }

    @Test
    public void distanceTestZero() {
        Coordinate a = new Coordinate(10, 20);
        Coordinate b = new Coordinate(10, 20);
        assertEquals(a.distance(b), 0, 0);
        assertEquals(b.distance(a), 0, 0);
    }

    @Test
    public void distanceTestSelf() {
        Coordinate a = new Coordinate(10, 20);
        assertEquals(a.distance(a), 0, 0);
    }

    @Test (expected = NullPointerException.class)
    public void distanceTestNull() {
        Coordinate a = new Coordinate(10, 20);
        assertEquals(a.distance(null), 0, 0);
    }

    @Test
    public void distanceTestNormal() {
        Coordinate a = new Coordinate(10, 20);
        Coordinate b = new Coordinate(30, 50);
        assertEquals(a.distance(b), 36.0555, 0.0001);
    }

    @Test
    public void distanceTestNegative() {
        Coordinate a = new Coordinate(-10, -20);
        Coordinate b = new Coordinate(-30, -50);
        assertEquals(a.distance(b), 36.0555, 0.0001);
    }

    @Test
    public void equalsTest() {
        Coordinate a = new Coordinate(10, 20);
        Coordinate b = new Coordinate(10, 20);
        assertEquals(a, a);
        assertEquals(a, b);
        assertEquals(b, a);
        Coordinate c = new Coordinate(0, 0);
        assertNotEquals(a, c);
        assertNotEquals(a, null);
    }
}
