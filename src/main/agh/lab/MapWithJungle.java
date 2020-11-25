package agh.lab;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
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

    public MapWithJungle(int width, int height, double jungleRatio, int grassTilesStartNo) {
        this.bottomLeftMap = new Vector2d(0, 0);
        this.topRightMap = new Vector2d(width - 1, height - 1);

        double jungleWidth = floor(width * jungleRatio);
        double jungleHeight = floor(height * jungleRatio);
        this.bottomLeftJungle = new Vector2d((int) floor((width - jungleWidth)/2), (int) floor((height - jungleHeight)/2));
        this.topRightJungle = new Vector2d((int) floor((width + jungleWidth)/2), (int) floor((height + jungleHeight)/2));
        System.out.println(bottomLeftJungle);
        System.out.println(topRightJungle);

        for(int i = 0; i < grassTilesStartNo; i++){
            placeGrassInJungleAndOutside();
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
            throw new IllegalArgumentException(animal.getPosition() + " field is occupied");
        }
        animalsMM.put(animal.getPosition(), animal);
        animal.addObserver(this);
        return true;
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

    public void placeGrassInJungleAndOutside(){ // potrzebny fix by się nie zapętlił jak nie ma wolnych miejsc
        Random rand = new Random();
        Vector2d position;

        do {
            position = new Vector2d(rand.nextInt(topRightMap.x - bottomLeftMap.x) + bottomLeftMap.x
                                    ,rand.nextInt(topRightMap.y - bottomLeftMap.y) + bottomLeftMap.y);

        } while (this.isOccupied(position) || isInJungle(position));

        grassTilesHM.put(position, new Grass(position));

        if (topRightJungle.equals(bottomLeftJungle) && objectAt(topRightJungle) == null){
            grassTilesHM.put(position, new Grass(position));
            return;
        }
        do {
            position = new Vector2d(rand.nextInt(topRightJungle.x - bottomLeftJungle.x) + bottomLeftJungle.x
                    ,rand.nextInt(topRightJungle.y - bottomLeftJungle.y) + bottomLeftJungle.y);
        } while (this.isOccupied(position));

        grassTilesHM.put(position, new Grass(position));
    }




}
