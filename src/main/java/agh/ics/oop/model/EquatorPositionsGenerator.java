package agh.ics.oop.model;

import java.util.*;

public class EquatorPositionsGenerator implements PlantsGenerator{

    private final List<Vector2d> equatorPositions;
    private final List<Vector2d> otherPositions;
    private int lowerEquatorIndex;
    private int upperEquatorIndex;
    private final int grassEnergy;
    private final Boundary equatorCoordinates;
    private final WorldMap map;

    static Random random = new Random();

    public EquatorPositionsGenerator(int grassEnergy, WorldMap map){
        equatorPositions = new ArrayList<>();
        otherPositions = new ArrayList<>();
        this.grassEnergy = grassEnergy;
        this.equatorCoordinates = setEquatorCoordinates(map.getHeight());
        this.map = map;
    }

    public void growGrass(int grassCount){
        generateEquatorPositions(map.getHeight(), map.getWidth());
        removeOccupiedPositions(map.getGrasses());
        shufflePositions();
        Vector2d position;
        int number;
        for(int i=0; i<grassCount; i++){
            number = random.nextInt(100);
            if(number < 80 && !equatorPositions.isEmpty()){
                position = equatorPositions.remove(equatorPositions.size()-1); //bierzemy ostatni element bo jest to operacja O(1)
                map.addGrass(position, grassEnergy);
            }
            else if(!otherPositions.isEmpty()){ //gdy nie ma wolnego miejsca na mapie nie losujemy juz zadnej pozycji
                position = otherPositions.remove(otherPositions.size()-1);
                map.addGrass(position, grassEnergy);
            }
        }
    }

    private void generateEquatorPositions(int height, int width){
        lowerEquatorIndex = equatorCoordinates.lowerLeft().getY();
        upperEquatorIndex = equatorCoordinates.upperRight().getY();

        for(int x=0; x<width; x++){
            for(int y=0; y<height; y++){
                if(y >= lowerEquatorIndex && y <= upperEquatorIndex){
                    equatorPositions.add(new Vector2d(x,y));
                }
                else{
                    otherPositions.add(new Vector2d(x,y));
                }
            }
        }
    }
    //otrzymujac height = 15 indeksy sa 0-14
    private Boundary setEquatorCoordinates(int height){
        int equatorHeight = (int)Math.round(height * 0.2);
        int lowerId = Math.round(((float)height / 2) - ((float)equatorHeight / 2));
        Vector2d lowerLeft = new Vector2d(0,lowerId);
        Vector2d upperLeft = new Vector2d(0, lowerId+equatorHeight-1);
        return new Boundary(lowerLeft, upperLeft);
    }
    public boolean belongsTo(Vector2d vector){ //sprawdza czy podany wektor znajduje sie na rowniku
        return vector.getY() >= equatorCoordinates.lowerLeft().getY() && vector.getY() <= equatorCoordinates.upperRight().getY();
    }
    private void removeOccupiedPositions(Map<Vector2d, Grass> occupied){
        for(Vector2d occupiedVector: occupied.keySet()){
            if(occupiedVector.getY() >= lowerEquatorIndex && occupiedVector.getY() <= upperEquatorIndex){
                equatorPositions.remove(occupiedVector);
            }
            else{
                otherPositions.remove(occupiedVector);
            }
        }
    }
    private void shufflePositions(){
        Collections.shuffle(equatorPositions);
        Collections.shuffle(otherPositions);
    }
}