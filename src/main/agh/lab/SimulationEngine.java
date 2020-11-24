package agh.lab;

public class SimulationEngine implements IEngine {
    private final MoveDirection[] directions;
    private final IWorldMap map;
    private final Vector2d[] animalPositions;

    public SimulationEngine(MoveDirection[] directions, IWorldMap map, Vector2d[] startingPosition) {
        this.directions = directions;
        this.map = map;
        this.animalPositions = startingPosition;
        for (Vector2d position : startingPosition) {
            map.place(new Animal(map, position));
        }
    }

    public void run() {
        int n = animalPositions.length;
        for (int i = 0; i < directions.length; i++) {
            Animal movedAnimal = (Animal) this.map.objectAt(animalPositions[i % n]);
            movedAnimal.move(directions[i]);
            this.animalPositions[i % n] = movedAnimal.getPosition();
        }
    }
}