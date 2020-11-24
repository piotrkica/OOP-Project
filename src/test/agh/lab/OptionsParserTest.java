package agh.lab;

import org.junit.Test;

import static org.junit.Assert.*;

public class OptionsParserTest {
    private final IWorldMap map = new RectangularMap(5, 5);
    private final Animal wombat = new Animal(map);
    private final OptionsParser parser = new OptionsParser();
    private final String[] dirs = {"f", "b", "f", "f", "r", "f", "f", "r", "l", "r"};
    private final String[] wrongDirs = {"a", "c", "d", "e"};

    @Test
    public void testOptionsParser() { //assertThrows, fail
        MoveDirection[] parsedDirs = parser.parse(dirs);
        for (MoveDirection direction : parsedDirs) {
            wombat.move(direction);
        }
        assertEquals(new Vector2d(4, 4), wombat.getPosition());
        assertEquals(MapDirection.SOUTH, wombat.getOrientation());
        assertThrows(IllegalArgumentException.class, () -> parser.parse(wrongDirs));
    }
}
