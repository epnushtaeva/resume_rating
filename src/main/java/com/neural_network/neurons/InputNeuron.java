package com.neural_network.neurons;

public class InputNeuron extends Neuron {

    public InputNeuron(double[] weights, double bias){
        this.setWeights(weights);
        this.setBias(bias);
    }

    @Override
    protected double coastFunction(double weightedSum) {
        return weightedSum;
    }
}
