package agh.ics.oop.model;

import java.util.*;

public class Reproduce {

    static Random random = new Random();
    public static boolean canReproduce(Animal parent1, Animal parent2, int energyThreshold) {
        // Zwierzeta musza miec odpowiednia ilosc energii, aby się rozmnazac
        return (parent1.getEnergy() >= energyThreshold) && (parent2.getEnergy() >= energyThreshold);
    }


    public static void reproduce(WorldMap map, Animal parent1, Animal parent2, int energyThreshold, int energyForChild, int minMutations, int maxMutations, int energyLossPerDay, Map<Vector2d, List<Animal>> animals){
        if (canReproduce(parent1, parent2, energyThreshold)){

            Animal strongerParent = parent1.getEnergy() > parent2.getEnergy() ? parent1 : parent2;
            Animal weakerParent = strongerParent == parent1 ? parent2 : parent1;

            double energyRatio = (double) strongerParent.getEnergy() / (strongerParent.getEnergy() + weakerParent.getEnergy());
            Integer[] childGenome = crossGenomes(strongerParent.getGenome(), weakerParent.getGenome(), energyRatio);

            strongerParent.loseEnergy(energyForChild);
            weakerParent.loseEnergy(energyForChild);
            strongerParent.incrementChildrenNumber();
            weakerParent.incrementChildrenNumber();

            mutateGenes(childGenome, minMutations, maxMutations);
            Animal child = new Animal(strongerParent.position(),energyForChild * 2, childGenome.length, energyLossPerDay, childGenome);
            map.place(child, animals);
        }
    }

    public static Integer[] crossGenomes(Integer[] genome1, Integer[] genome2, double energyRatio) {
        //genome1 - powinien byc silniejszy
        Integer[] childGenome = new Integer[genome1.length];

        // Wybieramy losowo strone silniejszego genotypu
        boolean chooseLeft = Math.random() < 0.5;

        for(int i=0; i<genome1.length; i++) {
            if (chooseLeft){
                childGenome[i] = (i < Math.round(energyRatio * genome1.length)) ? genome1[i] : genome2[i];
            }
            else{
                childGenome[i] = (i >= Math.round((1 - energyRatio) * genome1.length)) ? genome1[i] : genome2[i];
            }
        }
        return childGenome;
    }
    private static void mutate(Integer[] genome, int index){
        genome[index] = random.nextInt(MapDirection.values().length); // gen to liczba z zakresu 0-7 (gdyż każdy gen odpowiada jednemu kierunkowi których jest 8)
    }
    private static void mutateGenes(Integer[] genome, int minMutations, int maxMutations){
        int numberOfMutations = random.nextInt(maxMutations - minMutations + 1) + minMutations;
        numberOfMutations = Math.min(numberOfMutations, genome.length);
        // losujemy geny ktore zmutują (losowanie bez powtórzeń)
        // zakładamy, że jeden gen może zostać zmutowany maksymalnie 1 raz
        ArrayList<Integer> indicesOfGenes = new ArrayList<>();
        for(int i=0; i<genome.length; i++){
            indicesOfGenes.add(i);
        }
        for(int i=0; i<numberOfMutations; i++){
            int index = random.nextInt(indicesOfGenes.size());
            mutate(genome, indicesOfGenes.remove(index));
        }
    }
}
