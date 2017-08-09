package com.github.vvv1559.network;

public class Genome {
    private int score;
    private Network network;

    /**
     * Composed of a score and a Neural Network.
     *
     * @param network network
     */
    public Genome(Network network) {
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
