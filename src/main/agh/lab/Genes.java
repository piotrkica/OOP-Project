package agh.lab;

import java.util.*;

import static agh.lab.MapDirection.MAP_DIRS_INDEXED;

public class Genes {
    private int[] genes = new int[32];
    private final int[] genesSum = new int[8];
    private final Random rand = new Random();
    private String stringRepr = "";

    public Genes() {
        for (int i = 0; i < 32; i++) {
            genes[i] = rand.nextInt(8);
            genesSum[genes[i]]++;
        }
        addMissingGenesAndSort();
        for (int i = 0; i < 32; i++) {
            stringRepr += genes[i];
        }
    }

    public Genes(int[] strongerGenes, int[] weakerGenes) {
        int[] newGenes = Arrays.copyOf(strongerGenes, strongerGenes.length);
        Random rand = new Random();
        int firstCut = rand.nextInt(29) + 1;
        int secondCut = rand.nextInt(30 - firstCut) + firstCut;
        System.arraycopy(weakerGenes, firstCut, newGenes, firstCut, secondCut - firstCut);
        this.genes = newGenes;
        for (int i = 0; i < 32; i++) {
            genesSum[genes[i]]++;
        }
        addMissingGenesAndSort();
        for (int i = 0; i < 32; i++) {
            stringRepr += genes[i];
        }
    }

    private void addMissingGenesAndSort() {
        for (int i = 0; i < 8; i++) {
            if (genesSum[i] == 0) {
                int geneToSubstitute;
                do {
                    geneToSubstitute = rand.nextInt(8);

                } while (genesSum[geneToSubstitute] >= 2);
                this.genesSum[geneToSubstitute] -= 1;
                for (int j = 0; j < genes.length; j++) {
                    if (genes[j] == geneToSubstitute) {
                        genes[j] = i;
                        this.genesSum[i]++;
                        break;
                    }
                }
            }
        }
        Arrays.sort(genes);
    }

    public MapDirection rotateAnimal(MapDirection direction) {
        int currentIndex = MapDirection.getIndex(direction);
        return MAP_DIRS_INDEXED[(currentIndex + genes[rand.nextInt(32)]) % MAP_DIRS_INDEXED.length];
    }

    public int[] getGenesArray() {
        return this.genes;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.stringRepr);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Genes)) {
            return false;
        }
        Genes otherGenes = (Genes) other;
        return this.stringRepr.equals(otherGenes.stringRepr);
    }

    @Override
    public String toString() {
        return this.stringRepr;
    }


}
