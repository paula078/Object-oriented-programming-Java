package agh.ics.oop.model;

import java.util.ArrayList;
import java.util.Random;

public class Animal implements WorldElement {
    private MapDirection orientation;
    private Vector2d position;
    private int energy;
    private Integer[] genome;
    private int numberOfChildren;
    private int lengthOfLife;
    private int currentGene;
    private final int numberOfGenes;
    private final int energyLossPerDay;
    private final Random random = new Random();
    

    public Animal(Vector2d position, int startEnergy, int numberOfGenes, int energyLossPerDay){
        this.position = position;
        this.energy = startEnergy;
        this.numberOfChildren = 0;
        this.lengthOfLife = 0;
        this.numberOfGenes = numberOfGenes;
        this.energyLossPerDay = energyLossPerDay;
        this.genome = getRandomGenome();
        this.orientation = getRandomOrientation();
    }
    public Animal(Vector2d position, int startEnergy, int numberOfGenes, int energyLossPerDay, Integer[] genome){
        this(position, startEnergy, numberOfGenes, energyLossPerDay);
        this.genome = genome;
    }
    private Integer[] getRandomGenome(){
        Integer[] genome = new Integer[numberOfGenes];
        for(int i=0; i<genome.length; i++){
            genome[i] = random.nextInt(MapDirection.values().length);
        }
        return genome;
    }
    private MapDirection getRandomOrientation() {
        MapDirection[] directions = MapDirection.values();
        int randomIndex = random.nextInt(directions.length);
        return directions[randomIndex];
    }
    public Vector2d position(){
        return position;
    }
    public MapDirection getOrientation(){
        return orientation;
    }
    public int getEnergy(){
        return energy;
    }
    public Integer[] getGenome(){
        return genome;
    }
    public int getNumberOfChildren(){
        return numberOfChildren;
    }
    public int getLengthOfLife(){
        return lengthOfLife;
    }
    @Override
    public String toString(){
        return switch(orientation){
            case NORTH -> "N";
            case NORTH_EAST -> "NE";
            case EAST -> "E";
            case SOUTH_EAST -> "SE";
            case SOUTH -> "S";
            case SOUTH_WEST -> "SW";
            case WEST -> "W";
            case NORTH_WEST -> "NW";
        };
    }
    public void incrementLengthOfLife(){
        this.lengthOfLife++;
    }
    public void move(MoveValidator validator, int width, boolean mutationVariant){
        Vector2d newPosition = position.add(orientation.toUnitVector());
        if (validator.canMoveTo(newPosition)){
            this.position = new Vector2d((newPosition.getX() + width) % width, newPosition.getY());
        }
        else{
            changeOrientation(4);
        }
        energy -= energyLossPerDay;
        changeGene(mutationVariant);
        changeOrientation(currentGene);
    }
    private void changeOrientation(int numberOfSpins){
        for(int i=0 ;i<numberOfSpins; i++){
            orientation = orientation.next();
        }
    }
    public void eat(Grass grass){
        energy += grass.energy();
    }
    public void loseEnergy(int energy){
        this.energy -= energy;
    }

    public void incrementChildrenNumber(){numberOfChildren++; }
    public boolean perceptivenessTest(){
        int shoot = random.nextInt(100);
        return shoot < 20;
    }

    public void changeGene(boolean maddness) {
        if (maddness && perceptivenessTest()) {
            currentGene = random.nextInt(numberOfGenes);
        } else {
            currentGene += 1;
            currentGene %= numberOfGenes;
        }
    }

}

