package com.github.vvv1559;

/**
 * Declaration of module parameters (options) and default values
 */
class Options {

    /**
     * Generate random number in range [-1; 1]
     *
     * @return random number
     */
    static double randomClamped() {
        return Math.random() * 2 - 1;
    }

    /**
     * Logistic activation function.
     *
     * @param value input value
     * @return Logistic function output.
     */
    static double activationFunction(double value) {
        return (1 / (1 + Math.exp(-value)));
    }

    // Various factors and parameters (along with default values).
    // Perceptron network structure (1 hidden layer).
    static final int[][] network = new int[][]{new int[]{1}, new int[]{1}, new int[]{1}};

    // Population by generation.
    static final int population = 50;

    // Best networks kepts unchanged for the next generation (rate).
    static final double elitism = 0.2;

    // New random networks for the next generation (rate).
    static final double randomBehaviour = 0.2;

    // Mutation rate on the weights of synapses.
    static final double mutationRate = 0.1;

    // Interval of the mutation changes on the synapse weight.
    static final double mutationRange = 0.5;

    // Latest generations saved.
    static final int historic = 0;

    // Only save score (not the network).
    static boolean lowHistoric = false;

    // Sort order (-1 = desc, 1 = asc).
    static final int scoreSort = -1;

    // Number of children by breeding.
    static final int nbChild = 1;

    private Options() {
        throw new UnsupportedOperationException();
    }
}
