package org.arc.rutinabuilder.Controller;

import org.arc.rutinabuilder.Entity.Exercise;
import org.arc.rutinabuilder.Services.ExerciceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
     * @param exercice The Exercice object to be saved.
     * @return The saved Exercice object.
     */
    @PostMapping("/exercice")
    public ResponseEntity<Exercise> saveExercice(@RequestBody Exercise exercice) {
        if (exerciceService.saveExercice(exercice)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(exercice);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /**
    @PostMapping("/exercice")
    public ResponseEntity saveExercice(@RequestBody Exercise exercice) {
        try {
            if(exercice == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); //No es un ejercicio

            exercice.setId(getNextIdForNewObject());

            if (exercice.getDone()) {
                exercice.setCollection(exercice.getCollection() + "_done");
            } else {
                exercice.setCollection(exercice.getCollection() + "_guide");
            }

            Exercise newExercise = mongoTemplate.save(exercice, exercice.getCollection());
            if (newExercise == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); //Error al guardar el ejercicio

            return ResponseEntity.status(HttpStatus.OK).body(exercice);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()); // e.message (mensaje de la excepcion)
        }
    }
*/
    /**
     * Updates an Exercice Object received in the request body.
     *
     * @param exercice The Exercice object to be updated.
     * @return The Exercice object updated.
     */
    @PutMapping("/exercice")
    public ResponseEntity<Exercise> updateExercice(@RequestBody Exercise exercice) {
        if (exerciceService.updateExercice(exercice)) {
            return ResponseEntity.status(HttpStatus.OK).body(exercice);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes an Exercice and all the Exercices done with the same name.
     *
     * @param exercice The Exercice object to be deleted.
     * @return
     */
    @DeleteMapping("/exercice")
    public ResponseEntity<Exercise> deleteExercice(@RequestBody Exercise exercice) {
        if (exerciceService.deleteExercice(exercice.getId(), exercice.getCollection()) && exerciceService.deleteAllExerciceDone(exercice.getName(), exercice.getCollection())) {
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
     * Retrieves all Exercices performed by a specific name from a given collection.
     *
     * @param exerciceName   The name of the Exercice to filter by.
     * @param collectionName The name of the collection to search in.
     * @return A list of Exercices performed.
     */
    @GetMapping("/allExercicesDone")
    public ResponseEntity<List<Exercise>> getAllExercicesDoneOfOneType(@RequestParam String collectionName, String exerciceName) {
        try {
            if (collectionName.isEmpty() || exerciceName.isEmpty())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            return ResponseEntity.status(HttpStatus.OK).body(exerciceService.getAllExercicesDoneOfOneType(exerciceName, collectionName));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
/**
n @GetMapping("/routine") public ResponseEntity<List<Documet>> createRoutine(@RequestBody int numberOfWeeks, int numberOfDaysPerWeek, int numberOfExercisesPerDay){


 }
 */
}
