package org.arc.rutinabuilder.Controller;

import org.arc.rutinabuilder.Entity.Exercice;
import org.arc.rutinabuilder.Services.ExerciceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<Exercice> saveExercice(@RequestBody Exercice exercice) {
        if (exerciceService.saveExercice(exercice)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(exercice);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Updates an Exercice Object received in the request body.
     *
     * @param exercice The Exercice object to be updated.
     * @return The Exercice object updated.
     */
    @PutMapping("/exercice")
    public ResponseEntity<Exercice> updateExercice(@RequestBody Exercice exercice) {
        if (exerciceService.updateExercice(exercice)) {
            return ResponseEntity.status(HttpStatus.OK).body(exercice);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/exercice")
    public ResponseEntity<Exercice> deleteExercice (@RequestBody Exercice exercice){
        
    }

}
