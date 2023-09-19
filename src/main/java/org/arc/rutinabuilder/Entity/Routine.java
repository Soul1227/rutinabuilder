package org.arc.rutinabuilder.Entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document
public class Routine {
    private ArrayList<Exercice> Exercices;

    public Routine() {
    }

    public Routine(ArrayList<Exercice> exercices) {
        Exercices = exercices;
    }

    public ArrayList<Exercice> getExercices() {
        return Exercices;
    }

    public void setExercices(ArrayList<Exercice> exercices) {
        Exercices = exercices;
    }
}
