package sample.tests;

import org.junit.Before;
import org.junit.Test;
import sample.*;

import static org.junit.Assert.*;


public class ShipTest {
    private Region region;
    private Ship ship;

    @Before
    public void init() {
        region = new Region("nam",
                "desc",
                3,
                new Coordinate(0, 0),
                null,
                Alignment.SALTY);
        ship = new Ship(region, ShipType.BASIC);
    }

    @Test
    public void getterTest() {
        assertEquals(ship.getName(), "shippy");
        assertEquals(ship.getCargoSpace(), 50);
        assertEquals(ship.getInventory(), null); //should be changed when cargo is implemented
        assertEquals(ship.getFuelCapacity(), 20);
        assertEquals(ship.getFuel(), 20);
        assertEquals(ship.getHealth(), 30);
        assertEquals(ship.getMaxHealth(), 30);
        assertEquals(ship.getLocation(), region);
    }

    @Test
    public void flyingTest() {
        Region newRegion = new Region("newreg",
                "desc2",
                10,
                new Coordinate(10, 0),
                null,
                Alignment.SALTY);
        assertEquals(ship.getLocation(), region);
        ship.flyTo(newRegion, true);
        assertEquals(ship.getLocation(), newRegion);
        assertEquals(ship.getFuel(), 10);
    }

    @Test
    public void flyingOutOfRangeTest() {
        Region newRegion = new Region("newreg",
                "desc2",
                10,
                new Coordinate(50, 0),
                null,
                Alignment.AGGRESSIVE);
        assertEquals(ship.getLocation(), region);
        ship.flyTo(newRegion, true);
        assertEquals(ship.getLocation(), region);
        assertEquals(ship.getFuel(), 20);
    }
}
