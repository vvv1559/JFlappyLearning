package com.github.vvv1559;

public class Genome {
    private int score;
    private Network network;

    /**
     * Composed of a score and a Neural Network.
     *
     * @param score   score
     * @param network network
     */
    public Genome(int score, Network network) {
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
