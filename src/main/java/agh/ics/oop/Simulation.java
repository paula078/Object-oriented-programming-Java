package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.WorldMap;

import java.util.*;
import java.util.stream.Collectors;

public class Simulation implements Runnable {
    private final WorldMap map;
    private volatile boolean stopFlag = false; // spójny dostęp do pola w wielu wątkach
    private int numberOfDays=0;
    private int energyLossPerDay;
    private int startEnergy;

    public Simulation(WorldMap map, int numberOfAnimals, int startEnergy, int numberOfGenes, int energyLossPerDay) {
        this.map = map;
        Random random = new Random();
        this.energyLossPerDay = energyLossPerDay;
        this.startEnergy = startEnergy;
        for (int i = 0; i < numberOfAnimals; i++) {
            Vector2d randomPosition = new Vector2d(random.nextInt(map.getWidth()), random.nextInt(map.getHeight()));
            Animal animal = new Animal(randomPosition, startEnergy, numberOfGenes, energyLossPerDay);
            this.map.place(animal, map.getAnimals());
        }
    }
    public int getNumberOfDays(){
        return numberOfDays;
    }
    public int getStartEnergy() {
        return startEnergy;
    }

    public void setStopFlag(Boolean stopFlag) {
        this.stopFlag = stopFlag;
    }

    public void run(){
        int i=0;
        // symulacja trwa 1000 dni lub do momentu smierci wszystkich zwierząt
        while(i<1000) {
            if(!stopFlag) {
                numberOfDays++;
                map.removeDeadAnimals();
                map.moveAnimals();
                checkEvents();
                map.growGrass();
                map.mapChanged();
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    System.out.println("EXCEPTION");
                    throw new RuntimeException(e);
                }
                i++;
                if(map.getNumberOfAnimals() == 0){
                    break;
                }
            }
        }
    }
    // ponizsza funkcja opdowiada za rozmnazanie i zjadanie
    public void checkEvents(){
        for(int h=map.getHeight(); h>0; h--){
            for(int w=1;w<=map.getWidth(); w++){
                Vector2d position = new Vector2d(w-1, h-1);
                if(map.animalAt(position) != null){
                    handleAnimals(position);
                }
            }
        }
    }
    private void handleAnimals(Vector2d position){
        ArrayList<Animal> strongestAnimals;
        if(map.animalAt(position).size() > 1){
            strongestAnimals = solveConflict(map.animalAt(position));
            Animal animal1 = strongestAnimals.get(0);
            Animal animal2 = strongestAnimals.get(1);
            if(map.getGrasses().containsKey(position)){
                animal1.eat(map.getGrasses().remove(position));
            }
            Reproduce.reproduce(map, animal1, animal2, map.getEnergyThreshold(), map.getEnergyForChild(), map.getMinMutations(), map.getMaxMutations(), energyLossPerDay, map.getAnimals());
        }
        else {
            if (map.getGrasses().containsKey(position)) {
                Animal animal =  map.getAnimals().get(position).get(0);
                animal.eat(map.getGrasses().remove(position));
            }
        }
    }

    private ArrayList<Animal> solveConflict(List<Animal> animals2){
        ArrayList<Animal> strongAnimals = animals2.stream()
                .sorted(Comparator.comparingInt(Animal::getEnergy).reversed()
                        .thenComparing(Comparator.comparingInt(Animal::getLengthOfLife).reversed())
                        .thenComparing(Comparator.comparingInt(Animal::getNumberOfChildren).reversed()))
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Animal> result = new ArrayList<>();
        result.add(strongAnimals.get(0));
        result.add(strongAnimals.get(1));
        return result;
    }
}