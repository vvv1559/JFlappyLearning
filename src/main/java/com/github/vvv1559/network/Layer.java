package com.github.vvv1559.network;

/**
 * Neural Network Layer class.
 */
class Layer {
    private final Neuron[] neurons;

    /**
     * Populate the Layer with a set of randomly weighted Neurons.
     */
    Layer(int numberOfNeurons, int numberOfInputs) {
        neurons = new Neuron[numberOfNeurons];

        for (int i = 0; i < numberOfNeurons; i++) {
            neurons[i] = new Neuron(numberOfInputs);
        }

    }

    Layer(Layer layer) {
        neurons = new Neuron[layer.neurons.length];
        for (int i = 0; i < neurons.length; i++) {
            neurons[i] = new Neuron(layer.neurons[i]);
        }
    }

    void setInputs(double[] prevLayerOutput) {
        for (Neuron neuron : neurons) {
            neuron.evaluate(prevLayerOutput);
        }
    }

    void setOutputs(double[] inputs) {
        for (int i = 0; i < inputs.length; i++) {
            neurons[i].setOutput(inputs[i]);
        }
    }

    double[] getOutputs() {
        double[] result = new double[neurons.length];
        for (int i = 0; i < neurons.length; i++) {
            result[i] = neurons[i].getOutput();
        }

        return result;
    }

    Neuron getNeuron(int index) {
        return neurons[index];
    }

    int neuronsCount() {
        return neurons.length;
    }
}
