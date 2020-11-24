package agh.lab;

public class RectangularMap extends AbstractWorldMap {
    private final Vector2d bottomLeft;
    private final Vector2d topRight;

    public RectangularMap(int width, int height) {
        this.bottomLeft = new Vector2d(0, 0);
        this.topRight = new Vector2d(width - 1, height - 1);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !isOccupied(position) && position.follows(bottomLeft) && position.precedes(topRight);
    }

    @Override
    public Object objectAt(Vector2d position) {
        return animalsHM.get(position);
    }
}
