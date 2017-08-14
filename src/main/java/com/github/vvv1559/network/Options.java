package com.github.vvv1559.network;

/**
 * Declaration of module parameters (options) and default values
 */
class Options {

    /**
     * Generate random number in range [-1; 1]
     */
    static double randomClamped() {
        return Math.random() * 2 - 1;
    }

    /**
     * Logistic activation function - Sigmoid 1/(1+e^-x).
     */
    static double activationFunction(double value) {
        return (1 / (1 + Math.exp(-value)));
    }

    // Various factors and parameters (along with default values).
    // Perceptron network structure (1 hidden layer).
    static final int neuronsInInputLayer = 2;
    static final int[] neuronsInHiddenLayers = new int[]{2};
    static final int neuronsInOutLayer = 1;

    // Population by generation.
    static final int population = 50;

    // Best networks kepts unchanged for the next generation (rate).
    static final double elitism = 0.2;

    // New random networks for the next generation (rate).
    static final double randomBehaviour = 0.2;

    // Mutation rate on the weights of synapses.
    static final double mutationRate = 0.1;

    // Probability of crossover
    static final double crossoverFactor = 0.5;

    // Interval of the mutation changes on the synapse weight.
    static final double mutationRange = 0.5;

    // Number of children by breeding.
    static final int nbChild = 1;

    private Options() {
        throw new UnsupportedOperationException();
    }
}
