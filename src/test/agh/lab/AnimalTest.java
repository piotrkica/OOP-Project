package agh.lab;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class AnimalTest {
    private final IWorldMap map = new RectangularMap( 5, 5);
    private final Animal wombat = new Animal(map);
    private final OptionsParser parser = new OptionsParser();

    @Test
    public void testToString() {
        assertEquals("N", wombat.toString());
    }

    @Test
    public void testPosition() {
        wombat.move(MoveDirection.FORWARD);
        assertEquals(wombat.getPosition(), new Vector2d(2, 3));
        wombat.move(MoveDirection.BACKWARD);
        assertEquals(wombat.getPosition(), new Vector2d(2, 2));
    }

    @Test
    public void testOrientation() {
        wombat.move(MoveDirection.LEFT);
        assertEquals(wombat.getOrientation(), MapDirection.WEST);
        wombat.move(MoveDirection.RIGHT);
        assertEquals(wombat.getOrientation(), MapDirection.NORTH);
    }


    @Test
    public void testSingleAnimal() {
        String[] dirs = {"f", "f", "f", "r", "f", "f", "f"};
        MoveDirection[] parsedDirs = parser.parse(dirs);
        for (MoveDirection dir : parsedDirs) {
            wombat.move(dir);
        }
        assertEquals(wombat.getPosition(), new Vector2d(4, 4));
        assertEquals(wombat.getOrientation(), MapDirection.EAST);

        dirs = new String[]{"b", "b", "b", "b", "b", "l", "b", "b", "b", "b", "b"};
        parsedDirs = parser.parse(dirs);
        for (MoveDirection dir : parsedDirs) {
            wombat.move(dir);
        }
        assertEquals(wombat.getPosition(), new Vector2d(0, 0));
        assertEquals(wombat.getOrientation(), MapDirection.NORTH);
    }


}
