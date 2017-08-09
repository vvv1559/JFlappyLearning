package com.github.vvv1559.network;

public class NeuralEvolution {
    private Generation currentGeneration;

    /**
     * Produce next generation
     */
    public void nextGeneration() {
        currentGeneration = currentGeneration == null
                ? new Generation()
                : Generation.generateNextGeneration(currentGeneration);
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
}
