package com.github.vvv1559;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Composed of a set of Genomes.
 */
public class Generation {
    private Genome[] genomes;

    /**
     * Add a genome to the generation.
     *
     * @param genome Genome to add.
     */
    void addGenome(Genome genome) {
        // Locate position to insert Genome into.
        // The gnomes should remain sorted.
        int i = 0;
        for (; i < this.genomes.length; i++) {
            // Sort in descending order.
            if (Options.scoreSort < 0) {
                if (genome.getScore() > this.genomes[i].getScore()) {
                    break;
                }
                // Sort in ascending order.
            } else {
                if (genome.getScore() < this.genomes[i].getScore()) {
                    break;
                }
            }

        }

        // Insert genome into correct position.
//        this.genomes.splice(i, 0, genome);
        this.genomes = Arrays.copyOf(genomes, i + 1);
        this.genomes[i] = genome;
    }

    /**
     * Breed to genomes to produce offspring(s).
     *
     * @param g1 Genome 1.
     * @param g2 Genome 2.
     * @param nbChilds Number of offspring (children).
     */
    private Genome[] breed(Genome g1, Genome g2, int nbChilds) {
        List<Genome> datas = new LinkedList<>();
        for (int nb = 0; nb < nbChilds; nb++) {
            // Deep clone of genome 1.
//            var data = JSON.parse(JSON.stringify(g1));
            Genome data = g1;
            for (int i = 0; i < g2.getNetwork().getWeights().length; i++) {
                // Genetic crossover
                // 0.5 is the crossover factor.
                // FIXME Really should be a predefined constant.
                if (Math.random() <= 0.5) {
                    data.getNetwork().getWeights()[i] = g2.getNetwork().getWeights()[i];
                }
            }

            // Perform mutation on some weights.
            for (int i = 0; i < data.getNetwork().getWeights().length; i++) {
                if (Math.random() <= Options.mutationRate) {
                    data.getNetwork().getWeights()[i] += Math.random()
                        * Options.mutationRange
                        * 2
                        - Options.mutationRange;
                }
            }
            datas.add(data);
        }

        return datas.toArray(new Genome[datas.size()]);
    }

    /**
     * Generate the next generation.
     *
     */
    Network[] generateNextGeneration () {
        List<Network> nexts = new ArrayList<>();

        long generationCount = Math.round(Options.elitism * Options.population);
        for (int i = 0; i < generationCount; i++) {
            if (nexts.size() < Options.population) {
                // Push a deep copy of ith Genome's Nethwork.
//                nexts.push(JSON.parse(JSON.stringify(this.genomes[i].network)));
                nexts.add(this.genomes[i].getNetwork());
            }
        }

        long newGenomesCount = Math.round(Options.randomBehaviour * Options.population);
        for (int i = 0; i < newGenomesCount; i++) {
            Network n = this.genomes[0].getNetwork();
            for (int k = 0; k < n.getWeights().length; k++) {
                n.getWeights()[k] = Options.randomClamped();
            }

            if (nexts.size() < Options.population) {
                nexts.add(n);
            }
        }

        int max = 0;
        while (true) {
            for (int i = 0; i < max; i++) {
                // Create the children and push them to the nexts array.
                Genome[] childs = this.breed(this.genomes[i], this.genomes[max],
                    (Options.nbChild > 0 ? Options.nbChild : 1));
                for (Genome child : childs) {
                    nexts.add(child.getNetwork());
                    if (nexts.size() >= Options.population) {
                        // Return once number of children is equal to the
                        // population by generatino value.
                        return nexts.toArray(new Network[nexts.size()]);
                    }
                }
            }

            max++;
            if (max >= this.genomes.length - 1) {
                max = 0;
            }
        }
    }

    public Genome[] getGenomes() {
        return genomes;
    }
}
