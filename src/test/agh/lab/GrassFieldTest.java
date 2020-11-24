package agh.lab;

import org.junit.Test;

import static org.junit.Assert.*;

public class GrassFieldTest {
    private final GrassField map = new GrassField(10);

    @Test
    public void testCanMoveTo() {
        assertTrue(map.canMoveTo(new Vector2d(4, 4)));
        map.place(new Animal(map, new Vector2d(4, 4)));
        assertFalse(map.canMoveTo(new Vector2d(4, 4)));
    }

    @Test
    public void testPlace() {
        Animal wombat = new Animal(map, new Vector2d(3, 4));
        Animal lemur = new Animal(map, new Vector2d(3, 4));
        map.place(wombat);
        assertThrows(IllegalArgumentException.class, () -> map.place(lemur));
        assertNotEquals(lemur, map.objectAt(new Vector2d(3, 4)));
        assertTrue(map.place(new Animal(map, new Vector2d(4, 4))));
    }

    @Test
    public void testPlaceGrass() {
        assertTrue(map.placeGrass(new Grass(new Vector2d(150, 150))));
        assertFalse(map.placeGrass(new Grass(new Vector2d(150, 150))));
    }

    @Test
    public void testIsOccupied() {
        map.place(new Animal(map, new Vector2d(4, 4)));
        map.place(new Animal(map, new Vector2d(3, 3)));
        assertTrue(map.isOccupied(new Vector2d(3, 3)));
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
