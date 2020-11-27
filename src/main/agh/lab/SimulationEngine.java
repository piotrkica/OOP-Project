package agh.lab;

import java.util.Collection;

public class SimulationEngine implements IEngine {
    private final int days;
    private final MapWithJungle map;
    public static int startingEnergy = 15;
    public static int grassEnergyValue = 10;


    public SimulationEngine(int days, MapWithJungle map) {
        this.days = days;
        this.map = map;
    }

    public void run() {
        for (int i = 0; i < days;i++) {
            Collection<Animal> animalCollection = map.getAnimalsMM().values();
            Animal[] animals = new Animal[animalCollection.size()];
            animals = animalCollection.toArray(animals);
            int n = animals.length;

            System.out.println(map);

            this.map.removeDead();
            for (Animal animal : animals) {
                animal.move();
            }
            this.map.eatGrass();
            this.map.reproduceIfPossible();
            this.map.placeGrassInJungle();
            this.map.placeGrassOutsideJungle();
            for (Animal animal : animals) {
                System.out.println(animal.getEnergy());
            }
        }
        System.out.println(map);
    }
}