package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.model.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import java.io.*;
import java.util.*;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class SimulationWindow implements MapChangeListener {
    private static final Color pastelGreen = Color.rgb(144, 238, 144);
    private static int CELL_SIZE;
    @FXML
    private GridPane mapGrid;
    @FXML
    private Label numberOfDays;
    @FXML
    private Label numberOfAnimals;
    @FXML
    private Label numberOfGrasses;
    @FXML
    private Label numberOfEmptyCells;
    @FXML
    private Label dominantGenotype;
    @FXML
    private Label averageEnergy;
    @FXML
    private Label averageLengthOfLife;
    @FXML
    private Label averageNumberOfChildren;
    @FXML
    private BorderPane borderPane;
    @FXML
    private LineChart<Number, Number> lineChart;

    private WorldMap map;

    // Simulation parameters
    // Map properties
    private int mapHeight;
    private int mapWidth;
    private int initialGrassNumber;
    private int eachDayGrassNumber;
    private int energyOfEatenGrass;
    private boolean poisonedFruits;
    // Animals properties
    private int initialAnimalsNumber;
    private int energyOfAnimals;
    private int energyRequired;
    private int energyConsumed;
    private int energyLossPerDay;
    // Mutations properties
    private int minMutations;
    private int maxMutations;
    private int genomeLength;
    private boolean madness;
    private int emptyCells;
    private Simulation simulation;
    private boolean saveLogs;


    public void setParameters(Configuration configuration){
        this.mapHeight = configuration.mapHeight();
        this.mapWidth = configuration.mapWidth();
        this.initialGrassNumber = configuration.initialGrassNumber();
        this.eachDayGrassNumber = configuration.eachDayGrassNumber();
        this.energyOfEatenGrass = configuration.energyOfEatenGrass();
        this.poisonedFruits = configuration.poisonedFruits();
        this.initialAnimalsNumber = configuration.initialAnimalsNumber();
        this.energyOfAnimals = configuration.energyOfAnimals();
        this.energyRequired = configuration.energyRequired();
        this.energyConsumed = configuration.energyConsumed();
        this.energyLossPerDay = configuration.energyLossPerDay();
        this.minMutations = configuration.minMutations();
        this.maxMutations = configuration.maxMutations();
        this.genomeLength = configuration.genomeLength();
        this.madness = configuration.madness();
        this.saveLogs = configuration.saveLogs();
        setCellSize();
    }

    private void setCellSize(){
        int windowHeight = (int)borderPane.getPrefHeight();
        int windowWidth = (int)borderPane.getPrefWidth();
        int heightRatio = (windowHeight / (mapHeight+1)-6);
        int widthRatio = (windowWidth / (mapWidth+1));
        CELL_SIZE = Math.min(widthRatio, heightRatio);
    }

    public void setWorldMap(WorldMap map){
        this.map = map;
    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            try {
                onSimulationStartClicked();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                System.out.println("File read error");
            }
        });
    }
    public void drawMap(){

        clearGrid();
        emptyCells = 0;
        setupGridConstraints();

        for (int y = 0; y < mapHeight; y++){
            for(int x = 0; x < mapWidth; x++){
                Vector2d currPosition = new Vector2d(x, y);

                Rectangle mapCell = new Rectangle(CELL_SIZE, CELL_SIZE);
                mapCell.setStroke(Color.BLACK);

                boolean isColored = map.getPlantsGenerator().belongsTo(currPosition);
                if (isColored) {
                    mapCell.setFill(pastelGreen);
                    mapGrid.add(mapCell, x, y);
                }
                if(map.animalAt(currPosition) != null) {
                    ArrayList<Animal> animalsAtCurrPosition = map.getAnimalsAtPosition(currPosition);

                    int lowestEnergy = Integer.MAX_VALUE;

                    // jesli na danym polu jest kilka zwierzakow, rysujemy tego z najnizsza energia
                    for(Animal animal: animalsAtCurrPosition) {
                        if(animal.getEnergy() < lowestEnergy) {
                            lowestEnergy = animal.getEnergy();
                        }
                    }

                    drawAnimals(x, y, lowestEnergy);
                }
                else if (map.grassAt(currPosition) != null){
                    drawGrasses(currPosition, x, y);
                }
                else{
                    emptyCells++;
                }
            }
        }
        updateStatistics();
        updateChartData();
    }

    private void drawGrasses(Vector2d currPosition, int x, int y) {
        Image image;
        if(map.grassAt(currPosition).energy() > 0){
            image = new Image(String.valueOf(getClass().getClassLoader().getResource("GreenBush.png")));
        }
        else{
            image = new Image(String.valueOf(getClass().getClassLoader().getResource("RedBush.png")));
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(CELL_SIZE);
        imageView.setFitHeight(CELL_SIZE);
        mapGrid.add(imageView, x, y);
    }

    private void drawAnimals(int x, int y, int animalEnergy) {
        int animalsStartEnergy = simulation.getStartEnergy();

        Image animalImage;
        if (animalEnergy <= animalsStartEnergy * 0.25) {
            animalImage = new Image(String.valueOf(getClass().getClassLoader().getResource("Weak_animal.jpg")));
        } else if (animalEnergy <= animalsStartEnergy * 0.50) {
            animalImage = new Image(String.valueOf(getClass().getClassLoader().getResource("Exhausted_animal.jpg")));
        } else if (animalEnergy <= animalsStartEnergy * 0.75) {
            animalImage = new Image(String.valueOf(getClass().getClassLoader().getResource("Tired_animal.jpg")));
        } else {
            animalImage = new Image(String.valueOf(getClass().getClassLoader().getResource("Energized_animal.jpg")));
        }
        ImageView imageView = new ImageView(animalImage);
        imageView.setFitWidth(CELL_SIZE);
        imageView.setFitHeight(CELL_SIZE);

        // Zachowanie proporcji obrazu
        imageView.setPreserveRatio(true);

        mapGrid.add(imageView, x, y);
    }

    private void setupGridConstraints() {
        for(int i=0; i<mapWidth; i++){
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_SIZE));
        }
        for(int i=0; i<mapHeight; i++){
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_SIZE));
        }
    }

    private void updateStatistics(){
        numberOfDays.setText(Integer.toString(simulation.getNumberOfDays()));
        numberOfAnimals.setText(Integer.toString(map.getNumberOfAnimals()));
        numberOfGrasses.setText(Integer.toString(map.getGrasses().size()));
        numberOfEmptyCells.setText(Integer.toString(emptyCells));
        averageEnergy.setText(Double.toString(map.getAverageEnergy()));
        dominantGenotype.setText(Arrays.toString(map.getMostPopularGenome()));
        averageNumberOfChildren.setText(Double.toString(map.getAverageNumberOfChildren()));
        if(map.getAverageLengthOfLife() != 0){
            averageLengthOfLife.setText(Double.toString(map.getAverageLengthOfLife()));
        }
    }
    private void updateChartData() {
        int days = simulation.getNumberOfDays();
        int numberOfAnimals = map.getNumberOfAnimals();
        int numberOfGrasses = map.getGrasses().size();

        // Lista przechowujaca serie danych dla wykresu
        ObservableList<XYChart.Series<Number, Number>> data = lineChart.getData();

        // Sprawdzenie czy istnieje już pierwsza seria danych
        if (data.isEmpty()) {
            addNewSeries();
        } else {
            // Jakies dane juz sa na wykresie zatem dodawane sa nowe dane do juz istniejacych
            XYChart.Series<Number, Number> animalSeries = data.get(0);
            XYChart.Series<Number, Number> grassSeries = data.get(1);

            animalSeries.getData().add(new XYChart.Data<>(days, numberOfAnimals));
            grassSeries.getData().add(new XYChart.Data<>(days, numberOfGrasses));
            animalSeries.setName("Number of Animals");
            grassSeries.setName("Number of Grasses");
        }
    }

    private void addNewSeries() {
        XYChart.Series<Number, Number> animalCountSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> grassCountSeries = new XYChart.Series<>();

        lineChart.getData().add(animalCountSeries);
        lineChart.getData().add(grassCountSeries);
    }


    private void clearGrid() {
        mapGrid.setGridLinesVisible(false);
        mapGrid.getChildren().clear();
        mapGrid.setGridLinesVisible(true);
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }
    @Override
    public void mapChanged(WorldMap worldMap) {
        this.setWorldMap(map);
        Platform.runLater(this::drawMap);
    }

    public void onSimulationStartClicked() throws InterruptedException, IOException {
        WorldMap initMap = new WorldMap(mapWidth, mapHeight, initialGrassNumber,eachDayGrassNumber, energyOfEatenGrass,
                energyRequired, energyConsumed, energyLossPerDay, minMutations, maxMutations, madness, poisonedFruits);

        this.setWorldMap(initMap);
        map.registerObserver(this);
        SaveLogs mapDisplay = new SaveLogs(saveLogs);
        map.registerObserver(mapDisplay);

        simulation = new Simulation(map, initialAnimalsNumber , energyOfAnimals, genomeLength, energyLossPerDay);
        SimulationApp.getSimulationApp().addSimulation(simulation);
    }

    public void pauseSimulation() {
        simulation.setStopFlag(true);
    }

    public void resumeSimulation() {
        simulation.setStopFlag(false);
    }
}

