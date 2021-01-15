package com.dto;

public class NeuralNetworkEntityDto {
    private int layerNumber;
    private int neuronNumber;
    private double weight;
    private double bias;

    public int getLayerNumber() {
        return layerNumber;
    }

    public void setLayerNumber(int layerNumber) {
        this.layerNumber = layerNumber;
    }

    public int getNeuronNumber() {
        return neuronNumber;
    }

    public void setNeuronNumber(int neuronNumber) {
        this.neuronNumber = neuronNumber;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }
}
