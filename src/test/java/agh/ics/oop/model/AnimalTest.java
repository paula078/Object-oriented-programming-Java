package agh.ics.oop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {
    private Animal animal;
    private Random random = new Random();
    Vector2d initialPosition = new Vector2d(2,2);
    int startEnergy = 20;
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
    @BeforeEach
    void setUp() {
        animal = new Animal(initialPosition, startEnergy, numberOfGenes, energyLossPerDay);
        initMap = new Map(mapWidth, mapHeight, initialGrassNumber,eachDayGrassNumber, energyOfEatenGrass,
                energyRequired, energyConsumed, energyLossPerDay, minMutations, maxMutations, mutationVariant, poisonedFruits);

    }

    @Test
    void getPosition() {
        Vector2d expectedPosition = new Vector2d(2, 2);
        assertEquals(expectedPosition, animal.position());
    }

    @Test
    void getOrientation() {
        MapDirection[] possibleOrientations = MapDirection.values();

        MapDirection animalOrientation = animal.getOrientation();
        //System.out.println(animalOrientation);
        assertTrue(Arrays.asList(possibleOrientations).contains(animalOrientation),
                    "Animal orientation should be one of the possible orientations");
    }

    @Test
    void getEnergy() {
        int energy = 20;
        assertEquals(energy, animal.getEnergy());
    }

    @Test
    void getGenome() {
        Integer[] genome = animal.getGenome();
        for (int gene : genome) {
        assertTrue((gene >= 0) && (gene < MapDirection.values().length));
        }
    }

    @Test
    void getNumberOfChildren() {
        animal.incrementChildrenNumber();
        animal.incrementChildrenNumber();
        assertEquals(animal.getNumberOfChildren(), 2);
    }

    @Test
    void getLengthOfLife() {
        animal.incrementLengthOfLife();
        assertEquals(animal.getLengthOfLife(), 1);
    }
}