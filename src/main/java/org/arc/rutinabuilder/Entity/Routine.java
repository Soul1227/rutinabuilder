package org.arc.rutinabuilder.Entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document
public class Routine {
    private ArrayList<Exercise> Exercices;

    public Routine() {
    }

    public Routine(ArrayList<Exercise> exercices) {
        Exercices = exercices;
    }

    public ArrayList<Exercise> getExercices() {
        return Exercices;
    }

    public void setExercices(ArrayList<Exercise> exercices) {
        Exercices = exercices;
    }
}
