package agh.ics.oop.model;

import javafx.css.converter.DeriveColorConverter;

import java.util.*;

public class SquarePositionsGenerator implements PlantsGenerator{

    private final List<Vector2d> poisonousSquarePositions;
    private final List<Vector2d> otherPositions;
    private final Boundary squareVertices;
    private double percentageArea; //przydatne do okreslenia czy dana roslina urosnie na trujacej powierzchni
    private final int grassEnergy;
    private final WorldMap map;

    static Random random = new Random();

    public SquarePositionsGenerator(int grassEnergy, WorldMap map){
        poisonousSquarePositions = new ArrayList<>();
        otherPositions = new ArrayList<>();
        this.grassEnergy = grassEnergy;
        this.squareVertices = setSquareCoordinates(map.getHeight(), map.getWidth());
        this.map = map;
    }

    public void growGrass(int grassCount){
        generateSquarePositions(map.getHeight(), map.getWidth());
        removeOccupiedPositions(map.getGrasses());
        shufflePositions();
        Vector2d position;
        int number;
        for(int i=0; i<grassCount; i++){
            number = random.nextInt(100);
            if(number < percentageArea * 100 && !poisonousSquarePositions.isEmpty()){
                position = poisonousSquarePositions.remove(poisonousSquarePositions.size()-1);
                if(number < percentageArea * 25){ // 25% szans na zatruta rosline w obszarze
                    map.addGrass(position, -grassEnergy);
                }
                else{
                    map.addGrass(position, grassEnergy);
                }
            }

            else if(!otherPositions.isEmpty()){
                position = otherPositions.remove(otherPositions.size()-1);
                map.addGrass(position, grassEnergy);
            }
        }
    }

    private void generateSquarePositions(int height, int width){
        Vector2d vector;
        for(int x=0; x<width; x++){
            for(int y=0; y<height; y++){
                vector = new Vector2d(x,y);
                if(vector.follows(squareVertices.lowerLeft()) && vector.precedes(squareVertices.upperRight())){
                    poisonousSquarePositions.add(new Vector2d(x,y));
                }
                else{
                    otherPositions.add(new Vector2d(x,y));
                }
            }
        }
    }

    // losujemy w jakim miejscu bedzie kwadrat z zatrutymi owocami
    private Boundary setSquareCoordinates(int height, int width){
        int squareSideLength = getSquareSize(height,width);
        percentageArea = (double)squareSideLength*squareSideLength/(height*width);
        // koordynaty lewego dolnego wierzcholka:
        int xCoord = random.nextInt(width-squareSideLength+1);
        int yCoord = random.nextInt(height-squareSideLength+1);
        Vector2d lowerLeftSquare = new Vector2d(xCoord, yCoord);
        Vector2d upperRightSquare = new Vector2d(xCoord+squareSideLength-1, yCoord+squareSideLength-1);
        return new Boundary(lowerLeftSquare, upperRightSquare);
    }
    public boolean belongsTo(Vector2d vector){
        return vector.follows(squareVertices.lowerLeft()) && vector.precedes(squareVertices.upperRight());
    }

    // ta funkcja wylicza jakiej dlugosci bok ma kwadrat, ktory jest najbardziej zblizony do 20% poweirzchni mapy
    private int getSquareSize(int height, int width){
        int squareArea = (int) (height*width*0.2);
        double closestValue = Integer.MAX_VALUE;
        int squareSideLength = 0;
        for(int i=1; i<Integer.MAX_VALUE; i++){
            double actualValue = Math.abs(Math.pow(i,2) - squareArea);
            if(actualValue > closestValue){
                return squareSideLength;
            }
            closestValue = actualValue;
            squareSideLength = i;
        }
        return squareSideLength;
    }

    private void removeOccupiedPositions(Map<Vector2d, Grass> occupied){
        for(Vector2d occupiedVector: occupied.keySet()){
            if(occupiedVector.follows(squareVertices.lowerLeft()) && occupiedVector.precedes(squareVertices.upperRight())){
                poisonousSquarePositions.remove(occupiedVector);
            }
            else{
                otherPositions.remove(occupiedVector);
            }
        }
    }
    private void shufflePositions(){
        Collections.shuffle(poisonousSquarePositions);
        Collections.shuffle(otherPositions);
    }
}
