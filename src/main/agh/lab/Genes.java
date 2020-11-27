package agh.lab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static agh.lab.MapDirection.MAP_DIRS_INDEXED;

public class Genes {
    private List<Integer> genes = new ArrayList<>();
    private final int[] genesSum = new int[8];
    private final Random rand = new Random();

    public Genes(){
        for (int i = 0; i < 32; i++){
            genes.add(rand.nextInt(8));
        }
        for (int i = 0; i < 32; i++){
            genesSum[genes.get(i)]++;
        }
        addMissingGenesAndSort();
    }

    public Genes(List<Integer> strongerGenes, List<Integer> weakerGenes){
        List<Integer> newGenes = new ArrayList<>(strongerGenes);
        Random rand = new Random();
        int firstCut = rand.nextInt(31) + 1;
        int secondCut = rand.nextInt(31) + 1;
        while (secondCut == firstCut){
            firstCut = rand.nextInt(32);
        }
        if (firstCut > secondCut){
            int tmp = firstCut;
            firstCut = secondCut;
            secondCut = tmp;
        }
        for (int i = firstCut; i < secondCut; i++){
            newGenes.set(i, weakerGenes.get(i));
        }
        System.out.println("cuts:" + firstCut + " " + secondCut);
        System.out.println(strongerGenes);
        System.out.println(weakerGenes);
        System.out.println(newGenes);
        this.genes = newGenes;

        for (int i = 0; i < 32; i++){
            genesSum[genes.get(i)]++;
        }
        addMissingGenesAndSort();

    }

    private void addMissingGenesAndSort(){ // method to check if all moves are available
        List<Integer> moreThan2Genes = new ArrayList<>();
        for (int i = 0; i < 8;i++){
            if (genesSum[i] >= 2){
                moreThan2Genes.add(i);
            }
        }
        for (int i = 0; i < 8;i++){
            if (genesSum[i] == 0){
                int geneToSubstitute = moreThan2Genes.get(rand.nextInt(moreThan2Genes.size()));
                System.out.println("genpodmiany:"+geneToSubstitute);
                this.genesSum[geneToSubstitute]-=1;
                if (this.genesSum[geneToSubstitute] == 1){
                    moreThan2Genes.remove(geneToSubstitute);
                }
                genes.set(genes.indexOf(geneToSubstitute), i);
                this.genesSum[i]++;
            }
        }
        Collections.sort(genes);
    }

    public MapDirection chooseNextMove(){
        return MAP_DIRS_INDEXED[genes.get(rand.nextInt(32))];
    }

    public List<Integer> getGenes(){
        return this.genes;
    }

}
