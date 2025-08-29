package model;

import model.entities.Score;

import javax.swing.*;
import java.io.*;

public class ScoreModel {
    private DefaultListModel<Score> defaultListModel;
    private String fileName = "savedScores.dat";
    public ScoreModel() {
        defaultListModel = loadScoresFromFile();


    }
    public void saveScore(Score score) {
        defaultListModel.addElement(score);
        saveAllScoresToFile();
    }

    public DefaultListModel<Score> loadScoresFromFile() {
        File file = new File(fileName);
        if (!file.exists() || file.length() == 0) {
            return new DefaultListModel<>();
        }
        if(defaultListModel!= null && !defaultListModel.isEmpty()) {
            defaultListModel.clear();
        }
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
            return (DefaultListModel<Score>) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new DefaultListModel<>();
        }




    }


    public void saveAllScoresToFile() {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName))) {
            output.writeObject(defaultListModel);
        } catch (IOException e) {

        }
    }


    public boolean isDuplicateRecord (Score score ) {
        return defaultListModel.contains(score);
    }



    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public DefaultListModel<Score> getDefaultListModel() {
        return defaultListModel;
    }

    public void setDefaultListModel(DefaultListModel<Score> defaultListModel) {
        this.defaultListModel = defaultListModel;
    }
}
