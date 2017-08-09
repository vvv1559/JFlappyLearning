package com.github.vvv1559.network;

class Neuron {
    private double value = 0;
    //FIXME: real size
    private double[] weights;

    /**
     * Initialize number of neuron weights to random clamped values.
     * @param numberOfWeights Number of neuron weights (number of inputs).
     */
    void populate(int numberOfWeights) {
        this.weights = new double[numberOfWeights];
        for (int i = 0; i < numberOfWeights; i++) {
            this.weights[i] = Options.randomClamped();
        }
    }


    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }
}
