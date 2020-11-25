/*package agh.lab;

import org.junit.Test;

import static org.junit.Assert.*;

public class IntegrationTests {
    private final MapWithJungle rectMap = new MapWithJungle(11, 6,0, 0);
    private final OptionsParser parser = new OptionsParser();

    @Test
    public void testIntegrationRectangular() {
        Vector2d[] startingPositions = {new Vector2d(2, 2), new Vector2d(3, 4)};
        String[] directions = {"f", "b", "r", "l", "f", "f", "r", "r", "f", "f", "f", "f", "f", "f", "f", "f"};
        SimulationEngine engine = new SimulationEngine(parser.parse(directions), rectMap, startingPositions);
        engine.run();
        assertTrue(rectMap.objectAt(new Vector2d(2, 0)) instanceof Animal);
        assertTrue(rectMap.objectAt(new Vector2d(3, 5)) instanceof Animal);
    }
}
*/