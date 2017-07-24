package com.github.vvv1559;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Generations class.
 *
 * Hold's previous Generations and current Generation. */
public class Generations {
    private List<Generation> generations = new ArrayList<>();
    private Generation currentGeneration = new Generation();

    /**
     * Create the first generation.
     *
     * @return {number[]} First Generation.
     */
    Network[] firstGeneration() {
        List<Network> out = new LinkedList<>();
        for (int i = 0; i < Options.population; i++) {
            // Generate the Network and save it.
            Network nn = new Network();
            nn.perceptronGeneration(Options.network[0][0],
                Options.network[1],
                Options.network[2][0]);
            out.add(nn);
        }

        this.generations.add(new Generation());
        return out.toArray(new Network[out.size()]);
    };

    /**
     * Create the next Generation.
     *
     * @return Next Generation.
     */
    Network[] nextGeneration () {
        if (this.generations.isEmpty()) {
            // Need to create first generation.
            return null;
        }

        Network[] gen = this.generations.get(this.generations.size() - 1).generateNextGeneration();
        this.generations.add(new Generation());
        return gen;
    };

    /**
     * Add a genome to the Generations.
     *
     * @param genome genome
     */
    void addGenome(Genome genome) {
        // Can't add to a Generation if there are no Generations.
        if (!this.generations.isEmpty()){
            this.generations.get(this.generations.size() - 1).addGenome(genome);
        }
    }

    public Generation[] getGenerations() {
        return generations.toArray(new Generation[generations.size()]);
    }

    public void setGenerations(Generation[] generations) {
        this.generations = Arrays.asList(generations);
    }
}
