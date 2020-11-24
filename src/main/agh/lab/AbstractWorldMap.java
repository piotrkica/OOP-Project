package agh.lab;

import java.util.HashMap;
import java.util.Map;

abstract public class AbstractWorldMap implements IWorldMap, IPositionChangeObserver {
    private final MapVisualizer mapVis = new MapVisualizer(this);
    protected final Map<Vector2d, Animal> animalsHM = new HashMap<>();
    protected final MapBoundary mapBoundaries = new MapBoundary();

    public String toString() {
        return mapVis.draw(mapBoundaries.getBottomLeftBoundary(), mapBoundaries.getTopRightBoundary());
    }

    @Override
    public boolean place(Animal animal) {
        if (!canMoveTo(animal.getPosition())) {
            throw new IllegalArgumentException(animal.getPosition() + " field is occupied");
        }
        animalsHM.put(animal.getPosition(), animal);
        mapBoundaries.addAnimal(animal);
        animal.addObserver(this);
        return true;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return this.objectAt(position) != null;
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        if (oldPosition.equals(newPosition)) {
            return;
        }
        Animal animal = animalsHM.get(oldPosition);
        animalsHM.remove(oldPosition);
        animalsHM.put(newPosition, animal);
    }
}