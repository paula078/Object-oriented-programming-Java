package agh.ics.oop.model;

import agh.ics.oop.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SimulationEngine{

    ExecutorService executorService;

    public SimulationEngine(){
        executorService = Executors.newFixedThreadPool(4);
    }

    public void addSimulation(Simulation simulation){
        executorService.submit(simulation);
    }
}
