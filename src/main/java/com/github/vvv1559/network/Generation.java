package com.github.vvv1559.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Composed of a set of Genomes.
 */
class Generation {
    private final Genome[] genomes;

    Generation() {
        genomes = new Genome[Options.population];
        for (int i = 0; i < Options.population; i++) {

            genomes[i] = new Genome();
        }
    }

    private Generation(Genome[] genomes) {
        this.genomes = genomes;
    }

    /**
     * Generate the next generation.
     */
    static Generation generateNextGeneration(Generation prevGeneration) {
        /* SELECTION STAGE. */

        // Sort generations by score in descending order and take top-scored.
        Arrays.sort(prevGeneration.genomes, Collections.reverseOrder(Genome.genomeComparator()));

        List<Genome> result = new ArrayList<>();

        int topScoredCount = (int) Math.round(Options.elitism * Options.population);
        for (int i = 0; i < topScoredCount; i++) {
            result.add(new Genome(prevGeneration.genomes[i]));
        }

        //Add some copies of top genome
        Genome topGenome = prevGeneration.genomes[0];
        long newGenomesCount = Math.round(Options.randomBehaviour * Options.population);
        for (int i = 0; i < newGenomesCount && result.size() < Options.population; i++) {
            result.add(new Genome(topGenome));
        }

        //Apply combination
        int max = 0;
        while (true) {
            boolean breaket = false;
            for (int i = 0; i < max; i++) {
                // Create the children and push them to the result array.
                //And mutation
                Genome[] childs = prevGeneration.genomes[i].breed(prevGeneration.genomes[max],
                    (Options.nbChild > 0 ? Options.nbChild : 1));
                for (Genome child : childs) {
                    result.add(child);
                    if (result.size() >= Options.population) {
                        // Return once number of children is equal to the
                        // population by generatino value.
                        breaket = true;
                        break;
                    }
                }
                if (breaket) {
                    break;
                }
            }
            if (breaket) {
                break;
            }

            max++;
            if (max >= prevGeneration.genomes.length - 1) {
                max = 0;
            }
        }

        Genome[] genomes = new Genome[result.size()];
        for (int i = 0; i < result.size(); i++) {
            genomes[i] = new Genome(result.get(i));
        }

        return new Generation(genomes);
    }

    void fixScore(int index, int score) {
        this.genomes[index].setScore(score);
    }

    int genomesCount() {
        return genomes.length;
    }

    double[] compute(int genomeIndex, double[] values) {
        return genomes[genomeIndex].compute(values);
    }
}
