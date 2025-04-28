package test;

import org.junit.Before;
import org.junit.Test;
import org.psnbtech.Clock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ClockTest {
    private Clock clock;

    @Before
    public void setUp() throws Exception {
        // Huge number so the clock ticks several times during the tests
        clock = new Clock(500_000_000);
    }


    @Test
    public void testSetPausedPausesClock() {
        clock.setPaused(true);

        boolean result = clock.isPaused();
        assertTrue(result);
    }

    @Test
    public void testHasElapsedCycle() throws InterruptedException {
        Thread.sleep(500);

        clock.update();

        boolean result = clock.hasElapsedCycle();
        assertTrue(result);
    }

    @Test
    public void testPeekElapsedCycleReturnsFalse() {
        clock.setPaused(true);

        boolean result = clock.peekElapsedCycle();
        assertFalse(result);
    }

    @Test
    public void testPeekElapsedCycleReturnsTrue() throws InterruptedException {
        Thread.sleep(500);

        clock.update();

        boolean result = clock.peekElapsedCycle();
        assertTrue(result);
    }

    @Test
    public void testStopsIfPaused() {
        clock.setPaused(true);

        clock.update();

        boolean result = clock.hasElapsedCycle();
        assertFalse(result);
    }
}