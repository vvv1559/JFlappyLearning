package com.github.vvv1559.network;

import java.util.ArrayList;

/**
 * Neural Network class
 */
public class Network {
    private Layer[] layers;
    private double[] weights;

    /**
     * Generate the Network layers.
     *
     * @param numberNeuronsInput  Number of Neurons in Input layer.
     * @param numberNeuronsHidden Number of Neurons per Hidden layer.
     * @param numberNeuronsOut    Number of Neurons in Output layer.
     */
    //TODO clean signature
    void perceptronGeneration(int numberNeuronsInput, int[] numberNeuronsHidden, int numberNeuronsOut) {
        layers = new Layer[numberNeuronsHidden.length + 2];
        int index = 0;
        int previousNeurons = 0;
        Layer inputLayer = new Layer(index);
        inputLayer.populate(numberNeuronsInput, previousNeurons); // Number of Inputs will be set to 0 since it is
        // an input layer.
        previousNeurons = numberNeuronsInput;  // number of input is size of previous layer.
        this.layers[index] = inputLayer;
        index++;
        for (int aNumberNeuronsHidden : numberNeuronsHidden) {
            // Repeat same process as first layer for each hidden layer.
            Layer layer = new Layer(index);
            layer.populate(aNumberNeuronsHidden, previousNeurons);
            previousNeurons = aNumberNeuronsHidden;
            this.layers[index] = layer;
            index++;
        }
        Layer outLayer = new Layer(index);
        outLayer.populate(numberNeuronsOut, previousNeurons);  // Number of input is equal to the size of the last hidden layer.
        this.layers[index] = outLayer;
    }

    //FIXME Clear Solution
    public LayerData[] getSave() {
        LayerData[] data = new LayerData[layers.length];
        int index = 0;

        for (Layer layer : this.layers) {
            int neuronsCount = layer.getNeurons().length;
            ArrayList<Double> weights = new ArrayList<>();
            for (Neuron neuron : layer.getNeurons()) {
                // push all input weights of each Neuron of each Layer into a flat array.
                for (double weight : neuron.getWeights()) {
                    weights.add(weight);
                }
            }
            double[] weightsArray = new double[weights.size()];
            int arrayIndex = 0;
            for (Double w : weights) {
                weightsArray[arrayIndex++] = w;
            }
            data[index++] = new LayerData(neuronsCount, weightsArray);
        }
        return data;
    }


    /**
     * Apply network data (neurons and weights).
     *
     * @param save Copy of network data (neurons and weights).
     */
    public void setSave(LayerData[] save) {
        //FIXME Clear Solution
        int previousNeurons = 0;
        int index = 0;
        int indexWeights = 0;
        this.layers = new Layer[save.length];
        for (LayerData layerData : save) {
            // Create and populate layers.
            Layer layer = new Layer(index);
            layer.populate(layerData.neuronCounts, previousNeurons);
            for (int i = 0; i < layerData.neuronCounts; i++) {
                Neuron neuron = layer.getNeurons()[i];
                for (int k = 0; k < neuron.getWeights().length; k++) {
                    // Apply neurons weights to each Neuron.
                    neuron.getWeights()[k] = layerData.weights[indexWeights];
                    indexWeights++; // Increment index of flat array.
                }
            }
            this.layers[index++] = layer;
            previousNeurons = layerData.neuronCounts;
        }
    }

    /**
     * Compute the output of an input.
     *
     * @param inputs Set of inputs.
     * @return Network output.
     */
    public double[] compute(double[] inputs) {
        // Set the value of each Neuron in the input layer.
        for (int i = 0; i < inputs.length; i++) {
            if (this.layers.length > 0 && this.layers[0].getNeurons().length > 0) {
                this.layers[0].getNeurons()[i].setValue(inputs[i]);
            }
        }

        Layer prevLayer = this.layers[0]; // Previous layer is input layer.
        for (int i = 1; i < this.layers.length; i++) {
            for (int j = 0; j < this.layers[i].getNeurons().length; j++) {
                // For each Neuron in each layer.
                double sum = 0;
                for (int k = 0; k < prevLayer.getNeurons().length; k++) {
                    // Every Neuron in the previous layer is an input to each Neuron in
                    // the next layer.
                    sum += prevLayer.getNeurons()[k].getValue()
                        * this.layers[i].getNeurons()[j].getWeights()[k];
                }

                // Compute the activation of the Neuron.
                this.layers[i].getNeurons()[j].setValue(Options.activationFunction(sum));
            }
            prevLayer = this.layers[i];
        }

        // All outputs of the Network.
        Layer lastLayer = this.layers[this.layers.length - 1];
        double[] out = new double[lastLayer.getNeurons().length];
        for (int i = 0; i < lastLayer.getNeurons().length; i++) {
            out[i] = lastLayer.getNeurons()[i].getValue();
        }
        return out;
    }

    public double[] getWeights() {
        return weights;
    }

    public static class LayerData {
        // Number of Neurons per layer.
        private final int neuronCounts;

        // Weights of each Neuron's inputs.
        private final double[] weights;

        LayerData(int neuronCounts, double[] weights) {
            this.neuronCounts = neuronCounts;
            this.weights = weights;
        }
    }
}
