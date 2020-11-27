package agh.lab;

import java.util.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import static agh.lab.SimulationEngine.grassEnergyValue;
import static agh.lab.SimulationEngine.startingEnergy;
import static java.lang.Math.*;

public class MapWithJungle implements IWorldMap, IPositionChangeObserver{
    private final MapVisualizer mapVis = new MapVisualizer(this);
    private final Vector2d bottomLeftMap;
    private final Vector2d topRightMap;
    private final Vector2d bottomLeftJungle;
    private final Vector2d topRightJungle;
    private final Multimap<Vector2d, Animal> animalsMM = ArrayListMultimap.create();
    private final Map<Vector2d, Grass> grassTilesHM = new HashMap<>();
    private final List<Animal> deadAnimals = new ArrayList<>();
    private final Random rand = new Random();

    public MapWithJungle(int width, int height, double jungleRatio, int grassTilesStartNo, int animalsNo) {
        this.bottomLeftMap = new Vector2d(0, 0);
        this.topRightMap = new Vector2d(width - 1, height - 1);

        int jungleWidth = (int) floor(width * jungleRatio);
        int jungleHeight = (int) floor(height * jungleRatio);
        this.bottomLeftJungle = new Vector2d( (width - jungleWidth)/2, (height - jungleHeight)/2);
        this.topRightJungle = new Vector2d( (width + jungleWidth)/2, (height + jungleHeight)/2);

        System.out.println(bottomLeftJungle);
        System.out.println(topRightJungle);

        placeStartingAnimals(animalsNo);
        for(int i = 0; i < floorDiv(grassTilesStartNo, 2); i++){
            placeGrassInJungle();
        }
        for(int i = 0; i < ceil(grassTilesStartNo/2.0); i++){
            placeGrassOutsideJungle();
        }
    }

    public String toString() {
        return mapVis.draw(bottomLeftMap, topRightMap);
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return this.objectAt(position) != null;
    }

    @Override
    public boolean place(Animal animal) {
        if (!canMoveTo(animal.getPosition())) {
            throw new IllegalArgumentException(animal.getPosition() + " field is out of bounds");
        }
        animalsMM.put(animal.getPosition(), animal);
        animal.addObserver(this);
        return true;
    }

    public void placeStartingAnimals(int animalsNo){
        for (int i = 0; i < animalsNo; i++){
            Vector2d position = findFreeTile(bottomLeftMap,topRightMap);
            place(new Animal(this, position));
        }
    }

    public Vector2d findFreeTile(Vector2d bottomLeft, Vector2d topRight){// potrzebny fix by się nie zapętlił jak nie ma wolnych miejsc
        Vector2d position;
        do{
            position = new Vector2d(rand.nextInt(topRight.x - bottomLeft.x + 1) + bottomLeft.x
                    ,rand.nextInt(topRight.y - bottomLeft.y + 1) + bottomLeft.y);
        } while (this.isOccupied(position));
        return position;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(bottomLeftMap) && position.precedes(topRightMap);
    }

    public Vector2d repositionIfOutOfBounds(Vector2d position){
        if (!canMoveTo(position)){
            int x = position.x;
            int y = position.y;
            if (position.x > topRightMap.x){
                x = bottomLeftMap.x;
            }
            else if (position.x < bottomLeftMap.x){
                x = topRightMap.x;
            }
            if (position.y > topRightMap.y){
                y = bottomLeftMap.y;
            }
            else if (position.y < bottomLeftMap.y){
                y = topRightMap.y;
            }
            position = new Vector2d(x,y);
        }
        return position;
    }

    @Override
    public Object objectAt(Vector2d position) {
        if (animalsMM.get(position).size() == 0){
            return grassTilesHM.get(position);
        }
        return animalsMM.get(position);
    }

    public Multimap<Vector2d, Animal> getAnimalsMM() {
        return animalsMM;
    }

    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition) {
        animalsMM.remove(oldPosition, animal);
        animalsMM.put(animal.getPosition(), animal);

    }

    public boolean isInJungle(Vector2d position){
        return position.follows(bottomLeftJungle) && position.precedes(topRightJungle);
    }

    public void placeGrassOutsideJungle(){
        Vector2d position;
        do {
            position = findFreeTile(bottomLeftMap,topRightMap);
        } while(isInJungle(position));

        grassTilesHM.put(position, new Grass(position));
    }

    public void placeGrassInJungle(){
        Vector2d position;
        position = findFreeTile(bottomLeftJungle, topRightJungle);
        grassTilesHM.put(position, new Grass(position));
    }

    public List<Animal> getStrongestAnimals(Vector2d position){
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
        Vector2d[] grassTiles = grassTilesHM.keySet().toArray(new Vector2d[0]);
        for(Vector2d grassPosition : grassTiles){
            if (animalsMM.get(grassPosition).size() >= 1){
                grassTilesHM.remove(grassPosition);
                List<Animal> strongestAnimals = getStrongestAnimals(grassPosition);
                int grassEnergyValueSplit = floorDiv(grassEnergyValue, strongestAnimals.size());
                for(Animal animal : strongestAnimals){
                    animal.addEnergy(grassEnergyValueSplit);
                }
            }
        }
    }

    public void removeDead() {
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

    public Vector2d findFreePositionForChild(Vector2d parentsPosition){ // na obecną chwile przeszukuje w promieniu 2 pól, może znaleźć miejsc po drugiej stronie mapy
        for(int r = 1; r < 2; r++){
            for(int i = -r; i < r; i++){
                for(int j = -r; j < r; j++){
                    Vector2d possiblePosition = repositionIfOutOfBounds(new Vector2d(i, j));
                    if (!isOccupied(possiblePosition)){
                        return possiblePosition;
                    }
                }
            }
        }
        return parentsPosition;
    }

    public void reproduceIfPossible(){
        Vector2d[] animalPositions = animalsMM.keySet().toArray(new Vector2d[0]); //animalsMM.keySet().size()
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
                    place(new Animal(this, findFreePositionForChild(position), strongest1.getGenes(),strongest2.getGenes(), childEnergy));
                }
            }
        }
    }
}
