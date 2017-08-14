package com.github.vvv1559.network;

public class NeuralEvolution {
    private Generation currentGeneration;

    /**
     * Produce next generation
     */
    public int nextGeneration() {
        currentGeneration = currentGeneration == null
            ? new Generation()
            : Generation.generateNextGeneration(currentGeneration);

        return currentGeneration.genomesCount();
    }

    /**
     * Adds a new Genome with specified Neural Network and score.
     *
     * @param networkIndex Played network index.
     * @param score        Score value.
     */
    public void fixScore(int networkIndex, int score) {
        currentGeneration.fixScore(networkIndex, score);
    }

    public double[] compute(int index, double[] values) {
        return currentGeneration.compute(index, values);
    }
}
