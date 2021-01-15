package com.neural_network;

import com.neural_network.neurons.InputNeuron;
import com.neural_network.neurons.Neuron;
import com.neural_network.neurons.SigmoidNeuron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkLayer {

    private List<Neuron> neurons = new ArrayList<>();
    private Map<String, Neuron> neuronTypesToClasses= new HashMap<>();

    public NetworkLayer(String neuronsType, double[][] weights, double[] biases){
        this.init();

        for(int neuronIndex = 0; neuronIndex < weights.length; neuronIndex++){
            try {
                neurons.add(this.neuronTypesToClasses
                        .get(neuronsType)
                        .getClass()
                        .getConstructor(double[].class, double.class)
                        .newInstance(weights[neuronIndex], biases[neuronIndex]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public double[] getOutputs(double[][] values){
        double[] result = new double[this.neurons.size()];

        for(int outputIndex = 0; outputIndex < result.length; outputIndex++){
            result[outputIndex] = this.neurons.get(outputIndex).getOutput(values[outputIndex]);
        }

        return result;
    }

    public int getNeuronsCount(){
        return  this.neurons.size();
    }

    public double[][] getWeights(){
        double[][] result = new double[this.neurons.size()][];

        for(int neuronIndex = 0; neuronIndex < this.neurons.size(); neuronIndex++){
            result[neuronIndex] = this.neurons.get(neuronIndex).getWeights();
        }

        return result;
    }

    public void setWeights(double[][] weights){
        for(int neuronIndex = 0; neuronIndex < this.neurons.size(); neuronIndex++){
            this.neurons.get(neuronIndex).setWeights(weights[neuronIndex]);
        }
    }

    public double[] getBiases(){
        double[] result = new double[this.neurons.size()];

        for(int neuronIndex = 0; neuronIndex < this.neurons.size(); neuronIndex++){
            result[neuronIndex] = this.neurons.get(neuronIndex).getBias();
        }

        return result;
    }

    public void setBiases(double[] biases){
        for(int neuronIndex = 0; neuronIndex < this.neurons.size(); neuronIndex++){
            this.neurons.get(neuronIndex).setBias(biases[neuronIndex]);
        }
    }

    private void init(){
        this.neuronTypesToClasses.put("input", new InputNeuron(new double[0], 0));
        this.neuronTypesToClasses.put("sigma", new SigmoidNeuron(new double[0], 0));
    }
}
