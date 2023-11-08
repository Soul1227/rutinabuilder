package org.arc.rutinabuilder.Controller;

import org.arc.rutinabuilder.Entity.Exercise;
import org.arc.rutinabuilder.Services.ExerciceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ExerciceController {

    @Autowired
    ExerciceService exerciceService;

    public ExerciceController(ExerciceService exerciceService) {
        this.exerciceService = exerciceService;
    }

    /**
     * Saves an Exercice object received in the request body.
     *
     * @param exercise The Exercice object to be saved.
     * @return The saved Exercice object.
     */
    @PostMapping("/exercise")
    public ResponseEntity<Exercise> saveExercise(@RequestBody Exercise exercise) {
        try {
            if (exercise == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            if (exerciceService.saveExercice(exercise) != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(exercise);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Updates an Exercice Object received in the request body.
     *
     * @param exercice The Exercice object to be updated.
     * @return The Exercice object updated.
     */
    @PutMapping("/exercise")
    public ResponseEntity<Exercise> updateExercice(@RequestBody Exercise exercice) {
        Exercise exercise = exerciceService.updateExercice(exercice);
        if (exercise != null) {
            return ResponseEntity.status(HttpStatus.OK).body(exercise);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes an Exercise and all the Exercices done with the same name.
     *
     * @param exercise The Exercise object to be deleted.
     * @return
     */
    @DeleteMapping("/exercice")
    public ResponseEntity<Exercise> deleteExercise(@RequestBody Exercise exercise) {
        if (exerciceService.deleteExercice(exercise.getId(), exercise.getCollection()) && exerciceService.deleteAllExerciceDone(exercise.getName(), exercise.getCollection())) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //READ

    /**
     * Retrieves all the Exercices of one specific muscle.
     *
     * @param muscle of the type of Exercice to filter by.
     * @return A list of BSON documents representing the Exercices.
     */
    @GetMapping("/exercices")
    public ResponseEntity<List<Exercise>> getAllExerciceOfOneMuscle(@RequestParam String muscle) {
        try {
            if (muscle.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            return ResponseEntity.status(HttpStatus.OK).body(exerciceService.getAllExerciceOfOneType(muscle));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves the name of all collections in the Data Base.
     *
     * @return A list of BSON documents representing the Exercices.
     */
    @GetMapping("/collectionNames")
    public ResponseEntity<List<String>> getAllCollectionNames() {
        try {
            ArrayList<String> collectionNames = new ArrayList<>(exerciceService.getAllCollectionNames());
            return ResponseEntity.status(HttpStatus.OK).body(collectionNames);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves all Exercices performed by a specific name from a given collection.
     *
     * @param exerciseName   The name of the Exercice to filter by.
     * @param collectionName The name of the collection to search in.
     * @return A list of Exercices performed.
     */
    @GetMapping("/allExercicesDone")
    public ResponseEntity<List<Exercise>> getAllExercicesDoneOfOneType(@RequestParam String collectionName, String exerciseName) {
        try {
            if (collectionName.isEmpty() || exerciseName.isEmpty())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            return ResponseEntity.status(HttpStatus.OK).body(exerciceService.getAllExercicesDoneOfOneType(exerciseName, collectionName));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
