package agh.lab;

import java.util.*;

import static java.lang.Math.*;

public class GrassField extends AbstractWorldMap {
    private final Map<Vector2d, Grass> grassTilesHM = new HashMap<>();

    public GrassField(int grassCount) {
        Random rand = new Random();
        int bound = (int) sqrt(grassCount * 10);
        Vector2d position;
        for (int i = 0; i < grassCount; i++) {
            do {
                position = new Vector2d(rand.nextInt() % bound, rand.nextInt() % bound);
            } while (this.isOccupied(position));
            grassTilesHM.put(position, new Grass(position));
            mapBoundaries.addGrass(new Grass(position));
        }
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return (!(this.objectAt(position) instanceof Animal));
    }

    public boolean placeGrass(Grass grass) {
        Vector2d position = grass.getPosition();
        if ((this.objectAt(position) instanceof Grass)) {
            //throw new IllegalArgumentException(position + " already has grass");
            return false;
        }
        grassTilesHM.put(position, new Grass(position));
        return true;
    }

    @Override
    public Object objectAt(Vector2d position) {
        Object animalOrGrass = animalsHM.get(position);
        if (animalOrGrass != null) {
            return animalOrGrass;
        }
        animalOrGrass = grassTilesHM.get(position);
        return animalOrGrass;
    }
}
