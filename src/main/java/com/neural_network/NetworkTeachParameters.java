package com.neural_network;

public class NetworkTeachParameters {
    private double[][] trainingSets;
    private double[][] targetValues;
    private double teachScore;
    private double maxTeachTime;
    private double maxError;

    public double[][] getTrainingSets() {
        return trainingSets;
    }

    public NetworkTeachParameters setTrainingSets(double[][] trainingSets) {
        this.trainingSets = trainingSets;
        return this;
    }

    public double[][] getTargetValues() {
        return targetValues;
    }

    public double[] getTargetValues(int trainingSetIndex){
        return this.targetValues[trainingSetIndex];
    }

    public  NetworkTeachParameters setTargetValues(double[][] targetValues) {
        this.targetValues = targetValues;
        return this;
    }

    public double getTeachScore() {
        return teachScore;
    }

    public  NetworkTeachParameters setTeachScore(double teachScore) {
        this.teachScore = teachScore;
        return this;
    }

    public  double getMaxTeachTime() {
        return maxTeachTime;
    }

    public NetworkTeachParameters setMaxTeachTime(double maxTeachTime) {
        this.maxTeachTime = maxTeachTime;
        return this;
    }

    public  double getMaxError() {
        return maxError;
    }

    public NetworkTeachParameters setMaxError(double maxError) {
        this.maxError = maxError;
        return this;
    }
}
