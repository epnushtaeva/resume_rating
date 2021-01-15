package com.neural_network;

import com.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class Network {
    private List<NetworkLayer> layers = new ArrayList<>();

   public Network(int[] neuronsInLayersCounts, double[][] weights, double[] biases){
       int layerStartNeuronWeightsIndex = 0;
       int layerIndex = 0;
       String neuronType = "sigma";

       for(int neuronsInLayerCount: neuronsInLayersCounts){
           double[][] layerWeights = new double[neuronsInLayerCount][];
           double[] layerBiases = new double[neuronsInLayerCount];

           for(int weightIndex = layerStartNeuronWeightsIndex; weightIndex < layerStartNeuronWeightsIndex + neuronsInLayerCount; weightIndex++){
               layerWeights[weightIndex - layerStartNeuronWeightsIndex] = weights[weightIndex];
               layerBiases[weightIndex - layerStartNeuronWeightsIndex] = biases[weightIndex];
           }

           if(layerIndex == 0){
               neuronType = "input";
           } else {
               neuronType = "sigma";
           }

           this.layers.add(new NetworkLayer(neuronType, layerWeights, layerBiases));

           layerStartNeuronWeightsIndex += neuronsInLayerCount;
           layerIndex++;
       }
   }

   public double[] getOutputs(double[] values){
       double[][] currentValues = ArrayUtils.toMultipleArray(values);
       double[] currentLayerOutputs = new double[0];
       int layersCount = this.layers.size();
       int layerIndex = 0;

       for(NetworkLayer layer: this.layers){
           currentLayerOutputs = layer.getOutputs(currentValues);

           if(layerIndex != layersCount - 1) {
               currentValues = ArrayUtils.toMultipleArray(this.layers.get(layerIndex + 1).getNeuronsCount(), currentLayerOutputs);
           }

           layerIndex++;
       }

       return currentLayerOutputs;
   }

    public double[][] getOutputsFromAllLayers(double[] values){
        double[][] currentValues = ArrayUtils.toMultipleArray(values);
        double[] currentLayerOutputs = new double[0];
        double[][] outputs = new double[this.layers.size()][];
        int layersCount = this.layers.size();
        int layerIndex = 0;

        for(NetworkLayer layer: this.layers){
            currentLayerOutputs = layer.getOutputs(currentValues);
            outputs[layerIndex] = currentLayerOutputs;

            if(layerIndex != layersCount - 1) {
                currentValues = ArrayUtils.toMultipleArray(this.layers.get(layerIndex + 1).getNeuronsCount(), currentLayerOutputs);
            }

            layerIndex++;
        }

        return outputs;
    }

    public int getLayersCount(){
       return this.layers.size();
    }

   public double[][] getWeights(int layerIndex){
       return this.layers.get(layerIndex).getWeights();
   }

   public void setWeights(int layerIndex, double[][] weights){
       this.layers.get(layerIndex).setWeights(weights);
   }

    public double[] getBiases(int layerIndex){
        return this.layers.get(layerIndex).getBiases();
    }

   public void setBiases(int layerIndex, double[] biases){
       this.layers.get(layerIndex).setBiases(biases);
   }
}
