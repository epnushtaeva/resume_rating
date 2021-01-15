package com.neural_network;

import com.dictionaries.ConstantDigits;
import com.utils.ArrayUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class NetworkTeacher {
    private Network network;

    public NetworkTeacher(Network network) {
        this.network = network;
    }

    public void teach(NetworkTeachParameters networkTeachParameters) {
        boolean isNetworkTeached = false;
        double startTeachTime = System.currentTimeMillis();
        int learnEpochsCount = 0;

        while (!isNetworkTeached) {
            isNetworkTeached = true;
            int trainingSetIndex = 0;
            double[][] trainingSets = networkTeachParameters.getTrainingSets();

            for (double[] trainingSet : trainingSets) {
                double[][] outputs = this.network.getOutputsFromAllLayers(trainingSet);
                double[] targetValues = networkTeachParameters.getTargetValues(trainingSetIndex);
                double[] lastLayerErrors = this.getLastLayerErrors(outputs, targetValues);

                if (this.isErrorsMoreThanMaxErrorExists(lastLayerErrors, networkTeachParameters.getMaxError())) {
                    isNetworkTeached = false;
                    double[][][] layersErrors = new double[outputs.length][][];
                    double[][] biasErrors = new double[outputs.length][];

                    for (int layerIndex = outputs.length - 1; layerIndex > 0; layerIndex--) {
                        int neuronsCount = outputs[layerIndex].length;
                        double[] neuronsInputs = outputs[layerIndex - 1];
                        double[] changeCoefficients = this.getChangeWeightsAndBiasesCoefficients(outputs[layerIndex], networkTeachParameters.getTeachScore());

                        layersErrors[layerIndex] = new double[neuronsCount][];

                        double[] neuronsErrors = this.getNeuronsErrors(outputs, layerIndex, lastLayerErrors, changeCoefficients, layersErrors);
                        biasErrors[layerIndex] = neuronsErrors;
                        layersErrors[layerIndex] = this.getLayerErrors(neuronsErrors, neuronsInputs);
                    }

                    for (int layerIndex = 1; layerIndex < outputs.length; layerIndex++) {
                        double[][] newWeights = this.getCorrectedWeights(layerIndex, layersErrors[layerIndex]);
                        double[] newBiases = this.getCorrectedBiases(layerIndex, biasErrors[layerIndex]);
                        this.network.setWeights(layerIndex, newWeights);
                        this.network.setBiases(layerIndex, newBiases);
                    }
                }

                trainingSetIndex++;
            }

            if(System.currentTimeMillis() - startTeachTime > networkTeachParameters.getMaxTeachTime()){

                break;
            }

            learnEpochsCount++;
        }
        System.out.println(System.currentTimeMillis() - startTeachTime);
        System.out.println(learnEpochsCount);
    }

    private double[] getNeuronsErrors(double[][] outputs,
                                      int layerIndex,
                                      double[] lastLayerErrors,
                                      double[] changeCoefficients,
                                      double[][][] layersErrors) {
        int neuronsCount = outputs[layerIndex].length;
        double[] neuronsErrors = new double[neuronsCount];

        if (layerIndex == outputs.length - 1) {
            neuronsErrors = lastLayerErrors;

            for (int neuronIndex = 0; neuronIndex < neuronsCount; neuronIndex++) {
                neuronsErrors[neuronIndex] = neuronsErrors[neuronIndex] * changeCoefficients[neuronIndex];
            }
        } else {
            double[][] nextLayertWeights = this.network.getWeights(layerIndex + 1);
            double[][] nextLayerErrors = layersErrors[layerIndex + 1];
            double[][] weightedNextLayerErrors = this.getWeightedNextLayerWeights(nextLayerErrors, nextLayertWeights);

            for (int neuronIndex = 0; neuronIndex < weightedNextLayerErrors.length; neuronIndex++) {
                neuronsErrors[neuronIndex] = Arrays.stream(weightedNextLayerErrors[neuronIndex]).sum() * changeCoefficients[neuronIndex];
            }
        }


        return neuronsErrors;
    }

    private double[] getChangeWeightsAndBiasesCoefficients(double[] outputs, double teachScore){
        double[] result = new double[outputs.length];

        for(int coefficientIndex = 0; coefficientIndex < outputs.length; coefficientIndex++){
            result[coefficientIndex] = outputs[coefficientIndex] * (1 - outputs[coefficientIndex]) * teachScore;
        }

        return result;
    }

    private boolean isErrorsMoreThanMaxErrorExists(double[] errors, double maxError){
        for(double error: errors){
            if(Math.abs(error) > maxError){
                return true;
            }
        }

        return false;
    }

    private double[] getLastLayerErrors(double[][] outputs, double[] targetValues){
        double[] lastLayerOutputs = outputs[outputs.length - 1];
        double[] errors = new double[lastLayerOutputs.length];

        for(int lastLayerOutputIndex = 0; lastLayerOutputIndex < lastLayerOutputs.length; lastLayerOutputIndex++){
            errors[lastLayerOutputIndex] = lastLayerOutputs[lastLayerOutputIndex] - targetValues[lastLayerOutputIndex];
        }

        return errors;
    }

    private double[] getCorrectedBiases(int layerIndex, double[] biasesErrors){
        double[] biases = this.network.getBiases(layerIndex);

        for(int neuronIndex = 0; neuronIndex < biasesErrors.length; neuronIndex++){
            biases[neuronIndex] -= biasesErrors[neuronIndex];
        }

        return biases;
    }

    private double[][] getCorrectedWeights(int layerIndex, double[][] errors){
        double[][] weights = this.network.getWeights(layerIndex);

        for(int neuronIndex = 0; neuronIndex < weights.length; neuronIndex++){
            for(int weightsIndex = 0; weightsIndex < weights[neuronIndex].length; weightsIndex++){
                weights[neuronIndex][weightsIndex] -= errors[neuronIndex][weightsIndex];
            }
        }

        return weights;
    }

    private double[][] getWeightedNextLayerWeights(double[][] nextLayerErrors, double[][] nextLayerWeights) {
        int nextLayerNeuronsCount = nextLayerWeights.length;
        int nextLayerWeightsCount = nextLayerWeights[0].length;
        double[][] result = new double[nextLayerWeightsCount][];

        for (int neuronIndex = 0; neuronIndex < nextLayerNeuronsCount; neuronIndex++) {
            for (int weightIndex = 0; weightIndex < nextLayerWeightsCount; weightIndex++) {
                if (result[weightIndex] == null) {
                    result[weightIndex] = new double[nextLayerNeuronsCount];
                }

                result[weightIndex][neuronIndex] = nextLayerWeights[neuronIndex][weightIndex] * nextLayerErrors[neuronIndex][weightIndex];
            }
        }

        return result;
    }

    private double[][] getLayerErrors(double[] neuronsErrors, double[] neuronsInputs) {
        double[][] result = new double[neuronsErrors.length][];

        for (int neuronIndex = 0; neuronIndex < neuronsErrors.length; neuronIndex++) {
            result[neuronIndex] = new double[neuronsInputs.length];

            for (int weightIndex = 0; weightIndex < neuronsInputs.length; weightIndex++) {
                result[neuronIndex][weightIndex] = neuronsErrors[neuronIndex] * neuronsInputs[weightIndex];
            }
        }

        return result;
    }

    private double[][] getWeightedSumms(double[][][] currentLayerOutputs, double[][][] nextLayerErrors, double[][][][] nextLayerWeights){
        List<double[]> result= new ArrayList<>();
        int currentLayerVariantIndex = 0;

        for(double[][] layerVariantOutputs: currentLayerOutputs){
            double[] weightedSumms = new double[layerVariantOutputs[0].length];
            double[][] currentErrors = new double[nextLayerErrors.length][];
            int nextLayerIndex = 0;

            Arrays.fill(weightedSumms, 0);

            for(double[][] nextErrors: nextLayerErrors){
                currentErrors[nextLayerIndex] = nextErrors[currentLayerVariantIndex];
                nextLayerIndex++;
            }

            int weightIndex = 0;

            for(double[][][] weights:nextLayerWeights){
                double[][] currentErrorsWeights = weights[currentLayerVariantIndex];

                for(int currentErrorsWeightsIndex = 0; currentErrorsWeightsIndex < currentErrorsWeights[0].length; currentErrorsWeightsIndex++){
                    for(int currentErrorIndex = 0; currentErrorIndex < currentErrorsWeights.length; currentErrorIndex++){
                        weightedSumms[currentErrorsWeightsIndex] += currentErrorsWeights[currentErrorIndex][currentErrorsWeightsIndex]*currentErrors[weightIndex][currentErrorIndex];
                    }
                }

                weightIndex++;
            }

            result.add(weightedSumms);
            currentLayerVariantIndex++;
        }

        return ArrayUtils.toArrayOfDoubleArrays(result);
    }

    private double[][][][][] getChangedWeights(double[][][][] errors, double[][][][] outputs, double[][][][][] lastWeights, double teachScore){
        int layerIndex = 0;
        double[][][][][] result = new double[errors.length][][][][];

        for(double[][][] currentLayerErrors: errors){
            if(layerIndex==0){
                result[layerIndex] = lastWeights[layerIndex];
                layerIndex++;
                continue;
            }

            result[layerIndex] = new double[currentLayerErrors.length][][][];
            int layerVariantIndex = 0;

            for(double[][] currentLayerVariantErrors: currentLayerErrors){
                result[layerIndex][layerVariantIndex] = new double[currentLayerVariantErrors.length][][];
                int lastLayerVariantIndex = 0;

                for(double[] currentLayerErrorsForLastLayerVariant: currentLayerVariantErrors){
                    result[layerIndex][layerVariantIndex][lastLayerVariantIndex] = new double[currentLayerErrorsForLastLayerVariant.length][];
                    int neuronIndex = 0;

                    for(double neuronError: currentLayerErrorsForLastLayerVariant){
                        result[layerIndex][layerVariantIndex][lastLayerVariantIndex][neuronIndex] = new double[outputs[layerIndex-1][lastLayerVariantIndex][0].length];
                        int lastLayerNeuronIndex = 0;

                        for(double lastLayerOutput: outputs[layerIndex-1][lastLayerVariantIndex][0]){
                            result[layerIndex][layerVariantIndex][lastLayerVariantIndex][neuronIndex][lastLayerNeuronIndex] = lastWeights[layerIndex][layerVariantIndex][lastLayerVariantIndex][neuronIndex][lastLayerNeuronIndex] -
                                    (neuronError *
                                            outputs[layerIndex][layerVariantIndex][lastLayerVariantIndex][neuronIndex] *
                                            (1 - outputs[layerIndex][layerVariantIndex][lastLayerVariantIndex][neuronIndex]) *
                                            lastLayerOutput * teachScore);
                            lastLayerNeuronIndex++;
                        }

                        neuronIndex++;
                    }

                    lastLayerVariantIndex++;
                }

                layerVariantIndex++;
            }

            layerIndex++;
        }

        return result;
    }

    private double[][][] getChangedBiases(double[][][][] errors, double[][][][] outputs, double[][][] biases, double teachScore){
        int layerIndex = 0;
        double[][][] result = new double[errors.length][][];

        for(double[][][] layerErrors: errors){
            if(layerIndex==0){
                result[layerIndex] = biases[layerIndex];
                layerIndex++;
                continue;
            }

            int layerVariantIndex = 0;
            result[layerIndex] = new double[layerErrors.length][];

            for(double[][] layerVariantErrors: layerErrors){
                double[] averageErrors = new double[layerVariantErrors.length];

                for(int layerVariantAverageErrorIndex = 0; layerVariantAverageErrorIndex<layerVariantErrors.length; layerVariantAverageErrorIndex++){
                    averageErrors[layerVariantAverageErrorIndex] = 1;

                    for(double error: layerVariantErrors[layerVariantAverageErrorIndex]){
                        averageErrors[layerVariantAverageErrorIndex] -= error/layerVariantErrors[layerVariantAverageErrorIndex].length;
                    }
                }

                averageErrors = Arrays.stream(averageErrors).sorted().toArray();

                int countOfElementsInSequence = Arrays.stream(averageErrors).mapToInt(averageError->(int)Math.ceil(averageError*100)).sum();
                int sequenceIndexFrom = 0;
                double[] sequence = new double[countOfElementsInSequence];

                for(double averageError: averageErrors){
                    for(int sequenceIndex = sequenceIndexFrom; sequenceIndex < sequenceIndexFrom + (int) Math.ceil(averageError*100); sequenceIndex++){
                        sequence[sequenceIndex] = averageError;
                    }

                    sequenceIndexFrom += (int) Math.ceil(averageError*100);
                }

                int randomSequenceIndex = ThreadLocalRandom.current().nextInt(0, countOfElementsInSequence - 1);
                double randomError = Arrays.stream(averageErrors).max().getAsDouble();
                int errIndex = 0;

                for(double averageError: averageErrors){
                    if(averageError == randomError){
                        break;
                    }

                    errIndex++;
                }

                double[] changes = layerVariantErrors[errIndex];
                result[layerIndex][layerVariantIndex] = new double[changes.length];
                int neuronIndex = 0;

                for(double change: changes){
                    result[layerIndex][layerVariantIndex][neuronIndex] = biases[layerIndex][layerVariantIndex][neuronIndex] - change * (
                            outputs[layerIndex][layerVariantIndex][errIndex][neuronIndex] * (1-outputs[layerIndex][layerVariantIndex][errIndex][neuronIndex]) * teachScore);
                    neuronIndex++;
                }

                layerVariantIndex++;
            }

            layerIndex++;
        }

        return result;
    }

    private void addIndexesOdErrorsLessThanMaxError(double[][][] lastLayerErrors, double maxError, HashMap<String, Integer> indexesToExamplesCount){
        int layerVariantIndex = 0;

        for(double[][] layerVariantErrors: lastLayerErrors){
            int lastLayerVariantIndex = 0;

            for(double[] lastLayerVariantErrors: layerVariantErrors){
                boolean isAllErrorsLessThanMaxError = true;

                for(double error: lastLayerVariantErrors){
                    if(Math.abs(error) > maxError){
                        isAllErrorsLessThanMaxError = false;
                    }
                }

                if(isAllErrorsLessThanMaxError){
                    String index = layerVariantIndex + " " + lastLayerVariantIndex;

                    if(indexesToExamplesCount.containsKey(index)){
                        indexesToExamplesCount.put(index, indexesToExamplesCount.get(index) + 1);
                    } else {
                        indexesToExamplesCount.put(index, 1);
                    }
                }

                lastLayerVariantIndex++;
            }

            layerVariantIndex++;
        }
    }

    private boolean isExistsErrorsArrayWithAllErrorsLessThanMaxError(double[][][] lastLayerErrors, double maxError){
        for(double[][] layerVariantErrors: lastLayerErrors){
            for(double[] lastLayerVariantErrors: layerVariantErrors){
                boolean isAllErrorsLessThanMaxError = true;

                for(double error: lastLayerVariantErrors){
                    if(Math.abs(error) > maxError){
                        isAllErrorsLessThanMaxError = false;
                    }
                }

                if(isAllErrorsLessThanMaxError){
                    return true;
                }
            }
        }

        return false;
    }
}
