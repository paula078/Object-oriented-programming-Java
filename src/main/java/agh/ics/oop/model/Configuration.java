package agh.ics.oop.model;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.Objects;

public record Configuration(int mapHeight,
                            int mapWidth,
                            int initialGrassNumber,
                            int eachDayGrassNumber,
                            int energyOfEatenGrass,
                            boolean poisonedFruits,
                            int initialAnimalsNumber,
                            int energyOfAnimals,
                            int energyRequired,
                            int energyConsumed,
                            int energyLossPerDay,
                            int minMutations,
                            int maxMutations,
                            int genomeLength,
                            boolean madness,
                            boolean saveLogs) {

    public Configuration(TextField mapHeightField, TextField mapWidthField, TextField initialGrassNumberField,
                         TextField eachDayGrassNumberField, TextField energyOfEatenGrassField, ComboBox<String> plantVariant,
                         TextField initialAnimalsNumberField, TextField energyOfAnimalsField, TextField energyRequiredField,
                         TextField energyConsumedField, TextField energyLossPerDayField, TextField minMutationsField,
                         TextField maxMutationsField, TextField genomeLengthField, ComboBox<String> mutationVariant,
                         CheckBox saveLogsCheckbox) {
        this(Integer.parseInt(mapHeightField.getText()),
                Integer.parseInt(mapWidthField.getText()),
                Integer.parseInt(initialGrassNumberField.getText()),
                Integer.parseInt(eachDayGrassNumberField.getText()),
                Integer.parseInt(energyOfEatenGrassField.getText()),
                Objects.equals(plantVariant.getValue(), "Poisoned fruits"),
                Integer.parseInt(initialAnimalsNumberField.getText()),
                Integer.parseInt(energyOfAnimalsField.getText()),
                Integer.parseInt(energyRequiredField.getText()),
                Integer.parseInt(energyConsumedField.getText()),
                Integer.parseInt(energyLossPerDayField.getText()),
                Integer.parseInt(minMutationsField.getText()),
                Integer.parseInt(maxMutationsField.getText()),
                Integer.parseInt(genomeLengthField.getText()),
                Objects.equals(mutationVariant.getValue(), "A bit of madness"),
                saveLogsCheckbox.isSelected());
    }
}
