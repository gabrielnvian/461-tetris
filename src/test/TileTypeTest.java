package test;

import org.junit.Before;
import org.junit.Test;
import org.psnbtech.TileType;

import static org.junit.Assert.assertTrue;

public class TileTypeTest {
    private TileType tileType;

    @Before
    public void setUp() {
        tileType = TileType.TypeL;
    }

    @Test
    public void testGetSpawnColumnDoesNotThrow() {
        int result = tileType.getSpawnColumn();

        // Any value is good here
        assertTrue(result >= 0);
    }

    @Test
    public void testGetSpawnRowDoesNotThrow() {
        int result = tileType.getSpawnRow();

        // Any value is good here
        assertTrue(result >= 0);
    }
}
