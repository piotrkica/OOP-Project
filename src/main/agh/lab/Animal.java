package agh.lab;

import java.util.*;

import static agh.lab.MapDirection.MAP_DIRS_INDEXED;

public class Animal implements IMapElement {
    private final MapWithJungle map;
    private final List<IPositionChangeObserver> observers = new ArrayList<>();
    private final Random rand = new Random();
    private MapDirection orientation = MAP_DIRS_INDEXED[rand.nextInt(8)];
    private Vector2d position;
    private int energy;
    private int lifeSpan = 0;
    private int childrenCount = 0;
    private final Genes genes;

    public Animal(MapWithJungle map, Vector2d initialPosition, int startingEnergy) {
        this.map = map;
        this.position = initialPosition;
        this.energy = startingEnergy;
        this.genes = new Genes();
    }

    public Animal(MapWithJungle map, Vector2d initialPosition, List<Integer> strongerGenes, List<Integer> weakerGenes, int energy) {
        this.map = map;
        this.position = initialPosition;
        this.genes = new Genes(strongerGenes, weakerGenes);
        this.energy = energy;
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

    public void move(int moveEnergyCost) {
        Vector2d oldPosition = position;
        this.energy-= moveEnergyCost;
        lifeSpan++;
        MapDirection direction = genes.chooseNextMove();
        Vector2d newPosition = this.position.add(direction.toUnitVector());
        this.position = this.map.repositionIfOutOfBounds(newPosition);
        positionChanged(oldPosition);
        this.orientation = direction;

    }

    public void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    public void removeAllObservers() {
        for (IPositionChangeObserver observer : new ArrayList<>(observers)) {
            observers.remove(observer);
        }
    }

    public void positionChanged(Vector2d oldPosition) {
        for (IPositionChangeObserver observer : observers) {
            observer.positionChanged(this, oldPosition);
        }
    }

    public int getEnergy(){
        return this.energy;
    }

    public void addEnergy(int grassEnergyValueSplit) {
        this.energy += grassEnergyValueSplit;
    }

    public void setEnergy(int energy){
        this.energy = energy;
    }

    public void increaseChildrenCount(){
        this.childrenCount++;
    }

    public List<Integer> getGenes(){
        return this.genes.getGenes();
    }

    public int getLifeSpan(){
        return this.lifeSpan;
    }

    public int getChildrenCount(){
        return this.childrenCount;
    }
}
