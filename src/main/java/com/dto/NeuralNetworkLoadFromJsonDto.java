package com.dto;

import java.util.List;

public class NeuralNetworkLoadFromJsonDto {
    private List<String> dictionary;
    private String neuralNetwork = "";

    public List<String> getDictionary() {
        return dictionary;
    }

    public void setDictionary(List<String> dictionary) {
        this.dictionary = dictionary;
    }

    public String getNeuralNetwork() {
        return neuralNetwork;
    }

    public void setNeuralNetwork(String neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }
}
