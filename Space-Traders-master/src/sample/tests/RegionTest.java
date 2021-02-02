package sample.tests;
import org.junit.*;
import static org.junit.Assert.*;

import sample.Alignment;
import sample.Coordinate;
import sample.Region;

public class RegionTest {
    private Region region;

    @Before
    public void init() {
        region = new Region("nam",
                "descrip",
                5,
                new Coordinate(-1, 1),
                null,
                Alignment.FRIENDLY);
    }

    @Test
    public void getterTest() {
        assertEquals(region.getDescription(), "descrip");
        assertEquals(region.getName(), "nam");
        assertEquals(region.getTechLevel(), 5);
        assertEquals(region.getPosition(), new Coordinate(-1, 1));
    }

    @Test
    public void visitationTest() {
        assertFalse(region.isVisited());
        region.visit();
        assertTrue(region.isVisited());
    }
}
