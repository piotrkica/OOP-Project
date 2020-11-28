package agh.lab;

import java.util.ArrayList;
import java.util.List;

public class StatsMaker {
    private int animalsNo;
    private int grassTilesNo;
    private int totalEnergy;
    private int totalDeadAnimalsNo;
    private int totalDeadLifeSpan;
    private int totalChildrenOfAlive;
    private List<Integer> childrenNoOnNthDay = new ArrayList<>();

    public StatsMaker(int startingAnimalsNo, int startingGrassTilesNo, int startingEnergy) {
        this.animalsNo = startingAnimalsNo;
        this.grassTilesNo = startingGrassTilesNo;
        this.totalEnergy += startingAnimalsNo * startingEnergy;
    }

    public void dayPassed(){
        this.childrenNoOnNthDay.add(totalChildrenOfAlive);
    }

    public void animalMoved(){
        this.totalEnergy--;
    }

    public void childWasBorn(){
        this.totalChildrenOfAlive++;
    }

    public void addAnimal(int energy) {
        this.animalsNo++;
        this.totalEnergy += energy;
    }

    public void removeAnimal(int lifeSpan, int childrenNo) {
        this.animalsNo--;
        this.totalDeadAnimalsNo++;
        this.totalDeadLifeSpan += lifeSpan;
        this.totalChildrenOfAlive -= childrenNo;
    }

    public void addGrass() {
        this.grassTilesNo++;
    }

    public void removeGrass() {
        this.grassTilesNo--;
    }

    public void addChild() {
        totalChildrenOfAlive--;
    }

    public double getAvgEnergy(){
        return 1.0*totalEnergy/animalsNo;
    }

    public double getAvgDeadLifeSpan(){
        return 1.0*totalDeadLifeSpan/totalDeadAnimalsNo;
    }

    public double getAvgChildrenNo(){
        return 1.0*totalChildrenOfAlive/animalsNo;
    }

}
