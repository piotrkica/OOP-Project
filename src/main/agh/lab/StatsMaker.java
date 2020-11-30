package agh.lab;

import java.util.ArrayList;
import java.util.List;

public class StatsMaker {
    private int animalsAliveCount;
    private int grassTilesCount;
    private int totalEnergy;
    private int deadAnimalsCount;
    private int totalDeadLifeSpan;
    private int totalChildrenOfAlive;
    private List<Integer> childrenCountOnNthDay = new ArrayList<>();

    public void dayPassed(){
        this.childrenCountOnNthDay.add(totalChildrenOfAlive);
    }

    public void animalMoved(int moveEnergyCost){
        this.totalEnergy-=moveEnergyCost;
    }

    public void addAnimal(int energy) {
        this.animalsAliveCount++;
        this.totalEnergy += energy;
    }

    public void removeAnimal(int lifeSpan, int childrenCount) {
        this.animalsAliveCount--;
        this.deadAnimalsCount++;
        this.totalDeadLifeSpan += lifeSpan;
        this.totalChildrenOfAlive -= childrenCount;
    }

    public void addGrass() {
        this.grassTilesCount++;
    }

    public void removeGrass() {
        this.grassTilesCount--;
    }

    public void addEnergy(int addedEnergy){
        this.totalEnergy+=addedEnergy;
    }

    public void addChild() {
        totalChildrenOfAlive++;
    }

    public double getAvgEnergy(){
        return 1.0*totalEnergy/ animalsAliveCount;
    }

    public double getAvgDeadLifeSpan(){
        return 1.0*totalDeadLifeSpan/ deadAnimalsCount;
    }

    public double getAvgChildrenCount(){
        return 1.0*totalChildrenOfAlive/ animalsAliveCount;
    }

}
