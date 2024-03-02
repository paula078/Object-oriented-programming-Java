package agh.ics.oop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;


class MapTest {


    private Animal animal1;
    private Animal animal2;
    private Animal animal3;
    private Random random = new Random();
    Vector2d initialPosition1 = new Vector2d(2,2);
    Vector2d initialPosition2 = new Vector2d(2,2);
    Vector2d initialPosition3 = new Vector2d(3,3);
    int startEnergy1 = 20;
    int startEnergy2 = 10;
    int startEnergy3 = 20;
    int numberOfGenes = 6;
    int energyLossPerDay = 1;
    Map initMap;

    int mapWidth = 8;
    int mapHeight = 8;
    int initialGrassNumber = 6;
    int eachDayGrassNumber = 3;
    int energyOfEatenGrass = 1;
    int energyRequired = 10;
    int energyConsumed = 2;
    int minMutations = 1;
    int maxMutations = 3;
    boolean mutationVariant = false;
    boolean poisonedFruits = false;
    HashMap<Vector2d, List<Animal>> animals;

    @BeforeEach
    void setUp() {
        animal1 = new Animal(initialPosition1, startEnergy1, numberOfGenes, energyLossPerDay);
        animal2 = new Animal(initialPosition2, startEnergy2, numberOfGenes, energyLossPerDay);
        animal3 = new Animal(initialPosition3, startEnergy3, numberOfGenes, energyLossPerDay);
        initMap = new Map(mapWidth, mapHeight, initialGrassNumber,eachDayGrassNumber, energyOfEatenGrass,
                energyRequired, energyConsumed, energyLossPerDay, minMutations, maxMutations, mutationVariant, poisonedFruits);
        animals = new HashMap<>();
    }

    @Test
    void moveAnimals() {
//        initMap.place(animal1, animals);
//        initMap.place(animal2, animals);
//        initMap.place(animal3, animals);
//        Vector2d position1 = animal1.position();
//        Vector2d position2 = animal2.position();
//        Vector2d position3 = animal3.position();
//        initMap.moveAnimals();
//
//        assertNotEquals(position1, animal1.position());
//        assertNotEquals(position2, animal2.position());
//        assertNotEquals(position3, animal3.position());
    }

    @Test
    void reproduce() {
    }

    @Test
    void removeDeadAnimals() {
    }
}