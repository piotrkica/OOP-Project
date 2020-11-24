package agh.lab;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Vector2dTest {

    public final Vector2d x = new Vector2d(1, 1);
    public final Vector2d y = new Vector2d(-1, -1);
    public final Vector2d a = new Vector2d(1, -1);
    public final Vector2d b = new Vector2d(-1, 1);
    public final Vector2d c = new Vector2d(0, 0);

    @Test
    public void testEquals() {
        assertNotEquals(x, y);
        assertEquals(x, new Vector2d(1, 1));
        assertNotEquals(null, x);
    }

    @Test
    public void testToString() {
        assertEquals(x.toString(), "(1, 1)");
        assertNotEquals(x.toString(), y.toString());
    }

    @Test
    public void testPrecedes() {
        assertTrue(y.precedes(x));
        assertFalse(x.precedes(y));
        assertFalse(a.precedes(y));
        assertFalse(b.precedes(y));
        assertTrue(a.precedes(x));
        assertTrue(b.precedes(x));
    }

    @Test
    public void testFollows() {
        assertTrue(x.follows(y));
        assertFalse(y.follows(x));
        assertFalse(a.follows(x));
        assertFalse(b.follows(x));
        assertTrue(a.follows(y));
        assertTrue(b.follows(y));
    }

    @Test
    public void testUpperRight() {
        assertEquals(a.upperRight(b), x);
        assertNotEquals(a.upperRight(b), y);
    }

    @Test
    public void testLowerLeft() {
        assertEquals(a.lowerLeft(b), y);
        assertNotEquals(a.lowerLeft(b), x);
    }

    @Test
    public void testAdd() {
        assertEquals(x.add(y), c);
        assertNotEquals(x.add(c), y);
    }

    @Test
    public void testSubtract() {
        assertEquals(x.subtract(x), c);
        assertNotEquals(x.subtract(y), c);
    }

    @Test
    public void testOpposite() {
        assertEquals(x.opposite(), y);
        assertNotEquals(x.opposite(), c);
    }

}
