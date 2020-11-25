package agh.lab;

import java.util.ArrayList;
import java.util.List;

public class Animal implements IMapElement {
    private MapDirection orientation = MapDirection.NORTH;
    private Vector2d position;
    private final MapWithJungle map;
    private final List<IPositionChangeObserver> observers = new ArrayList<>();

    public Animal(MapWithJungle map) {
        this.map = map;
        this.position = new Vector2d(2, 2);
    }

    public Animal(MapWithJungle map, Vector2d initialPosition) {
        this.map = map;
        this.position = initialPosition;
    }

    public String toString() {
        switch (this.orientation) {
            case NORTH:
                return "N";
            case NORTHEAST:
                return "NE";
            case EAST:
                return "E";
            case SOUTHEAST:
                return "SE";
            case SOUTH:
                return "S";
            case SOUTHWEST:
                return "SW";
            case WEST:
                return "W";
            case NORTHWEST:
                return "NW";
            default:
                return null;
        }
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public MapDirection getOrientation() {
        return this.orientation;
    }

    public void move(MoveDirection direction) {
        Vector2d oldPosition = position;
        switch (direction) {
            case RIGHT:
                this.orientation = this.orientation.next();
                break;
            case LEFT:
                this.orientation = this.orientation.previous();
                break;
            case BACKWARD:
            case FORWARD:
                Vector2d movement = this.orientation.toUnitVector();
                if (direction == MoveDirection.BACKWARD) {
                    movement = movement.opposite();
                }
                Vector2d newPosition = this.position.add(movement);

                this.position = this.map.repositionIfOutOfBounds(newPosition);
                System.out.println(this.position);
                positionChanged(oldPosition);
                break;
        }
    }

    public void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
    }

    public void positionChanged(Vector2d oldPosition) {
        for (IPositionChangeObserver observer : observers) {
            observer.positionChanged(this, oldPosition);
        }
    }

}
