package agh.lab;

import java.util.Collection;

public class SimulationEngine implements IEngine {
    private final MoveDirection[] directions;
    private final MapWithJungle map;
    private final int animalsNo;

    public SimulationEngine(MoveDirection[] directions, MapWithJungle map, Vector2d[] startingPosition) {
        this.directions = directions;
        this.map = map;
        this.animalsNo = startingPosition.length;
        for (Vector2d position : startingPosition) {
            map.place(new Animal(map, position));
        }
    }

    public void run() {
        Collection<Animal> animalCollection = map.getAnimalsMM().values();
        Animal[] animals = new Animal[animalCollection.size()];
        animals = animalCollection.toArray(animals);
        for (int i = 0; i < directions.length; i++) {
            animals[i % animalsNo].move(directions[i]);
        }

    }
}