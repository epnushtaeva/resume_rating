package com.neural_network.neurons;

import com.dictionaries.ConstantDigits;

public class ReluNeuron extends Neuron {

    @Override
    protected double coastFunction(double weightedSum) {
        double result =  Math.pow(ConstantDigits.E, weightedSum);

        if(result>1) {
            return 1;
        }

        return result;
    }
}
