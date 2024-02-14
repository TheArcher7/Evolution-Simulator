package test;

import org.junit.Test;
import java.util.Random;
import main.java.ai.NeuralNetwork;

public class NeuralNetworkTest {
    @Test
    public void BasicTestOfNeuralNetwork(){
        NeuralNetwork neuralNetwork = new NeuralNetwork(4, 3, 2);

        Random random = new Random();
        int counter = 0;
        while (counter++ < 10) {
            double[] inputs = new double[4];
            for (int i = 0; i < inputs.length; i++) {
                inputs[i] += random.nextGaussian() * 0.5;
            }
            printArray("Inputs", inputs);
            neuralNetwork.process(inputs);
            double[] outputs = neuralNetwork.getOutputs();
            printArray("outputs", outputs);

            neuralNetwork.printActivations(); System.out.println("");
        }
    }

    private void printArray(String title, double[] array){
        System.out.println(title);
        for (double d : array) {
            System.out.println(d);
        }
        System.out.println("");
    }
}
