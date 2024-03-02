package agh.ics.oop.model;

import agh.ics.oop.Simulation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Objects;

public class SimulationApp extends Application {
    private static SimulationApp simulationApp;
    private SimulationEngine simulationEngine;

    @Override
    public void init() {
        simulationApp = this;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("initialWindow.fxml"));
        BorderPane viewRoot = loader.load();
        configureStage(primaryStage, viewRoot);
        primaryStage.show();
        simulationEngine = new SimulationEngine();
    }
    private void configureStage(Stage primaryStage, BorderPane viewRoot){
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        Image icon = new Image(String.valueOf(getClass().getClassLoader().getResource("Icon.png")));
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Darwin World");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
    public static SimulationApp getSimulationApp(){
        return simulationApp;
    }
    public void addSimulation(Simulation simulation){
        simulationEngine.addSimulation(simulation);
    }
}
