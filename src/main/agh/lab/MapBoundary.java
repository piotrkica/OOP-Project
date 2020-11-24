package agh.lab;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class MapBoundary implements IPositionChangeObserver {
    private final SortedSet<Vector2d> elementsByX = new TreeSet<Vector2d>(new Vector2dComparatorX());
    private final SortedSet<Vector2d> elementsByY = new TreeSet<Vector2d>(new Vector2dComparatorY());

    public void addAnimal(Animal animal) {
        elementsByX.add(animal.getPosition());
        elementsByY.add(animal.getPosition());
        animal.addObserver(this);
    }

    public void addGrass(Grass grass) {
        elementsByX.add(grass.getPosition());
        elementsByY.add(grass.getPosition());
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        elementsByX.remove(oldPosition);
        elementsByY.remove(oldPosition);

        elementsByX.add(newPosition);
        elementsByY.add(newPosition);
    }

    public Vector2d getBottomLeftBoundary() {
        if (elementsByX.isEmpty() || elementsByY.isEmpty()) {
            return new Vector2d(0, 0);
        }
        return new Vector2d(elementsByX.first().x, elementsByY.first().y);
    }

    public Vector2d getTopRightBoundary() {
        if (elementsByX.isEmpty() || elementsByY.isEmpty()) {
            return new Vector2d(5, 5);
        }
        return new Vector2d(elementsByX.last().x, elementsByY.last().y);
    }
}

class Vector2dComparatorX implements Comparator {

    @Override
    public int compare(Object object1, Object object2){
        if (!(object1 instanceof Vector2d) || !(object2 instanceof Vector2d)){
            throw new ClassCastException("There are 1-2 objects in Comparator<Vector2d> that are not Vector2d type");
        }
        Vector2d vector1 = (Vector2d) object1;
        Vector2d vector2 = (Vector2d) object2;
        if (vector1.x == vector2.x){
            return vector1.y - vector2.y;
        }
        return vector1.x - vector2.x;
    }
}

class Vector2dComparatorY implements Comparator {

    @Override
    public int compare(Object object1, Object object2){
        if (!(object1 instanceof Vector2d) || !(object2 instanceof Vector2d)){
            throw new ClassCastException("There are 1-2 objects in Comparator<Vector2d> that are not Vector2d type");
        }
        Vector2d vector1 = (Vector2d) object1;
        Vector2d vector2 = (Vector2d) object2;
        if (vector1.y == vector2.y){
            return vector1.x - vector2.x;
        }
        return vector1.y - vector2.y;
    }
}
