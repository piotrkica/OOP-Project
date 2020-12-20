package agh.lab;

import com.google.common.collect.Multimap;

import java.util.*;

import static java.lang.Math.ceil;

public class SimulationEngine {
    private final MapWithJungle map;
    private final int startingEnergy;
    private final int grassEnergyValue;
    private final int moveEnergyCost;
    private final StatsMaker statsMaker = new StatsMaker();

    public SimulationEngine(Map<String, Object> parameters) {
        this.map = new MapWithJungle((int) parameters.get("width"), (int) parameters.get("height"), (float) parameters.get("jungleRatio"));
        this.startingEnergy = (int) parameters.get("startingEnergy");
        this.grassEnergyValue = (int) parameters.get("grassEnergyValue");
        this.moveEnergyCost = (int) parameters.get("moveEnergyCost");
        placeStartingAnimals((int) parameters.get("startingAnimals"));
        placeStartingGrass((int) parameters.get("startingGrassTiles"));
    }

    public void dayPassed() {
        removeDeadAnimals();
        moveAnimals();
        eatGrass();
        reproduceIfPossible();
        placeGrassInJungleAndOutside();
        this.statsMaker.dayPassed();
    }

    public void placeStartingAnimals(int animalsNo) {
        for (int i = 0; i < animalsNo; i++) {
            if (this.map.notPossibleFreeTileInJungle() && this.map.notPossibleFreeTileOutsideJungle()) {
                break;
            }
            Vector2d position = this.map.findFreeTile(this.map.getBottomLeft(), this.map.getTopRight());
            Animal animal = new Animal(this.map, position, startingEnergy);
            this.map.place(animal);
            this.statsMaker.addAnimal(animal);
        }
    }

    public void placeStartingGrass(int startingGrassTiles) {
        for (int i = 0; i < startingGrassTiles / 2; i++) {
            if (this.map.notPossibleFreeTileInJungle()) {
                break;
            }
            if (this.map.placeGrassInJungle()) {
                this.statsMaker.addGrass();
            }
        }
        for (int i = 0; i < ceil(startingGrassTiles / 2.0); i++) {
            if (this.map.notPossibleFreeTileOutsideJungle()) {
                break;
            }
            if (this.map.placeGrassOutsideJungle()) {
                this.statsMaker.addGrass();
            }
        }
    }

    public void moveAnimals() {
        Collection<Animal> animalCollection = map.getAnimalsMM().values();
        Animal[] animals = new Animal[animalCollection.size()];
        animals = animalCollection.toArray(animals);
        for (Animal animal : animals) {
            animal.move(moveEnergyCost);
            this.statsMaker.animalMoved(moveEnergyCost);
        }
    }

    public void placeGrassInJungleAndOutside() {
        if (this.map.placeGrassInJungle()) {
            this.statsMaker.addGrass();
        }
        if (this.map.placeGrassOutsideJungle()) {
            this.statsMaker.addGrass();
        }
    }

    public void eatGrass() {
        Multimap<Vector2d, Animal> animalsMM = map.getAnimalsMM();
        Map<Vector2d, Grass> grassTilesHM = map.getGrassTilesHM();
        Vector2d[] grassTiles = grassTilesHM.keySet().toArray(new Vector2d[0]);

        for (Vector2d grassPosition : grassTiles) {
            if (animalsMM.get(grassPosition).size() >= 1) {
                grassTilesHM.remove(grassPosition);
                List<Animal> strongestAnimals = this.map.getStrongestAnimals(grassPosition);
                int grassEnergyValueSplit = grassEnergyValue / strongestAnimals.size();
                for (Animal animal : strongestAnimals) {
                    animal.addEnergy(grassEnergyValueSplit);
                    this.statsMaker.addEnergy(grassEnergyValueSplit);
                }
                this.statsMaker.removeGrass();
            }
        }
    }

    public void removeDeadAnimals() {
        Multimap<Vector2d, Animal> animalsMM = map.getAnimalsMM();
        Collection<Animal> animalCollection = animalsMM.values();
        Animal[] animals = new Animal[animalCollection.size()];
        animals = animalCollection.toArray(animals);

        for (Animal animal : animals) {
            if (animal.getEnergy() <= 0) {
                animal.setDayOfDeath(statsMaker.getDay());
                animal.removeAllObservers();
                animalsMM.remove(animal.getPosition(), animal);
                this.statsMaker.removeAnimal(animal.getLifeSpan(), animal.getChildrenCount());
                this.statsMaker.removeGene(animal.getGenes());
            }
        }
    }

    public void reproduceIfPossible() {
        Multimap<Vector2d, Animal> animalsMM = map.getAnimalsMM();

        Vector2d[] animalPositions = animalsMM.keySet().toArray(new Vector2d[0]);
        List<Animal> animalsBorn = new ArrayList<>();
        for (Vector2d position : animalPositions) {
            if (animalsMM.get(position).size() >= 2) { // sort
                Animal strongest1 = null;
                Animal strongest2 = null;
                for (Animal animal : animalsMM.get(position)) {
                    if (strongest1 == null) {
                        strongest1 = animal;
                    } else if (strongest2 == null) {
                        if (strongest1.getEnergy() > animal.getEnergy()) {
                            strongest2 = animal;
                        } else {
                            strongest2 = strongest1;
                            strongest1 = animal;
                        }
                    } else if (strongest1.getEnergy() < animal.getEnergy()) {
                        strongest2 = strongest1;
                        strongest1 = animal;
                    } else if (strongest2.getEnergy() < animal.getEnergy()) {
                        strongest2 = animal;
                    }
                }
                if (strongest1.getEnergy() > startingEnergy / 2 && strongest2.getEnergy() > startingEnergy / 2) {
                    Animal newBorn = strongest1.reproduce(strongest2);
                    animalsBorn.add(newBorn);
                    this.statsMaker.addChild(newBorn);
                }
            }
        }
        for (Animal animal : animalsBorn) {
            map.place(animal);
        }
    }

    public Animal getStrongestAnimalAt(int x, int y) {
        return this.map.getStrongestAnimalAt(new Vector2d(x, y));
    }

    @Override
    public String toString() {
        return this.map.toString();
    }

    public void writeStats(boolean firstSimulation) {
        if (firstSimulation) {
            JsonWriter.writeJson(this.statsMaker, "simulationStats");
        } else {
            JsonWriter.writeJson(this.statsMaker, "simulation2Stats");
        }
    }

    public String getStats() {
        return this.statsMaker.toString();
    }
}