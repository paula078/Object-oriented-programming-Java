package agh.ics.oop.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class SaveLogs implements MapChangeListener{
    private int dayCounter = 0;
    private final boolean saveLogs;
    private File file;

    public SaveLogs(boolean saveLogs){
        this.saveLogs = saveLogs;
        if(saveLogs){
            String fileName = getFileName();
            file = new File(fileName);
            appendHeader();
        }
    }

    public void mapChanged(WorldMap worldMap){
        if (saveLogs) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                StringBuilder sb = new StringBuilder();
                sb.append((dayCounter++)).append(",");
                sb.append(worldMap.getNumberOfAnimals()).append(",");
                sb.append(worldMap.getGrasses().size()).append(",");
                sb.append(worldMap.getAverageEnergy()).append(",");
                sb.append(worldMap.getAverageLengthOfLife()).append(",");
                sb.append(worldMap.getAverageNumberOfChildren());
                writer.write(sb.toString());
                writer.newLine();
            } catch (IOException e) {
                System.out.println("File read error");
            }
        }
    }

    private void appendHeader(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            StringBuilder sb = new StringBuilder();
            sb.append("Day").append(",");
            sb.append("Number of animals").append(",");
            sb.append("Number of grasses").append(",");
            sb.append("Avarage energy of animals").append(",");
            sb.append("Avarage length of life").append(",");
            sb.append("Avarage number of children of living animals");
            writer.write(sb.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("File read error");
        }
    }
    private String getFileName (){
        String baseFileName = "simulation_logs";
        String fileExtension = ".csv";
        int index = 0;

        File file = new File(baseFileName + fileExtension);
        while(file.exists()){
            index++;
            file = new File(baseFileName + "_" + index + fileExtension);
        }
        return baseFileName + (index > 0 ? "_" + index : "") + fileExtension;
    }

}
