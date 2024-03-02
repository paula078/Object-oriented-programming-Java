package agh.ics.oop.presenter;

import agh.ics.oop.model.Configuration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class InitialWindow {
    private int openSimulationWindows = 0;

    @FXML
    private TextField mapHeight;
    @FXML
    private TextField mapWidth;
    @FXML
    private TextField initialGrass;
    @FXML
    private TextField energyGained;
    @FXML
    private TextField grassPerDay;
    @FXML
    private ComboBox<String> plantVariant;
    @FXML
    private TextField initialAnimals;
    @FXML
    private TextField energyOfAnimals;
    @FXML
    private TextField energyRequired;
    @FXML
    private TextField energyConsumed;
    @FXML
    private TextField energyLossPerDay;
    @FXML
    private TextField minMutations;
    @FXML
    private TextField maxMutations;
    @FXML
    private TextField genomeLength;
    @FXML
    private ComboBox<String> mutationVariant;
    @FXML
    private CheckBox saveLogs;


    public void simulationStart() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulationWindow.fxml"));
        BorderPane root = loader.load();

        SimulationWindow simulationController = loader.getController();

        Configuration configuration = new Configuration(mapHeight, mapWidth, initialGrass, grassPerDay, energyGained,
                plantVariant, initialAnimals, energyOfAnimals, energyRequired,
                energyConsumed, energyLossPerDay, minMutations, maxMutations, genomeLength, mutationVariant, saveLogs);

        simulationController.setParameters(configuration);
        openSimulationWindows++;

        Scene scene = new Scene(root);
        Stage secondStage = new Stage();
        secondStage.setTitle("Simulation");
        secondStage.setScene(scene);
        Image icon = new Image(String.valueOf(getClass().getClassLoader().getResource("Icon.png")));
        secondStage.getIcons().add(icon);

        secondStage.setOnCloseRequest(event -> { // Obsluga zdarzenia zamkniecia okna
            openSimulationWindows--;
            checkIfAllWindowsAreClosed();
        });

        secondStage.show();
    }
    private void checkIfAllWindowsAreClosed() {
        if (openSimulationWindows == 0) {
            System.exit(0); // Zamknij aplikacje, gdy wszystkie okna zostana zamkniete
        }
    }
}
