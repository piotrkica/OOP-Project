package agh.lab;

import java.util.*;

public class StatsMaker {
    private int day = 0;
    private int aliveAnimalsCount = 0;
    private int grassTilesCount = 0;
    private int totalEnergy = 0;
    private int deadAnimalsCount = 0;
    private int totalDeadLifeSpan = 0;
    private int totalChildrenOfAlive = 0;
    private final Map<Genes, Integer> genesCounter = new HashMap<>();
    private final Map<Genes, Integer> dominantCounter = new HashMap<>();
    private double sumAvgAliveAnimalsCount = 0;
    private double sumAvgGrassTilesCount = 0;
    private double sumAvgEnergyCount = 0;
    private double sumAvgDeadAnimalsLifeSpan = 0;
    private double sumAvgChildrenCount = 0;

    public void dayPassed() {
        this.day++;
        Genes dominantGene = this.getDominantGene();
        if (dominantCounter.containsKey(dominantGene)) {
            dominantCounter.replace(dominantGene, dominantCounter.get(dominantGene) + 1);
            return;
        }
        dominantCounter.put(dominantGene, 1);
        updateStatsFromWholeSimulation();
    }

    public void animalMoved(int moveEnergyCost) {
        this.totalEnergy -= moveEnergyCost;
    }

    public void addAnimal(Animal animal) {
        this.aliveAnimalsCount++;
        this.totalEnergy += animal.getEnergy();
        addGene(animal.getGenes());
    }

    public void removeAnimal(int lifeSpan, int childrenCount) {
        this.aliveAnimalsCount--;
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

    public void addEnergy(int addedEnergy) {
        this.totalEnergy += addedEnergy;
    }

    public void addChild(Animal animal) {
        this.aliveAnimalsCount++;
        this.totalChildrenOfAlive += 2;
        addGene(animal.getGenes());
    }

    public void addGene(Genes genes) {
        if (genesCounter.containsKey(genes)) {
            genesCounter.replace(genes, genesCounter.get(genes) + 1);
            return;
        }
        genesCounter.put(genes, 1);
    }

    public void removeGene(Genes genes) {
        if (genesCounter.containsKey(genes)) {
            genesCounter.replace(genes, genesCounter.get(genes) - 1);
        }
        if (genesCounter.get(genes) == 0) {
            genesCounter.remove(genes);
        }
    }

    public int getDay() {
        return this.day;
    }

    public Genes getDominantGene() {
        Iterator genes = genesCounter.keySet().iterator();
        if (!genes.hasNext()) {
            return null;
        }
        Genes dominantGene = (Genes) genes.next();
        while (genes.hasNext()) {
            Genes nextGene = (Genes) genes.next();
            if (genesCounter.get(nextGene) > genesCounter.get(dominantGene)) {
                dominantGene = nextGene;
            }
        }
        return dominantGene;
    }

    public String getDominantGeneOverTime() {
        Iterator genes = dominantCounter.keySet().iterator();
        if (!genes.hasNext()) {
            return "There is no dominant gene yet";
        }
        Genes dominantGeneOverTime = (Genes) genes.next();
        while (genes.hasNext()) {
            Genes nextGene = (Genes) genes.next();
            if (dominantCounter.get(nextGene) > dominantCounter.get(dominantGeneOverTime)) {
                dominantGeneOverTime = nextGene;
            }
        }
        return dominantGeneOverTime.toString();
    }

    public double getAvgAnimalEnergy() {
        if (aliveAnimalsCount != 0) {
            return (double) Math.round(100 * (double) this.totalEnergy / aliveAnimalsCount) / 100;
        }
        return 0;
    }

    public double getAvgDeadAnimalsLifeSpan() {
        if (deadAnimalsCount != 0) {
            return (double) Math.round(100 * (double) this.totalDeadLifeSpan / this.deadAnimalsCount) / 100;
        }
        return 0;
    }

    public double getAvgChildrenCount() {
        if (aliveAnimalsCount != 0) {
            return (double) Math.round(100 * (double) this.totalChildrenOfAlive / this.aliveAnimalsCount) / 100;
        }
        return 0;
    }

    private void updateStatsFromWholeSimulation() {
        this.sumAvgAliveAnimalsCount += this.aliveAnimalsCount;
        this.sumAvgGrassTilesCount += this.grassTilesCount;
        this.sumAvgEnergyCount += this.getAvgAnimalEnergy();
        this.sumAvgDeadAnimalsLifeSpan += this.getAvgDeadAnimalsLifeSpan();
        this.sumAvgChildrenCount += this.getAvgChildrenCount();
    }

    public double getAvgAliveAnimalsCountOverTime() {
        return (double) Math.round(100 * this.sumAvgAliveAnimalsCount / this.day) / 100;
    }

    public double getAvgGrassTilesCountOverTime() {
        return (double) Math.round(100 * this.sumAvgGrassTilesCount / this.day) / 100;
    }

    public double getAvgEnergyCountOverTime() {
        return (double) Math.round(100 * this.sumAvgEnergyCount / this.day) / 100;
    }

    public double getAvgDeadAnimalsLifeSpanOverTime() {
        return (double) Math.round(100 * this.sumAvgDeadAnimalsLifeSpan / this.day) / 100;
    }

    public double getAvgChildrenCountOverTime() {
        return (double) Math.round(100 * this.sumAvgChildrenCount / this.day) / 100;
    }


    @Override
    public String toString() {
        String stats = "";
        stats += "Day : " + this.day + "\n";
        stats += "Number of alive animals: " + this.aliveAnimalsCount + "\n";
        stats += "Number of grass tiles: " + this.grassTilesCount + "\n";
        stats += "Longest dominant genotype: " + this.getDominantGeneOverTime() + "\n";
        stats += "Average animal energy: " + this.getAvgAnimalEnergy() + "\n";
        if (deadAnimalsCount > 0) {
            stats += "Average dead animal life span: " + this.getAvgDeadAnimalsLifeSpan() + "\n";
        } else {
            stats += "Average dead animal life span: No one died yet" + "\n";
        }
        stats += "Average value of children for animal: " + this.getAvgChildrenCount() + "\n";
        return stats;
    }

}
