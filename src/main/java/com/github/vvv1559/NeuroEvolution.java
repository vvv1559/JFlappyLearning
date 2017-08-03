package com.github.vvv1559;

import java.util.Arrays;

public class NeuroEvolution {
    private Generations generations = new Generations();
    private Network[] currentNetworks;

    /**
     * Produce next generation
     */
    void nextGeneration() {
        Generation[] generations = this.generations.getGenerations();
        Network[] networks = this.generations.nextGeneration();

        // Create Networks from the current Generation.
        Network[] nns = new Network[networks.length];
        for (int i = 0; i < networks.length; i++) {
            Network nn = new Network();
            nn.setSave(networks[i].getSave());
            nns[i] = nn;
        }

        if (Options.lowHistoric) {
            // Remove old Networks.
            if (generations.length >= 2) {
                Genome[] genomes = generations[generations.length - 2].getGenomes();
                for (Genome genome : genomes) {
                    genome.setNetwork(null);
                }
            }
        }

        if (Options.historic != -1) {
            // Remove older generations.
            if (generations.length > Options.historic + 1) {
                int newLen = generations.length - (Options.historic + 1);
                this.generations.setGenerations(Arrays.copyOf(generations, newLen));
            }
        }

        this.currentNetworks = nns;

    }

    /**
     * Adds a new Genome with specified Neural Network and score.
     *
     * @param networkIndex Played network index.
     * @param score        Score value.
     */
    void fixScore(int networkIndex, int score) {
        generations.addGenome(new Genome(currentNetworks[networkIndex], score));
    }
}
