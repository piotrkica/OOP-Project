package agh.lab;

import java.util.*;

import static agh.lab.MapDirection.MAP_DIRS_INDEXED;

public class Animal {
    private final MapWithJungle map;
    private final List<IPositionChangeObserver> observers = new ArrayList<>();
    private final Random rand = new Random();
    private final Genes genes;
    private final List<Animal> children = new ArrayList<>();
    private MapDirection direction = MAP_DIRS_INDEXED[rand.nextInt(8)];
    private Vector2d position;
    private int energy;
    private int lifeSpan = 0;
    private int childrenCount = 0;
    private int dayOfDeath = -1;
    private int childrenSinceFollowing = 0;
    private int descendantsBeforeFollowing = 0;
    private int daysFollowed = 0;
    private boolean beingFollowed = false;
    private boolean alreadyCountedToDescendants = false;

    public Animal(MapWithJungle map, Vector2d initialPosition, int startingEnergy) {
        this.map = map;
        this.position = initialPosition;
        this.energy = startingEnergy;
        this.genes = new Genes();
    }

    public Animal(MapWithJungle map, Vector2d initialPosition, int[] strongerGenes, int[] weakerGenes, int energy) {
        this.map = map;
        this.position = initialPosition;
        this.genes = new Genes(strongerGenes, weakerGenes);
        this.energy = energy;
    }

    public String toString() {
        return "\ud83d\udc11";
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public void move(int moveEnergyCost) {
        Vector2d oldPosition = position;
        this.energy -= moveEnergyCost;
        this.lifeSpan++;
        this.direction = genes.rotateAnimal(this.direction);
        Vector2d newPosition = this.position.add(Objects.requireNonNull(direction.toUnitVector()));
        this.position = this.map.repositionIfOutOfBounds(newPosition);
        positionChanged(oldPosition);
    }

    public void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    public void removeAllObservers() {
        for (IPositionChangeObserver observer : new ArrayList<>(observers)) {
            observers.remove(observer);
        }
    }

    private void positionChanged(Vector2d oldPosition) {
        for (IPositionChangeObserver observer : observers) {
            observer.positionChanged(this, oldPosition);
        }
    }

    public int getEnergy() {
        return this.energy;
    }

    public void addEnergy(int grassEnergyValueSplit) {
        this.energy += grassEnergyValueSplit;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public Animal reproduce(Animal other) {
        int childEnergy = this.energy / 4 + other.energy / 4;
        this.setEnergy(3 * this.energy / 4);
        other.setEnergy(3 * other.energy / 4);
        this.childrenCount++;
        other.childrenCount++;
        Animal newBorn = new Animal(this.map, this.map.findFreePositionForChild(position), this.getGenesArray(), other.getGenesArray(), childEnergy);
        this.children.add(newBorn);
        other.children.add(newBorn);
        if (this.beingFollowed) {
            childrenSinceFollowing++;
        }
        if (other.beingFollowed) {
            other.childrenSinceFollowing++;
        }
        return newBorn;
    }

    public Genes getGenes() {
        return this.genes;
    }

    public int[] getGenesArray() {
        return this.genes.getGenesArray();
    }

    public int getLifeSpan() {
        return this.lifeSpan;
    }

    public int getChildrenCount() {
        return this.childrenCount;
    }

    public void setDayOfDeath(int dayOfDeath) {
        this.dayOfDeath = dayOfDeath;
    }

    public int getDescendantCount() {
        int descendants = 0;
        for (Animal child : children) {
            if(!child.alreadyCountedToDescendants){
                child.alreadyCountedToDescendants = true;
                descendants += child.getDescendantCount() + 1;
            }
        }
        for(Animal child : children){
            child.alreadyCountedToDescendants = false;
        }
        return descendants;
    }

    public void setFollowing(boolean isFollowed) {
        if(!isFollowed){
            this.childrenSinceFollowing = 0;
            this.descendantsBeforeFollowing = 0;
            this.daysFollowed = 0;
        }
        this.beingFollowed = isFollowed;
        this.descendantsBeforeFollowing = this.getDescendantCount();
    }

    private int calculateDescendantsSinceFollowing(){
        this.daysFollowed++;
        int currentDescendants = this.getDescendantCount();
        return currentDescendants - descendantsBeforeFollowing;
    }

    public String getStats() {
        String stats = "";
        stats += "Energy: " + this.energy + "\n";
        stats += "Genotype: " + this.genes.toString() + "\n";
        stats += "Number of children: " + this.childrenCount + "\n";
        stats += "Number of descendants: " + this.getDescendantCount() + "\n";
        if (this.dayOfDeath != -1) {
            stats += "Died on day: " + this.dayOfDeath + "\n";
        }
        stats += "\n";
        stats += "Days followed:" + this.daysFollowed + "\n";
        stats += "Children since following: " + this.childrenSinceFollowing + "\n";

        stats += "Descendants since following: " + this.calculateDescendantsSinceFollowing() + "\n";
        return stats;
    }
}
