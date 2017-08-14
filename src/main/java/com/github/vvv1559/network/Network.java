package com.github.vvv1559.network;

class Network {
    private final Layer[] layers;

    /**
     * @param neuronsInInputLayer   Neurons count in input layer
     * @param neuronsInHiddenLayers Array of neuron counts in hidden layers
     * @param neuronsInOutLayer     Neurons count in output layer
     */
    Network(int neuronsInInputLayer, int[] neuronsInHiddenLayers, int neuronsInOutLayer) {

        layers = new Layer[neuronsInHiddenLayers.length + 2];
        int index = 0;
        // Number of inputs will be set to 0 because it is an input layer.
        layers[index++] = new Layer(neuronsInInputLayer, 0);

        int neuronsInPrevLayer = neuronsInInputLayer;
        // Repeat same process as first layer for each hidden layer.
        for (int neuronsInHiddenLayer : neuronsInHiddenLayers) {
            layers[index++] = new Layer(neuronsInHiddenLayer, neuronsInPrevLayer);
            neuronsInPrevLayer = neuronsInHiddenLayer;
        }

        // Out layer
        layers[index] = new Layer(neuronsInOutLayer, neuronsInPrevLayer);

    }

    Network(Network network) {
        layers = new Layer[network.layers.length];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = new Layer(network.layers[i]);
        }
    }

    /**
     * Compute the output of an input.
     *
     * @param inputs Set of inputs.
     * @return Network output.
     */
    double[] compute(double[] inputs) {
        // Directly set the output value of each Neuron in the input layer.
        Layer inputLayer = layers[0];
        inputLayer.setOutputs(inputs);

        for (int i = 1; i < layers.length; i++) {
            layers[i].setInputs(layers[i - 1].getOutputs());
        }

        int lastLayerIndex = layers.length - 1;
        return layers[lastLayerIndex].getOutputs();
    }

    Layer getLayer(int index) {
        return layers[index];
    }

    int layersCount() {
        return layers.length;
    }
}
