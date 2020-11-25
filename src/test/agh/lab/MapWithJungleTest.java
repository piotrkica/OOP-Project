/*package agh.lab;

import org.junit.Test;

import static org.junit.Assert.*;

public class MapWithJungleTest {
    private final IWorldMap map = new MapWithJungle(11, 6, 0, 0);

    @Test
    public void testCanMoveTo() {
        assertFalse(map.canMoveTo(new Vector2d(-1, -1)));
        assertFalse(map.canMoveTo(new Vector2d(11, 2)));
        assertFalse(map.canMoveTo(new Vector2d(2, 6)));
        assertTrue(map.canMoveTo(new Vector2d(2, 2)));
    }

    @Test
    public void testPlace() {
        Animal wombat = new Animal(map, new Vector2d(3,4));
        Animal lemur = new Animal(map, new Vector2d(3,4));
        map.place(wombat);
        map.place(lemur);
        assertNotEquals(lemur, map.objectAt(new Vector2d(3,4)));
        assertTrue(map.place(new Animal(map, new Vector2d(4, 4))));
    }

    @Test
    public void testIsOccupied() {
        map.place(new Animal(map, new Vector2d(4, 4)));
        map.place(new Animal(map, new Vector2d(3, 3)));
        assertTrue(map.isOccupied(new Vector2d(3, 3)));
        assertFalse(map.isOccupied(new Vector2d(1, 1)));
    }

    @Test
    public void testObjectAt() {
        map.place(new Animal(map, new Vector2d(4, 4)));
        assertNull(map.objectAt(new Vector2d(1, 1)));
        Animal animal = new Animal(map);
        map.place(animal);
        assertEquals(animal, map.objectAt(new Vector2d(2, 2)));
    }
}
*/