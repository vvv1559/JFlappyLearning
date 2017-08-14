package com.github.vvv1559.network;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

class Genome {
    private int score = 0;
    private final Network network;

    /**
     * Composed of a score and a Neural Network.
     */
    Genome() {
        this.network = new Network(
            Options.neuronsInInputLayer,
            Options.neuronsInHiddenLayers,
            Options.neuronsInOutLayer
        );
    }

    Genome(Genome genome) {
        network = new Network(genome.network);
    }

    private int getScore() {
        return score;
    }

    static Comparator<Genome> genomeComparator() {
        return Comparator.comparingInt(Genome::getScore);
    }

    void setScore(int score) {
        this.score = score;
    }

    //Breed to genomes to produce offspring(s).
    Genome[] breed(Genome secondGenome, int childrenCount) {
        List<Genome> result = new LinkedList<>();
        while (childrenCount-- != 0) {
            Genome firstGenome = new Genome(this);
            for (int layerIndex = 0; layerIndex < secondGenome.network.layersCount(); layerIndex++) {
                Layer secondGenomeLayer = secondGenome.network.getLayer(layerIndex);

                for (int neuronIndex = 0; neuronIndex < secondGenomeLayer.neuronsCount(); neuronIndex++) {
                    Neuron secondGenomeNeuron = secondGenomeLayer.getNeuron(neuronIndex);

                    for (int weightIndex = 0; weightIndex < secondGenomeNeuron.weightsCount(); weightIndex++) {
                        if (Math.random() <= Options.crossoverFactor) {
                            Neuron firstGenomeNeuron = firstGenome.network.getLayer(layerIndex).getNeuron(neuronIndex);
                            firstGenomeNeuron.setWeight(weightIndex, secondGenomeNeuron.getWeight(weightIndex));
                        }
                    }
                }
            }

            // Perform mutation on some weights.
            for (int layerIndex = 0; layerIndex < secondGenome.network.layersCount(); layerIndex++) {
                Layer firstGenomeLayer = secondGenome.network.getLayer(layerIndex);

                for (int neuronIndex = 0; neuronIndex < firstGenomeLayer.neuronsCount(); neuronIndex++) {
                    Neuron firstGenomeNeuron = firstGenomeLayer.getNeuron(neuronIndex);

                    for (int weightIndex = 0; weightIndex < firstGenomeNeuron.weightsCount(); weightIndex++) {

                        if (Math.random() <= Options.mutationRate) {
                            double newWeight = firstGenomeNeuron.getWeight(weightIndex) +
                                Math.random() * Options.mutationRange * 2 - Options.mutationRange;

                            firstGenomeNeuron.setWeight(weightIndex, newWeight);
                        }
                    }
                }
            }
            result.add(firstGenome);
        }

        return result.toArray(new Genome[result.size()]);
    }

    double[] compute(double[] values) {
        return network.compute(values);
    }
}
