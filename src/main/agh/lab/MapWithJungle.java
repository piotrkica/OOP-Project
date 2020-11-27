package agh.lab;

import java.util.*;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import static java.lang.Math.*;

public class MapWithJungle implements IWorldMap, IPositionChangeObserver{
    private final MapVisualizer mapVis = new MapVisualizer(this);
    private final Vector2d bottomLeftMap;
    private final Vector2d topRightMap;
    private final Vector2d bottomLeftJungle;
    private final Vector2d topRightJungle;
    private final Multimap<Vector2d, Animal> animalsMM = ArrayListMultimap.create();
    private final Map<Vector2d, Grass> grassTilesHM = new HashMap<>();
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

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(bottomLeftMap) && position.precedes(topRightMap);
    }

    @Override
    public Object objectAt(Vector2d position) {
        if (animalsMM.get(position).size() == 0){
            return grassTilesHM.get(position);
        }
        return animalsMM.get(position);
    }

    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition) {
        animalsMM.remove(oldPosition, animal);
        animalsMM.put(animal.getPosition(), animal);

    }

    public boolean isInJungle(Vector2d position){
        return position.follows(bottomLeftJungle) && position.precedes(topRightJungle);
    }

    public Vector2d findFreeTile(Vector2d bottomLeft, Vector2d topRight){// potrzebny fix by się nie zapętlił jak nie ma wolnych miejsc
        Vector2d position;
        do{
            position = new Vector2d(rand.nextInt(topRight.x - bottomLeft.x + 1) + bottomLeft.x
                    ,rand.nextInt(topRight.y - bottomLeft.y + 1) + bottomLeft.y);
        } while (this.isOccupied(position));
        return position;
    }

    public void placeStartingAnimals(int animalsNo){
        for (int i = 0; i < animalsNo; i++){
            Vector2d position = findFreeTile(bottomLeftMap,topRightMap);
            place(new Animal(this, position));
        }
    }

    public void placeGrassInJungle(){
        Vector2d position;
        position = findFreeTile(bottomLeftJungle, topRightJungle);
        grassTilesHM.put(position, new Grass(position));
    }

    public void placeGrassOutsideJungle(){
        Vector2d position;
        do {
            position = findFreeTile(bottomLeftMap,topRightMap);
        } while(isInJungle(position));

        grassTilesHM.put(position, new Grass(position));
    }

    public Vector2d findFreePositionForChild(Vector2d parentsPosition){ // na obecną chwile przeszukuje w promieniu 2 pól, może znaleźć miejsc po drugiej stronie mapy
        for(int i = -1; i < 1; i++){
            for(int j = -1; j < 1; j++){
                Vector2d possiblePosition = repositionIfOutOfBounds(new Vector2d(i, j));
                if (!isOccupied(possiblePosition)){
                    return possiblePosition;
                }
            }
        }
        return new Vector2d(rand.nextInt(3)+parentsPosition.x - 1, rand.nextInt(3)+parentsPosition.y - 1);
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

    public Multimap<Vector2d, Animal> getAnimalsMM() {
        return animalsMM;
    }

    public Map<Vector2d, Grass> getGrassTilesHM(){
        return grassTilesHM;
    }





}
