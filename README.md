# Path Integral Monte Carlo Simulation (PIMC)

## Overview

The **PIMC** (Path Integral Monte Carlo) project simulates quantum particle dynamics using Feynman’s Path Integral formulation, a fundamental approach in quantum mechanics that models particles as a collection of paths. This project provides an interactive platform to explore quantum behavior under different potential fields through real-time visualizations of particle trajectories, probability distributions, and energy states. This project is written in Java.

## Features

- **Flexible Potential Selection**:
  - Harmonic Oscillator
  - Morse Potential
  - Double-Well Potential
  - Anharmonic Oscillator
- **Monte Carlo Simulation**: Implements the Monte Carlo method to accurately sample particle paths and calculate quantum properties like wavefunctions and ground state energies.
- **Real-Time Visualization**: Watch the quantum paths evolve and observe how the system adapts over time.
- **User-Controlled Parameters**: Modify the number of segments, total simulation time, displacement range, and potential type to explore various quantum scenarios.

## User Interface

The graphical interface is built with the Open Source Physics (OSP) library, offering a responsive environment for controlling and observing the simulation. The main components are as follows:

### Path Frame

The **Path Frame** visualizes the particle’s quantum path as a connected line across discrete segments. This graph provides a dynamic representation of the particle's movement, illustrating how the path adapts with each simulation step.

### Probability Distribution Frame

The **Probability Distribution Frame** depicts the ground state probability distribution, \( P(x) \). This graph shows the likelihood of finding the particle at specific positions and evolves in real time, allowing users to observe how potential fields influence quantum uncertainty.

### Wave Function Frame

The **Wave Function Frame** plots the ground state wavefunction density, \( |Ψ_0(x)|^2 \). This visualization represents the particle’s spatial probability, highlighting regions where the particle is most likely to be found. 

### Energy Frame

The **Energy Frame** tracks changes in the particle’s ground state energy \( E_0 \) as the simulation progresses. It offers insights into how the energy varies with different potentials and simulation parameters, providing a view into the system’s stability and the impact of quantum interactions.

### Control Panel

The **Control Panel** lets you adjust simulation parameters to explore various quantum behaviors. Parameters include:
- **Total Number of Segments (N)**: Controls the path detail by setting the number of discrete points along the path.
- **Total Time (τ)**: Defines the total time for the simulation.
- **Maximum Displacement (δ)**: Limits the allowed change in displacement per simulation step.
- **Potential Type (s)**: Switch between potential functions to examine different quantum systems.

## Running the Simulation

To start the simulation, follow these steps:

1. Install the [Open Source Physics (OSP) library](https://www.compadre.org/osp/items/detail.cfm?ID=7375).
2. Compile and run `PIMCApp.java`. The simulation will launch with default settings, displaying the Path, Probability Distribution, Wave Function, and Energy frames.
3. Adjust parameters using the control panel and observe the impact on the quantum path and associated properties.

## License

This project is licensed under the MIT License.