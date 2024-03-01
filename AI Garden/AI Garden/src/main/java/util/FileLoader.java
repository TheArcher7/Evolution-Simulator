package main.java.util;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import main.java.ai.HiddenLayer;
import main.java.model.BaseOrganism;
import main.java.model.WorldModel;

public class FileLoader {
    public static BaseOrganism readOrganismFromFile(File filename, WorldModel model) throws IOException{
        //set the coordinates to spawn at (0,0)
        Pos p = new Pos(model.width / 2.0, model.height / 2.0);
        //creating the organism
        BaseOrganism o = new BaseOrganism(p, model);

        //setting the neural network and hidden layers from file

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        try{
            String lineString = reader.readLine();
            String[] words = lineString.split("\\s+");
            if (!words[0].equals("BaseOrganism")) {
                System.out.println("WARNING. File not formatted as expected. Found \"" + words[0] + "\" instead of \"BaseOrganism.\" Attempting to read anyway.");
            }

            /*
             * Read the baseOrganism data
             */

             lineString = reader.readLine();
            // Split the line by whitespace
            String[] tokens = lineString.split("\\s+");

            // Initialize variables equal to defaults in BaseOrganism's constructor
            double weight = o.weight;
            double maxEnergy = o.maxEnergy;
            int age = 0;
            double size = o.size;
            int generation = 0;
            double energyNeededToReproduce = o.energyNeededToReproduce;
            double weightNeededToReproduce = o.weightNeededToReproduce;
            int colorR = o.r;
            int colorG = o.g;
            int colorB = o.b;
            double maxVelocity = o.maxVelocity;
            double maxDeltaDirection = o.maxDeltaDirection;
            double visionRadius = o.visionRadius;
            double[] phiVisionDirection = o.phiVisionDirection; //file may have more or less elements, be carefull

            // Iterate through tokens to get values stored on file
            for (int i = 0; i < tokens.length; i += 2) {
                switch (tokens[i]) {
                    case "weight":
                        weight = Double.parseDouble(tokens[i + 1]);
                        break;
                    case "maxEnergy":
                        maxEnergy = Double.parseDouble(tokens[i + 1]);
                        break;
                    case "age":
                        //age = Integer.parseInt(tokens[i + 1]); //does loading from a file start it from scratch? yes.
                        break;
                    case "size":
                        size = Double.parseDouble(tokens[i + 1]);
                        break;
                    case "generation":
                        //generation = Integer.parseInt(tokens[i + 1]); //newly loaded organisms start without knowledge of previous generations
                        break;
                    case "energyNeededToReproduce":
                        energyNeededToReproduce = Double.parseDouble(tokens[i + 1]);
                        break;
                    case "weightNeededToReproduce":
                        weightNeededToReproduce = Double.parseDouble(tokens[i + 1]);
                        break;
                    case "colorR":
                        colorR = Integer.parseInt(tokens[i + 1]);
                        break;
                    case "colorG":
                        colorG = Integer.parseInt(tokens[i + 1]);
                        break;
                    case "colorB":
                        colorB = Integer.parseInt(tokens[i + 1]);
                        break;
                    case "maxVelocity":
                        maxVelocity = Double.parseDouble(tokens[i + 1]);
                        break;
                    case "maxDeltaDirection":
                        maxDeltaDirection = Double.parseDouble(tokens[i + 1]);
                        break;
                    case "visionRadius":
                        visionRadius = Double.parseDouble(tokens[i + 1]);
                        break;
                    case "phiVisionDirection[]":
                        int elements = Integer.parseInt(tokens[i+1]);
                        phiVisionDirection = new double[elements];
                        for (int j = 0; j < elements; j++) {
                            phiVisionDirection[j] = Double.parseDouble(tokens[i + 2 + j]);
                        }
                        break;
                    default:
                        break;
                }
            }

            // Set variables in new organism equal to values found on file
            o.weight = weight;
            o.maxEnergy = maxEnergy;
            o.age = age;
            o.size = size;
            o.generation = generation;
            o.energyNeededToReproduce = energyNeededToReproduce;
            o.weightNeededToReproduce = weightNeededToReproduce;
            o.r = colorR;
            o.g = colorG;
            o.b = colorB;
            o.maxVelocity = maxVelocity;
            o.maxDeltaDirection = maxDeltaDirection;
            o.visionRadius = visionRadius;
            o.phiVisionDirection = phiVisionDirection;

            /*
             * read the AI data
             */
            lineString = reader.readLine();
            words = lineString.split("\\s+");
            if (!words[0].equals("NeuralNetwork")) {
                System.out.println("WARNING. File not formatted as expected. Found \"" + words[0] + "\" instead of \"NeuralNetwork.\" Attempting to read anyway.");
            }
            int hiddenLayerCount = Integer.parseInt(words[1]);

            HiddenLayer[] layers = new HiddenLayer[hiddenLayerCount];

            for (int i = 0; i < hiddenLayerCount; i++) {
                lineString = reader.readLine();
                words = lineString.split(" ");
                if (!words[0].equals("hl_Weights")) {
                    System.out.println("WARNING. File not formatted as expected. Found \"" + words[0] + "\" instead of \"hl_Weights.\" Attempting to read anyway.");
                }
                int weightsLinesToRead = Integer.parseInt(words[1]); //current layer size
                int weightsPerLine = Integer.parseInt(words[2]); //previous layer size (num biases)
                
                double[][] weights = new double[weightsLinesToRead][weightsPerLine];
                for (int j = 0; j < weightsLinesToRead; j++) {
                    lineString = reader.readLine();
                    words = lineString.split(",");
                    for (int k = 0; k < words.length; k++){
                        weights[j][k] = Double.parseDouble(words[k]);
                    }
                }

                
                lineString = reader.readLine();
                words = lineString.split(" ");
                if (!words[0].equals("hl_Biases")) {
                    System.out.println("WARNING. File not formatted as expected. Found \"" + words[0] + "\" instead of \"hl_Biases.\" Attempting to read anyway.");
                }
                int numBiases = Integer.parseInt(words[1]);
                
                double[] biases = new double[numBiases];
                lineString = reader.readLine();
                words = lineString.split(",");
                for (int j = 0; j < words.length; j++){
                    biases[j] = Double.parseDouble(words[j]);
                }
                
                HiddenLayer hl = new HiddenLayer(weights, biases);
                layers[i] = hl;
            }

            o.ai.neuralNetwork.setHiddenLayers(layers);

        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
        finally {
            reader.close();
        }

        return o;
    }
}
