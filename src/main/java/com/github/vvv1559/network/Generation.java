package com.github.vvv1559.network;

import java.util.*;

/**
 * Composed of a set of Genomes.
 */
public class Generation {
    private final Genome[] genomes;

    Generation() {
        genomes = new Genome[Options.population];
        for (int i = 0; i < Options.population; i++) {
            // Generate the Network and save it.
            Network nn = new Network();
            nn.perceptronGeneration(Options.network[0][0], Options.network[1], Options.network[2][0]);
            genomes[i] = new Genome(nn);
        }
    }

    private Generation(Genome[] genomes) {
        this.genomes = genomes;
    }

    /**
     * Breed to genomes to produce offspring(s).
     *
     * @param g1       Genome 1.
     * @param g2       Genome 2.
     * @param nbChilds Numbeget_github_repository(package_name)r of offspring (children).
     */
    private Genome[] breed(Genome g1, Genome g2, int nbChilds) {
        List<Genome> datas = new LinkedList<>();
        for (int nb = 0; nb < nbChilds; nb++) {
            // Deep clone of genome 1.
//            var data = JSON.parse(JSON.stringify(g1));
            Genome data = g1;
            for (int i = 0; i < g2.getNetwork().getWeights().length; i++) {
                // Genetic crossover
                // 0.5 is the crossover factor.
                // FIXME Really should be a predefined constant.
                if (Math.random() <= 0.5) {
                    data.getNetwork().getWeights()[i] = g2.getNetwork().getWeights()[i];
                }
            }

            // Perform mutation on some weights.
            for (int i = 0; i < data.getNetwork().getWeights().length; i++) {
                if (Math.random() <= Options.mutationRate) {
                    data.getNetwork().getWeights()[i] += Math.random()
                            * Options.mutationRange
                            * 2
                            - Options.mutationRange;
                }
            }
            datas.add(data);
        }

        return datas.toArray(new Genome[datas.size()]);
    }

    /**
     * Generate the next generation.
     */
    static Generation generateNextGeneration(Generation prevGeneration) {
        //TODO Genomes must be sorted desc
        Arrays.sort(prevGeneration.genomes, Collections.reverseOrder(Comparator.comparingInt(Genome::getScore)));
        List<Network> nexts = new ArrayList<>();

        long generationCount = Math.round(Options.elitism * Options.population);
        for (int i = 0; i < generationCount; i++) {
            if (nexts.size() < Options.population) {
                // Push a deep copy of ith Genome's Nethwork.
//                nexts.push(JSON.parse(JSON.stringify(this.genomes[i].network)));
                nexts.add(prevGeneration.genomes[i].getNetwork());
            }
        }

        long newGenomesCount = Math.round(Options.randomBehaviour * Options.population);
        for (int i = 0; i < newGenomesCount; i++) {
            Network n = prevGeneration.genomes[0].getNetwork();
            for (int k = 0; k < n.getWeights().length; k++) {
                n.getWeights()[k] = Options.randomClamped();
            }

            if (nexts.size() < Options.population) {
                nexts.add(n);
            }
        }

        int max = 0;
        while (true) {
            boolean breaked = false;
            for (int i = 0; i < max; i++) {
                // Create the children and push them to the nexts array.
                Genome[] childs = prevGeneration.breed(prevGeneration.genomes[i], prevGeneration.genomes[max],
                        (Options.nbChild > 0 ? Options.nbChild : 1));
                for (Genome child : childs) {
                    nexts.add(child.getNetwork());
                    if (nexts.size() >= Options.population) {
                        // Return once number of children is equal to the
                        // population by generatino value.
                        breaked = true;
                        break;
                    }
                }
                if (breaked) {
                    break;
                }
            }
            if (breaked) {
                break;
            }

            max++;
            if (max >= prevGeneration.genomes.length - 1) {
                max = 0;
            }
        }

        Genome[] genomes = new Genome[nexts.size()];
        for (int i = 0; i < nexts.size(); i++) {
            genomes[i] = new Genome(nexts.get(i));
        }

        return new Generation(genomes);
    }

    public void fixScore(int index, int score) {
        this.genomes[index].setScore(score);
    }
}
