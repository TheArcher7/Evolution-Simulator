package main.java.ai;

import java.text.DecimalFormat;

//references
//https://youtu.be/cpYRGNT41Go?si=7gqZuaSCCe5LO3x-

public class HiddenLayer {
    private double[][] weights;
    private double[] biases;

    // Constructor for deserialization
    //private HiddenLayer() {}

    // Constructor
    public HiddenLayer(int currentLayerSize, int previousLayerSize) {
        weights = new double[currentLayerSize][previousLayerSize];
        biases = new double[currentLayerSize];
        initRandom();
    }

    // Constructor for copying a HiddenLayer to a new HiddenLayer
    public HiddenLayer(HiddenLayer other) {
        double[][] otherWeights = other.weights;
        double[] otherBiases = other.biases;
        weights = new double[otherWeights.length][otherWeights[0].length];
        biases = new double[otherBiases.length];

        for (int i = 0; i < weights.length; i++) {
            biases[i] = otherBiases[i];
            System.arraycopy(otherWeights[i], 0, weights[i], 0, weights[0].length);
        }
    }

    private void initRandom() {
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[0].length; j++) {
                weights[i][j] = 2 * Math.random() - 1; // [-1 .. 1)
            }
        }

        for (int i = 0; i < biases.length; i++) {
            biases[i] = 2 * Math.random() - 1; // [-1 .. 1)
        }
    }

    public double[] multiplyAddBias(double[] activation) {
        if (activation.length != weights[0].length) {
            throw new IllegalArgumentException("Invalid activation length " + activation.length);
        }

        // performs the matrix multiplication needed to calculate an input
        double[] result = new double[weights.length];
        for (int i = 0; i < weights.length; i++) {
            double sum = 0;
            for (int j = 0; j < weights[0].length; j++) {
                sum += activation[j] * weights[i][j];
            }
            sum += biases[i];
            result[i] = sum;
        }

        return result;
    }

    @Override
    public String toString() {
        return toString(""); 
    }

    public String toString(String indent) {
        StringBuilder builder = new StringBuilder();

        //Format the weights first
        int wLength = weights.length;
        int w0Length = weights[0].length;
        builder.append(indent);
        builder.append(String.format("hl_Weights %d %d", wLength, w0Length));
        builder.append(System.lineSeparator());
        for (int i = 0; i < wLength; i++) {
            builder.append(indent);
            for (int j = 0; j < w0Length; j++) {
                if(j > 0)
                    builder.append(",");
                builder.append(weights[i][j]);
            }
            builder.append(System.lineSeparator());
        }

        //Format the biases second
        int bLength = biases.length;
        builder.append(indent);
        builder.append(String.format("hl_Biases %d", bLength));
        builder.append(System.lineSeparator());
        for (int i = 0; i < bLength; i++) {
            builder.append(indent);
            if(i > 0)
                builder.append(",");
            builder.append(biases[i]);
        }
        builder.append(System.lineSeparator());
        return builder.toString(); 
    }

    public double[] getBiases() {
        return biases;
    }

    public double[][] getWeights() {
        return weights;
    }

}
