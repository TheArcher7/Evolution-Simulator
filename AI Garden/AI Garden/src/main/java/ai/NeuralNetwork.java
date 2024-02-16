package main.java.ai;

import java.util.Random;

//references
//https://youtu.be/cpYRGNT41Go?si=7gqZuaSCCe5LO3x-

public class NeuralNetwork {
    private HiddenLayer[] hiddenLayers;
    private double[][] activations;

    private int generationVersion;
    private int mutationCount = 0;
    private String filename;

    // Constructor for deserialization
    //private NeuralNetwork(){}

    // Constructor
    public NeuralNetwork(int... layers){
        activations = new double[layers.length][];
        hiddenLayers = new HiddenLayer[layers.length -1];

        activations[0] = new double[layers[0]];

        for (int i = 1; i < layers.length; i++) {
            activations[i] = new double[layers[i]];
            hiddenLayers[i-1] = new HiddenLayer(layers[i], layers[i-1]);
        }
    }

    // Constructor for copying a NeuralNetwork to a new NeuralNetwork
    public NeuralNetwork(NeuralNetwork other) {
        generationVersion = other.generationVersion;
        filename = other.filename;

        double[][] otherActivations = other.activations;
        activations = new double[otherActivations.length][];
        activations[0] = new double[otherActivations[0].length];

        HiddenLayer[] otherHiddenLayers = other.hiddenLayers;
        hiddenLayers = new HiddenLayer[activations.length - 1];

        for (int i = 1; i < activations.length; i++) {
            activations[i] = new double[otherActivations[i].length];
            hiddenLayers[i - 1] = new HiddenLayer(otherHiddenLayers[i - 1]);
        }
    }

    // TODO create a constructor for copying a NN with a sight mutation
    /*
    public NeuralNetwork(NeuralNetwork other, double mutationRate){
        // copy the weights and biases
        // add or subtract a little from the weights and biases based on the mutation rate
        // use the error function to keep the final weights and biases within -1 and 1
    } */

    // Constructor for loading a neural network from a file
    public NeuralNetwork(String filename) {
        // TODO load NeuralNetwork from a file
        this.filename = filename;
    }

    public double[] process(double[] inputs) {
        if (inputs.length != activations[0].length) {
            throw new IllegalArgumentException("Invalid number of inputs " + inputs.length);
        }

        // takes an array of inputs that we want to apply to the neural network and performs the calculations
        System.arraycopy(inputs, 0, activations[0], 0, inputs.length);

        for (int i = 0; i < activations.length - 1; i++) {
            activations[i + 1] = hiddenLayers[i].multiplyAddBias(activations[i]);
            //relu(activations[i + 1]);
            sigmoid(activations[i + 1]);
        }

        return getOutputs();
    }

    /*
    private void relu(double[] activation) {
        for (int i = 0; i < activation.length; i++) {
            activation[i] = Math.max(0, activation[i]);
        }
    }
    */

    private void sigmoid(double[] activation) {
        for (int i = 0; i < activation.length; i++) {
            activation[i] = 1.0 / (1 + Math.exp(-activation[i]));
        }
    }

    public double[] getOutputs(){
        return activations[activations.length - 1];
    }

    @Deprecated
    public NeuralNetwork crossover(NeuralNetwork other) { //There is something wrong with this method, but I can't figure out what or why
        NeuralNetwork clone = new NeuralNetwork(this);

        HiddenLayer[] hiddenLayers = clone.hiddenLayers;
        HiddenLayer[] otherHiddenLayers = other.getHiddenLayers();

        for (int layer = 0; layer < hiddenLayers.length; layer++) {
            HiddenLayer hiddenLayer = hiddenLayers[layer];
            HiddenLayer otherHiddenLayer = otherHiddenLayers[layer];

            double[][] weights = hiddenLayer.getWeights();
            double[][] otherWeights = otherHiddenLayer.getWeights();

            int rows = weights.length;
            int cols = weights[0].length;
            int weightSize = rows * cols;

            for (int crossOverPoint = (int) (Math.random() * weightSize); crossOverPoint < weightSize; crossOverPoint++) {
                weights[crossOverPoint / cols][crossOverPoint % cols] = otherWeights[crossOverPoint / cols][crossOverPoint % cols];
            }

            double[] biases = hiddenLayer.getBiases();
            double[] otherBiases = otherHiddenLayer.getBiases();
            for (int crossOverPoint = (int) (Math.random() * biases.length); crossOverPoint < biases.length; crossOverPoint++) {
                biases[crossOverPoint] = otherBiases[crossOverPoint];
            }
        }

        return clone;
    }

    /**
     * Requires {@code double} values with a positive sign, greater
     * than or equal to {@code 0.0} and less than {@code 1.0}.
     * Mutates the weights and biases of the neural network's hidden layers.
     */
    public void mutate(double rate, double strength) {
        Random random = new Random();

        for (HiddenLayer hiddenLayer : hiddenLayers) {
            double[][] weights = hiddenLayer.getWeights();
            for (int i = 0; i < weights.length; i++) {
                for (int j = 0; j < weights[0].length; j++) {
                    if (Math.random() < rate) {
                        weights[i][j] += random.nextGaussian() * strength;
                        mutationCount++;
                    }
                }
            }

            double[] biases = hiddenLayer.getBiases();
            for (int i = 0; i < biases.length; i++) {
                if (Math.random() < rate) {
                    biases[i] += random.nextGaussian() * strength;
                    mutationCount++;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "NeuralNetwork toString() not implemented";
    }

    public void printActivations(){
        System.out.println("Printing activations array:");
        for (int i = 0; i < activations.length; i++) {
            System.out.print("L" + i + "= ");
            for (int j = 0; j < activations[i].length; j++) {
                System.out.print(activations[i][j] + " ");
            }
            System.out.println();
        }
    }

    public HiddenLayer[] getHiddenLayers() {
        return hiddenLayers;
    }
}
