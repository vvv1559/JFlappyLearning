package com.github.vvv1559;

import java.util.Arrays;

public class NeuroEvolution {
    private Generations generations = new Generations();

    /**
     * Reset and create a new Generations object.
     */
    void restart() {
        generations = new Generations();
    }

    Network[] nextGeneration() {
        Network[] networks;

        Generation[] generations = this.generations.getGenerations();
        if (generations.length == 0) {
            // If no Generations, create first.
            networks = this.generations.firstGeneration();
        } else {
            // Otherwise, create next one.
            networks = this.generations.nextGeneration();
        }

        // Create Networks from the current Generation.
        Network[] nns = new Network[networks.length];
        for (int i =0; i < networks.length; i++) {
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

        return nns;
    }

    /**
     * Adds a new Genome with specified Neural Network and score.
     *
     * @param network Neural Network.
     * @param score Score value.
     */
   void  networkScore (Network network, int score) {
//        generations.addGenome(new Genome(score, network.getSave()));
    }
}
