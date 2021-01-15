package com.utils;

import java.util.List;

public class ArrayUtils {

    public static int[] createAndFill(int fromValue, int toValue){
        int[] result = new int[toValue - fromValue + 1];

        for(int arrayIndex = 0; arrayIndex < result.length; arrayIndex++){
            int value = arrayIndex + fromValue;
            result[arrayIndex] = value;
        }

        return result;
    }

    public static double[][] toMultipleArray(double[] values){
        double[][] result = new double[values.length][];

        for(int valueIndex = 0; valueIndex < values.length; valueIndex++){
            result[valueIndex] = new double[1];
            result[valueIndex][0] = values[valueIndex];
        }

        return result;
    }

    public static double[][] toMultipleArray(int count, double[] values){
        double[][] result = new double[count][];

        for(int valueIndex = 0; valueIndex < count; valueIndex++){
            result[valueIndex] = values;
        }

        return result;
    }

    public static double[] toPrimitiveDoubleArray(List<Double> listOfClasses){
        double[] result = new double[listOfClasses.size()];

        for (int index = 0; index < listOfClasses.size(); index++) {
            result[index] = listOfClasses.get(index);
        }

        return result;
    }

    public static int[] toPrimitiveIntegerArray(List<Integer> listOfClasses){
        int[] result = new int[listOfClasses.size()];

        for (int index = 0; index < listOfClasses.size(); index++) {
            result[index] = listOfClasses.get(index);
        }

        return result;
    }

    public  static double[][] toArrayOfDoubleArrays(List<double[]> listOfArrays){
        double[][] result = new double[listOfArrays.size()][];

        for(int index = 0; index < listOfArrays.size(); index++){
            result[index] = listOfArrays.get(index);
        }

        return result;
    }
}
