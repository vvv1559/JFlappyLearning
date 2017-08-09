package com.github.vvv1559.network;

/**
 * Neural Network Layer class.
 */
public class Layer {
    private int index = 0;
    private Neuron[] neurons;

    /**
     * Build Layer
     *
     * @param index index of layer
     */
    public Layer(int index) {
        this.index = index;
    }

    /**
     * Populate the Layer with a set of randomly weighted Neurons.
     *
     * Each Neuron be initialied with nbInputs inputs with a random clamped
     * value.
     *
     * @param numberOfNeurons Number of neurons.
     * @param numberOfInputs nbInputs Number of inputs.
     */
    public void populate(int numberOfNeurons, int numberOfInputs) {
        neurons = new Neuron[numberOfNeurons];

        for (int i = 0; i < numberOfNeurons; i++) {
            Neuron neuron = new Neuron();
            neuron.populate(numberOfInputs);
            neurons[i] = neuron;
        }
    }

    public int getIndex() {
        return index;
    }

    public Neuron[] getNeurons() {
        return neurons;
    }
}
