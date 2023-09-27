package org.arc.rutinabuilder.Services;

import org.arc.rutinabuilder.Entity.Exercise;
import org.arc.rutinabuilder.Entity.Routine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The RoutineService class facilitates the creation of workout routines by assigning exercises to specific muscle group collections.
 * It ensures that exercises with empty or missing collections are assigned based on a predefined list of collection names.
 * Additionally, it maintains a mapping of the least recently performed exercises for each muscle group guide collection.
 */
@Service
public class RoutineService {

    @Autowired
    ExerciceService exerciceService;

    private List<String> listCollectionNameBanned = new ArrayList<>();
    private List<String> listCollectionName = new ArrayList<>();
    private final HashMap<String, Exercise> mapNameCollection = new HashMap<String, Exercise>();

    public RoutineService() {
    }

    /**
     * Creates a workout routine by preparing the necessary data, assigning collections to exercises, and filling
     * exercises with updated information.
     *
     * @param routine The input routine to be processed.
     * @return A modified Routine object with exercises assigned to collections and updated exercise information.
     */
    public Routine CreateRoutine(Routine routine) {
        PreparingList(routine);
        AssignCollection(routine, listCollectionName);
        FillExercises(routine);
        return routine;
    }

    /**
     * Fills exercises in the routine with updated information based on assigned collections.
     *
     * @param routine The routine containing exercises with assigned collections.
     */
    private void FillExercises(Routine routine) {
        for (Exercise exercice : routine.getExercices()) {
            exercice = mapNameCollection.get(exercice.getCollection());
            exercice.setId(null);
            exercice.setDone(false);
        }
    }

    /**
     * Assigns collections to exercises in the routine, ensuring each exercise has a collection assigned.
     *
     * @param routine            The routine containing exercises to be assigned collections.
     * @param listCollectionName A list of collection names to use for assignment.
     */
    private void AssignCollection(Routine routine, List<String> listCollectionName) {
        int collectionIndex = 0;
        for (Exercise exercise : routine.getExercices()) {
            if (exercise.getCollection() == null || exercise.getCollection().isEmpty()) {
                exercise.setCollection(listCollectionName.get(collectionIndex));
                collectionIndex = (collectionIndex + 1) % listCollectionName.size();
            }
        }
    }

    /**
     * Prepares the required lists and mappings for routine creation, including banned collection names, available
     * collection names, and a mapping of exercises by collection name.
     *
     * @param routine The input routine for which data is being prepared.
     */
    private void PreparingList(Routine routine) {
        setListCollectionNameBanned(routine);
        setListCollectionName(exerciceService.getAllCollectionNames().stream().toList());
        listCollectionName.forEach(listCollectionNameBanned::remove);
        FillExerciseMap();
    }

    /**
     * Retrieves a HashMap of the least recent exercises performed for each muscle group guide collection.
     *
     * @return A HashMap of Exercice objects, where each object represents the last performed exercise for a muscle group guide collection.
     */
    public HashMap<String, Exercise> FillExerciseMap() {
        List<String> guideCollections = exerciceService.getAllCollectionNames().stream().filter(collectionName -> collectionName.contains("_guide")).toList();
        for (String collectionName : guideCollections) {
            Exercise exercice = exerciceService.findLeastPerformedExercise(collectionName);
            if (exercice != null) {
                mapNameCollection.put(collectionName, exercice);
            }
        }
        return mapNameCollection;
    }

    public List<String> getListCollectionNameBanned() {
        return listCollectionNameBanned;
    }

    public void setListCollectionNameBanned(Routine routine) {
        for (int index = 0; index < routine.getExercices().size(); index++) {
            if (!listCollectionNameBanned.contains(routine.getExercices().get(index).getCollection()) && !routine.getExercices().get(index).getCollection().isEmpty())
                listCollectionNameBanned.add(routine.getExercices().get(index).getCollection());
        }
    }

    public List<String> getListCollectionName() {
        return listCollectionName;
    }

    public void setListCollectionName(List<String> listCollectionName) {
        this.listCollectionName = listCollectionName;
    }
}
