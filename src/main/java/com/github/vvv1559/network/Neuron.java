package com.github.vvv1559.network;

import java.util.Arrays;

class Neuron {
    private double output = 0;
    private final double[] weights;

    /**
     * Initialize number of neuron weights to random clamped values.
     *
     * @param numberOfInputs Number of neuron weights (number of inputs).
     */
    Neuron(int numberOfInputs) {
        this.weights = new double[numberOfInputs];
        for (int i = 0; i < numberOfInputs; i++) {
            this.weights[i] = Options.randomClamped();
        }
    }

    Neuron(Neuron neuron) {
        weights = Arrays.copyOf(neuron.weights, neuron.weights.length);
    }

    void evaluate(double[] inputs) {
        double sum = 0;
        for (int i = 0; i < inputs.length; i++) {
            sum += inputs[i] * weights[i];
        }
        output = Options.activationFunction(sum);
    }

    double getOutput() {
        return output;
    }

    void setOutput(double output) {
        this.output = output;
    }

    int weightsCount() {
        return weights.length;
    }

    double getWeight(int index) {
        return weights[index];
    }

    void setWeight(int index, double weight) {
        weights[index] = weight;
    }
}
