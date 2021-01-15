package com.neural_network.neurons;

import com.dictionaries.ConstantDigits;

public class SigmoidNeuron extends Neuron {

    public SigmoidNeuron(double[] weights, double bias){
        this.setWeights(weights);
        this.setBias(bias);
    }

    @Override
    protected double coastFunction(double weightedSum) {
        return 1/(1 + Math.pow(ConstantDigits.E, 0 - weightedSum));
    }
}
