package agh.lab;

import java.util.*;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

import static java.lang.Math.*;

public class MapWithJungle implements IPositionChangeObserver {
    private final MapVisualizer mapVis = new MapVisualizer(this);
    private final Vector2d bottomLeft;
    private final Vector2d topRight;
    private final Vector2d bottomLeftJungle;
    private final Vector2d topRightJungle;
    private final Multimap<Vector2d, Animal> animalsMM = ArrayListMultimap.create();
    private final Map<Vector2d, Grass> grassTilesHM = new HashMap<>();
    private final Random rand = new Random();

    public MapWithJungle(int width, int height, float jungleRatio) {
        this.bottomLeft = new Vector2d(0, 0);
        this.topRight = new Vector2d(width - 1, height - 1);
        int jungleWidth = (int) floor(width * jungleRatio);
        int jungleHeight = (int) floor(height * jungleRatio);
        this.bottomLeftJungle = new Vector2d((width - jungleWidth) / 2, (height - jungleHeight) / 2);
        this.topRightJungle = new Vector2d((width + jungleWidth) / 2, (height + jungleHeight) / 2);
    }

    public String toString() {
        return mapVis.draw(bottomLeft, topRight);
    }

    public boolean isOccupied(Vector2d position) {
        return this.objectAt(position) != null;
    }

    public void place(Animal animal) {
        if (!canMoveTo(animal.getPosition())) {
            throw new IllegalArgumentException(animal.getPosition() + " field is out of bounds");
        }
        animalsMM.put(animal.getPosition(), animal);
        animal.addObserver(this);
    }

    public boolean canMoveTo(Vector2d position) {
        return position.follows(bottomLeft) && position.precedes(topRight);
    }

    public Object objectAt(Vector2d position) {
        if (animalsMM.get(position).size() == 0) {
            return grassTilesHM.get(position);
        }
        return animalsMM.get(position);
    }

    public Animal getStrongestAnimalAt(Vector2d position) {
        if (animalsMM.get(position).size() == 0) {
            return null;
        }
        Animal strongestAnimal = Iterables.get(animalsMM.get(position), 0);
        for (Animal animal : animalsMM.get(position)) {
            if (animal.getEnergy() > strongestAnimal.getEnergy()) {
                strongestAnimal = animal;
            }
        }
        return strongestAnimal;
    }

    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition) {
        animalsMM.remove(oldPosition, animal);
        animalsMM.put(animal.getPosition(), animal);

    }

    public boolean isAJungleTile(Vector2d position) {
        return position.follows(bottomLeftJungle) && position.precedes(topRightJungle);
    }

    public Vector2d findFreeTile(Vector2d bottomLeft, Vector2d topRight) {
        Vector2d position;
        do {
            position = new Vector2d(rand.nextInt(topRight.x - bottomLeft.x + 1) + bottomLeft.x
                    , rand.nextInt(topRight.y - bottomLeft.y + 1) + bottomLeft.y);
        } while (this.isOccupied(position));
        return position;
    }

    public List<Animal> getStrongestAnimals(Vector2d position) {
        List<Animal> strongestAnimals = new LinkedList<>();
        int strongestEnergy = 0;
        for (Animal animal : animalsMM.get(position)) {
            strongestEnergy = max(strongestEnergy, animal.getEnergy());
        }
        for (Animal animal : animalsMM.get(position)) {
            if (animal.getEnergy() == strongestEnergy) {
                strongestAnimals.add(animal);
            }
        }
        return strongestAnimals;
    }

    public boolean placeGrassInJungle() {
        if (notPossibleFreeTileInJungle()) {
            return false;
        }
        Vector2d position;
        position = findFreeTile(bottomLeftJungle, topRightJungle);
        grassTilesHM.put(position, new Grass(position));
        return true;
    }

    public boolean placeGrassOutsideJungle() {
        if (notPossibleFreeTileOutsideJungle()) {
            return false;
        }
        Vector2d position;
        do {
            position = findFreeTile(bottomLeft, topRight);
        } while (isAJungleTile(position));

        grassTilesHM.put(position, new Grass(position));
        return true;

    }

    public boolean notPossibleFreeTileInJungle() {
        for (int x = bottomLeftJungle.x; x <= topRightJungle.x; x++) {
            for (int y = bottomLeftJungle.y; y <= topRightJungle.y; y++) {
                if (!isOccupied(new Vector2d(x, y))) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean notPossibleFreeTileOutsideJungle() {
        for (int x = bottomLeft.x; x <= topRight.x; x++) {
            for (int y = bottomLeft.y; y <= topRight.y; y++) {
                if (isAJungleTile(new Vector2d(x, y))) {
                    continue;
                }
                if (!isOccupied(new Vector2d(x, y))) {
                    return false;
                }
            }
        }
        return true;
    }

    public Vector2d findFreePositionForChild(Vector2d parentsPosition) {
        for (int i = -1; i < 1; i++) {
            for (int j = -1; j < 1; j++) {
                Vector2d possiblePosition = repositionIfOutOfBounds(new Vector2d(parentsPosition.x + i, parentsPosition.y + j));
                if (!isOccupied(possiblePosition)) {
                    return possiblePosition;
                }
            }
        }
        return repositionIfOutOfBounds(new Vector2d(rand.nextInt(3) + parentsPosition.x - 1, rand.nextInt(3) + parentsPosition.y - 1));
    }

    public Vector2d repositionIfOutOfBounds(Vector2d position) {
        if (!canMoveTo(position)) {
            int x = position.x;
            int y = position.y;
            if (position.x >= topRight.x) {
                x = bottomLeft.x;
            } else if (position.x < bottomLeft.x) {
                x = topRight.x;
            }
            if (position.y >= topRight.y) {
                y = bottomLeft.y;
            } else if (position.y < bottomLeft.y) {
                y = topRight.y;
            }
            position = new Vector2d(x, y);
        }
        return position;
    }

    public Multimap<Vector2d, Animal> getAnimalsMM() {
        return this.animalsMM;
    }

    public Map<Vector2d, Grass> getGrassTilesHM() {
        return this.grassTilesHM;
    }

    public Vector2d getBottomLeft() {
        return this.bottomLeft;
    }

    public Vector2d getTopRight() {
        return this.topRight;
    }

}
