package agh.lab;

import java.lang.reflect.Array;
import java.util.*;

import static agh.lab.SimulationEngine.startingEnergy;
import static agh.lab.MapDirection.MAP_DIRS_INDEXED;

public class Animal implements IMapElement {
    private final MapWithJungle map;
    private final List<IPositionChangeObserver> observers = new ArrayList<>();
    private MapDirection orientation = MapDirection.NORTH;
    private Vector2d position;
    private int energy;
    private int daysAlive = 0;
    private int childrenNo = 0;
    private List<Integer> genes = new ArrayList<>();
    private final int[] genesSum = new int[8];
    private final Random rand = new Random();

    public Animal(MapWithJungle map, Vector2d initialPosition) {
        this.map = map;
        this.position = initialPosition;
        this.energy = startingEnergy;
        for (int i = 0; i < 32; i++){
            int gene = rand.nextInt(8);
            genes.add(gene);
            genesSum[gene]++;
        }
        addMissingMovesAndSort();
    }

    public Animal(MapWithJungle map, Vector2d initialPosition, List<Integer> initialGenes, int energy) {
        this.map = map;
        this.position = initialPosition;
        this.genes = initialGenes;
        for (Integer gene : genes){
            genesSum[gene]++;
        }
        addMissingMovesAndSort();
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

    private void addMissingMovesAndSort(){ // to check if all moves are available
        List<Integer> moreThan2Genes = new ArrayList<>();
        for (int i = 0; i < 8;i++){
            if (genesSum[i] >= 2){
                moreThan2Genes.add(i);
            }
        }
        //System.out.println("moreThan2Genes:"+moreThan2Genes.size());
        //System.out.println("genesSum:"+genesSum.length);

        for (int i = 0; i < 8;i++){
            if (genesSum[i] == 0){
                //System.out.println("missingGene:"+i);
                //System.out.println(genes);

                int geneToSubstitute = moreThan2Genes.get(rand.nextInt(moreThan2Genes.size()));
                System.out.println("genpodmiany:"+geneToSubstitute);
                this.genesSum[geneToSubstitute]-=1;
                if (this.genesSum[geneToSubstitute] == 1){
                    moreThan2Genes.remove(geneToSubstitute);
                }
                genes.set(genes.indexOf(geneToSubstitute), i);
            }
        }
        Collections.sort(genes);
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public MapDirection getOrientation() {
        return this.orientation;
    }

    public MapDirection chooseNextMove(){
        int moveDir = genes.get(rand.nextInt(32));
        return MAP_DIRS_INDEXED[moveDir];
    }

    public void move() {
        Vector2d oldPosition = position;
        energy--;
        daysAlive++;
        MapDirection direction = chooseNextMove();
        Vector2d newPosition = this.position.add(direction.toUnitVector());
        this.position = this.map.repositionIfOutOfBounds(newPosition);
        positionChanged(oldPosition);
        this.orientation = direction;

    }

    public void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
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

    public void increaseChildNo(){
        this.childrenNo++;
    }

    public List<Integer> getGenes(){
        return this.genes;
    }

}
