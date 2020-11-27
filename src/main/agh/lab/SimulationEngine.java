package agh.lab;

import com.google.common.collect.Multimap;

import java.util.*;

import static java.lang.Math.max;

public class SimulationEngine implements IEngine {
    private final int days;
    private final MapWithJungle map;
    public static int startingEnergy = 15;
    public static int grassEnergyValue = 10;
    private final List<Animal> deadAnimals = new ArrayList<>();


    public SimulationEngine(int days, MapWithJungle map) {
        this.days = days;
        this.map = map;
    }

    public void run() {
        for (int i = 0; i < days;i++) {
            System.out.println(map);
            removeDead();
            moveAnimals();
            eatGrass();
            reproduceIfPossible();
            placeGrassInJungleAndOutside();
        }
        System.out.println(map);
    }

    public void moveAnimals(){
        Collection<Animal> animalCollection = map.getAnimalsMM().values();
        Animal[] animals = new Animal[animalCollection.size()];
        animals = animalCollection.toArray(animals);
        for (Animal animal : animals) {
            animal.move();
        }
        for (Animal animal : animals) {
            System.out.println(animal.getEnergy());
        }
    }

    public void placeGrassInJungleAndOutside(){
        this.map.placeGrassInJungle();
        this.map.placeGrassOutsideJungle();
    }

    public List<Animal> getStrongestAnimals(Vector2d position){
        Multimap<Vector2d, Animal> animalsMM = map.getAnimalsMM();
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

    public void eatGrass(){
        Multimap<Vector2d, Animal> animalsMM = map.getAnimalsMM();
        Map<Vector2d, Grass> grassTilesHM = map.getGrassTilesHM();
        Vector2d[] grassTiles = grassTilesHM.keySet().toArray(new Vector2d[0]);

        for(Vector2d grassPosition : grassTiles){
            if (animalsMM.get(grassPosition).size() >= 1){
                grassTilesHM.remove(grassPosition);
                List<Animal> strongestAnimals = getStrongestAnimals(grassPosition);
                int grassEnergyValueSplit = grassEnergyValue/strongestAnimals.size();
                for(Animal animal : strongestAnimals){
                    animal.addEnergy(grassEnergyValueSplit);
                }
            }
        }
    }

    public void removeDead() {
        Multimap<Vector2d, Animal> animalsMM = map.getAnimalsMM();
        Collection<Animal> animalCollection = animalsMM.values();
        Animal[] animals = new Animal[animalCollection.size()];
        animals = animalCollection.toArray(animals);

        for(Animal animal : animals){
            if(animal.getEnergy() <= 0){
                deadAnimals.add(animal);
                animal.removeAllObservers();
                animalsMM.remove(animal.getPosition(), animal);
            }
        }
    }

    public void reproduceIfPossible(){
        Multimap<Vector2d, Animal> animalsMM = map.getAnimalsMM();

        Vector2d[] animalPositions = animalsMM.keySet().toArray(new Vector2d[0]);
        for (Vector2d position : animalPositions){
            if (animalsMM.get(position).size() >= 2){
                Animal strongest1 = null;
                Animal strongest2 = null;
                for (Animal animal : animalsMM.get(position)){
                    if (strongest1 == null){
                        strongest1 = animal;
                    }
                    else if (strongest2 == null){
                        if (strongest1.getEnergy() > animal.getEnergy()){
                            strongest2 = animal;
                        }
                        else{
                            strongest2 = strongest1;
                            strongest1 = animal;
                        }
                    }
                    else if (strongest1.getEnergy() < animal.getEnergy()){
                        strongest2 = strongest1;
                        strongest1 = animal;
                    }
                    else if (strongest2.getEnergy() < animal.getEnergy()){
                        strongest2 = animal;
                    }
                }
                if (strongest1.getEnergy() > startingEnergy/2 && strongest2.getEnergy() > startingEnergy/2){
                    int childEnergy = strongest1.getEnergy()/2 + strongest2.getEnergy()/2;
                    strongest1.setEnergy(strongest1.getEnergy()/2);
                    strongest2.setEnergy(strongest2.getEnergy()/2);
                    strongest1.increaseChildNo();
                    map.place(new Animal(this.map, this.map.findFreePositionForChild(position), strongest1.getGenes(),strongest2.getGenes(), childEnergy));
                }
            }
        }
    }

}