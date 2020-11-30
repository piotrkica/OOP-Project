package agh.lab;

import com.google.common.collect.Multimap;

import java.util.*;

import static java.lang.Math.ceil;
import static java.lang.Math.max;

public class SimulationEngine implements IEngine {
    private int days = 0;
    private final MapWithJungle map;
    public int startingEnergy;
    public int grassEnergyValue;
    public int moveEnergyCost;
    private final List<Animal> deadAnimals = new ArrayList<>();
    private final StatsMaker statsMaker = new StatsMaker();

    public SimulationEngine(int width, int height, float jungleRatio, int startingAnimals, int startingGrassTiles, int startingEnergy, int grassEnergyValue, int moveEnergyCost) {
        this.map = new MapWithJungle(width, height, jungleRatio);
        this.startingEnergy = startingEnergy;
        this.grassEnergyValue = grassEnergyValue;
        this.moveEnergyCost = moveEnergyCost;
        placeStartingAnimals(startingAnimals);
        placeStartingGrass(startingGrassTiles);
    }

    public void run() {
        for (int i = 0; i < 50;i++) {
            this.days++;
            System.out.println("Day: "+ this.days);
            System.out.println(map);
            removeDeadAnimals();
            moveAnimals();
            eatGrass();
            reproduceIfPossible();
            placeGrassInJungleAndOutside();
        }
        System.out.println(map);
    }

    public void placeStartingAnimals(int animalsNo){
        for (int i = 0; i < animalsNo; i++){
            Vector2d position = this.map.findFreeTile(this.map.getBottomLeft(),this.map.getTopRight());
            this.map.place(new Animal(this.map, position, startingEnergy));
            this.statsMaker.addAnimal(startingEnergy);
        }
    }

    public void placeStartingGrass(int startingGrassTiles){
        for(int i = 0; i < startingGrassTiles/2; i++){
            this.map.placeGrassInJungle();
            this.statsMaker.addGrass();
        }
        for(int i = 0; i < ceil(startingGrassTiles/2.0); i++){
            this.map.placeGrassOutsideJungle();
            this.statsMaker.addGrass();
        }
    }

    public void moveAnimals(){
        Collection<Animal> animalCollection = map.getAnimalsMM().values();
        Animal[] animals = new Animal[animalCollection.size()];
        animals = animalCollection.toArray(animals);
        for (Animal animal : animals) {
            animal.move(moveEnergyCost);
            System.out.println(animal.getEnergy());
            this.statsMaker.animalMoved(moveEnergyCost);
        }
    }

    public void placeGrassInJungleAndOutside(){
        this.map.placeGrassInJungle();
        this.statsMaker.addGrass();
        this.map.placeGrassOutsideJungle();
        this.statsMaker.addGrass();
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

        for(Animal animal : animals){
            if(animal.getEnergy() <= 0){
                deadAnimals.add(animal);
                animal.removeAllObservers();
                animalsMM.remove(animal.getPosition(), animal);
                this.statsMaker.removeAnimal(animal.getLifeSpan(), animal.getChildrenCount());
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
                    int childEnergy = strongest1.getEnergy()/4 + strongest2.getEnergy()/4;
                    strongest1.setEnergy(strongest1.getEnergy()/4);
                    strongest2.setEnergy(strongest2.getEnergy()/4);
                    strongest1.increaseChildrenCount();
                    map.place(new Animal(this.map, this.map.findFreePositionForChild(position), strongest1.getGenes(),strongest2.getGenes(), childEnergy));
                    this.statsMaker.addAnimal(childEnergy);
                    this.statsMaker.addChild();
                }
            }
        }
    }

}