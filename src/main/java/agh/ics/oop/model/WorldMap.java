package agh.ics.oop.model;

import java.io.IOException;
import java.util.HashMap;

import java.util.*;

public class WorldMap implements MoveValidator {

    private final List<MapChangeListener> observers = new LinkedList<>();
    private Map<Vector2d, List<Animal>> animals = Collections.synchronizedMap(new HashMap<>());
    private final Map<Vector2d, Grass> grasses = Collections.synchronizedMap(new HashMap<>());
    private GenomesAnalyzer genomesAnalyzer;
    private final int width;
    private final int height;
    private final int eachDayGrassNumber;
    private final int energyOfGrass;
    private final int energyThreshold;
    private final int energyForChild;
    private final int energyLossPerDay;
    private final int minMutations;
    private final int maxMutations;
    private int numberOfAnimals=0;
    private final boolean madness;
    private int deadAnimals;
    private int summedLengthOfLife;
    private int numberOfChildren;
    private double averageEnergy;
    private List<Integer[]> genomesList = new ArrayList<>();
    private Integer [] mostPopularGenome;
    private final PlantsGenerator plantsGenerator;

    public WorldMap(int width, int height, int initialGrassNumber, int eachDayGrassNumber, int energyOfGrass,
               int energyThreshold, int energyForChild, int energyLossPerDay, int minMutations,
               int maxMutations, boolean mutationVariant, boolean poisonedFruits){
        this.width = width;
        this.height = height;
        this.eachDayGrassNumber = eachDayGrassNumber;
        this.energyOfGrass = energyOfGrass;
        this.energyThreshold = energyThreshold;
        this.energyForChild = energyForChild;
        this.energyLossPerDay = energyLossPerDay;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        this.madness = mutationVariant;
        if(poisonedFruits){
            plantsGenerator = new SquarePositionsGenerator(energyOfGrass,this);
        }
        else{
            plantsGenerator = new EquatorPositionsGenerator(energyOfGrass,this);
        }
        plantsGenerator.growGrass(initialGrassNumber);
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public int getEnergyThreshold(){
        return energyThreshold;
    }
    public int getEnergyForChild() {
        return energyForChild;
    }
    public int getMinMutations() {
        return minMutations;
    }
    public int getMaxMutations() {
        return maxMutations;
    }
    public int getNumberOfAnimals() {return numberOfAnimals;}
    public PlantsGenerator getPlantsGenerator(){
        return plantsGenerator;
    }
    public double getAverageEnergy(){
        return averageEnergy;
    }
    public List<Integer[]> getGenomesList() {
        return genomesList;
    }
    public Integer[] getMostPopularGenome() {
        return mostPopularGenome;
    }
    public Map<Vector2d, List<Animal>> getAnimals(){
        return animals;
    }
    public ArrayList<Animal> getAnimalsAtPosition(Vector2d position){
        List<Animal> animalsAtPosition = animals.get(position);
        return new ArrayList<>(animalsAtPosition);
    }
    public Map<Vector2d, Grass> getGrasses(){
        return grasses;
    }

    public void place(Animal animal, Map<Vector2d, List<Animal>> animals) {
        Vector2d position = animal.position();
        // Pobranie listy zwierzat na danej pozycji lub utworzenie nowej listy
        List<Animal> animalsAtPosition = animals.getOrDefault(position, new ArrayList<>());
        // Dodanie nowego zwierzecia do listy
        animalsAtPosition.add(animal);
        // Umieszczenie zaktualizowanej listy zwierzat z powrotem na mapie
        animals.put(position, animalsAtPosition);
        numberOfAnimals++;
    }
    public void moveAnimals(){
        numberOfAnimals=0;
        numberOfChildren=0;
        double summedEnergy=0;
        genomesList.clear();
        HashMap<Vector2d, List<Animal>> animalsAfterMoves = new HashMap<>();
        for(List<Animal> animalArrayList: animals.values()){
            for(Animal animal: animalArrayList){
                animal.move(this, width, madness);
                animal.incrementLengthOfLife();
                incrementGenomesList(animal.getGenome());
                summedEnergy += animal.getEnergy();
                numberOfChildren += animal.getNumberOfChildren();
                place(animal, animalsAfterMoves);
            }
        }
        averageEnergy = (numberOfAnimals > 0) ? summedEnergy / numberOfAnimals : 0;
        genomesAnalyzer = new GenomesAnalyzer(getGenomesList());
        mostPopularGenome = genomesAnalyzer.findMostPopularGenome();
        animals = new HashMap<>(animalsAfterMoves);
    }
    public void growGrass(){
        plantsGenerator.growGrass(eachDayGrassNumber);
    }
    public double getAverageLengthOfLife(){
        if(deadAnimals > 0)
            return (double) summedLengthOfLife/deadAnimals;
        else return 0;
    }
    public double getAverageNumberOfChildren(){
        if(numberOfAnimals > 0){
            return (double) numberOfChildren/numberOfAnimals;
        }
        else return 0;
    }
    private void incrementGenomesList(Integer[] genome){
        genomesList.add(genome);
    }
    public void removeDeadAnimals(){
        for (List<Animal> animalsAtPosition : animals.values()) {
            animalsAtPosition.removeIf(animal -> {
                if (animal.getEnergy() <= 0) {
                    deadAnimals++;
                    summedLengthOfLife+=animal.getLengthOfLife();
                    return true;
                }
                return false;
            });
        }
    }
    public List<Animal> animalAt(Vector2d position) {
        if(isOccupied(position)){
            if(animals.containsKey(position)) return animals.get(position);
        }
        return null;
    }
    public Grass grassAt(Vector2d position) {
        if(isOccupied(position) && grasses.containsKey(position)){
            return grasses.get(position);
        }
        return null;
    }

    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position) || grasses.containsKey(position);
    }
    public void addGrass(Vector2d position, int energy) {
        grasses.put(position, new Grass(position, energy));
    }
    public boolean canMoveTo(Vector2d position) {
        return position.getY() < height && position.getY() >= 0;
    }
    public void registerObserver(MapChangeListener observer){
        observers.add(observer);
    }
    public void deregisterObserver(MapChangeListener observer){
        observers.remove(observer);
    }
    public void mapChanged(){
        observers.forEach(observers -> {
            try {
                observers.mapChanged(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
