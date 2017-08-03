package com.github.vvv1559;

public class Genome {
    private int score;
    private Network network;

    /**
     * Composed of a score and a Neural Network.
     *
     * @param network network
     * @param score   score
     */
    public Genome(Network network, int score) {
        this.score = score;
        this.network = network;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }
}
