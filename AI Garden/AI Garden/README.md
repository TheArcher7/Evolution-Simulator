## Getting Started

Welcome to the AI Garden, an evolution simulator.
This project was created by Micah Mock as a capstone project for CSU Global in Spring of 2024

Micah Mock

Colorado State University Global

CSC 480 Comupter Science Capstone

Professor Chintan Thakkar

Spring 2024

Contact Info:
micahmock2020@gmail.com


## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

## Dependency Management

Github link: https://github.com/TheArcher7/Evolution-Simulator/tree/temp-branch/AI%20Garden/AI%20Garden

## Project Documentation

Updated 2/3/2024

The AI class is an abstract class that holds the basic methods that will be used in all AI created from it. Essentially, every organism in the simulator will have coordinates, a hitbox, and vision lines. If their vision lines intersect with the hitbox of another organism or food item, then that is something they can “see” and it will register as an input for their AI to use. It will have a buildAI() method.
The Basic AI will use standard logic to interpret input data and decide how to move. It will be useful for testing the simulator.
The Custom AI will be an AI intended for customization and experimental purposes. It will likely be out of scope for this project, but useful for future versions of the simulation, such as if the user wants to create their own AI from a special screen.
The DoNothingAI is a class containing an AI that has no logic dictating how it should handle input and output. It is useful for testing the UI.
The EvolvingAI is a class containing a neural network, as well as the weights for it, stored in variables. It will have methods for saving and loading AI to a file, and a method for mutating its own AI to pass on to an offspring. 

There is one controller object for this project, the WorldController, which will handle zooming and panning with the mouse, and update the WorldModel. It will process all entity’s hitboxes, feeding them inputs and interpreting their outputs to update the simulation. It also triggers reproduction.

The BaseOrganism will contain variables such as Energy and MaxEnergy. The Carnivore and Herbivore classes will inherit these variables and methods. It will also store a reference to its AI.
The Food class will contain attributes about the food items, such as their (x,y) position and value.
The WorldFactory will be a class for initializing the world, including its size and the organisms.

The LogElement will be a class containing the data observed at a single moment in time, such as the population of the world and the average age of the organisms.
The Statistics class will be a class for holding an ArrayList of LogElements, providing helper methods for adding a LogElement and reading the items inside. It provides functionality for calculating things such as the average age, and also stores the most “successful” organism for the entire time of the simulation.

The Util folder contains classes helpful for the serialization of data, which is useful for saving to files. The serializer classes can read CSV files to load data from a file, and save the data from the simulator as a CSV. It also can contain any helper classes that may be needed in the future.

The various views for this program will contain GUI data for various screens. The CreateWorldView will be a pop-up dialog that allows the user to define aspects about the world and the organisms that will spawn in it, such as the area size, the organisms’ starting attributes, as well as a submit button that will reload the world.
The MainWindow will instantiate the program with a menu bar. The user will be able to see attributes from the simulation in real time, and be able to adjust factors such as the food rate in real time to influence the simulation and thus affect the AI. The menu bar will contain dropdown items for exiting the program, saving it to a file, seeing the detailed statistics screen over time, and creating a new world with creatures. The user will also be able to load an AI from a file, or toggle debug mode which renders entities as hitboxes and vision lines instead.
The Statistics screen will contain details from across the whole time of the simulation. Data such as the population over time, will be displayed in a line chart. A line chart for measuring the success of an organism will be provided, and there will be a button to save the AI to a file.
The WorldInputListener class will help the program interpret input from the mouse.
Lastly, the WorldView will enable the program to interpret the input from the mouse as zooming and panning and enable the entities to be rendered on the screen to the correct scale. If debug mode is enabled, then the entities will be rendered with hitboxes and vision lines, versus colored circles.

