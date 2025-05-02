package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        BoardPanelTest.class,
        ClockTest.class,
        SidePanelTest.class,
        TetrisTest.class,
        TileTypeTest.class,
})
public class Test {
}