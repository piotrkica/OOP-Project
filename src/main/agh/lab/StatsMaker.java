package agh.lab;

import static agh.lab.SimulationEngine.startingEnergy;

public class StatsMaker {
    public int animalsNo;
    public int grassTilesNo;
    public int totalEnergy;
    public int totalDeadAnimalsNo;
    public int totalDeadLifeSpan;
    public int totalChildrenOfAlive;

    public StatsMaker(int startingAnimalsNo, int startingGrassTilesNo) {
        this.animalsNo = startingAnimalsNo;
        this.grassTilesNo = startingGrassTilesNo;
        this.totalEnergy += startingAnimalsNo * startingEnergy;
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
