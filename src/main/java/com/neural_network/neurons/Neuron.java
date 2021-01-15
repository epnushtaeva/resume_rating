package com.neural_network.neurons;

import com.utils.ArrayUtils;

import java.util.stream.IntStream;

public abstract class Neuron {
    private double[] weights = {};
    private double bias;

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public double getOutput(double[] values){
        return this.coastFunction(this.getWeightedSum(values) + bias);
    }

    private double getWeightedSum(double[] values){
        return IntStream
                .of(ArrayUtils.createAndFill(0, values.length - 1))
                .mapToDouble(
                        weightAndValueIndex -> this.weights[weightAndValueIndex] * values[weightAndValueIndex]
                        )
                .sum();
    }

    protected abstract double coastFunction(double weightedSum);
}
