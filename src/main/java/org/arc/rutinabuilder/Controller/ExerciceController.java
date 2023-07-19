package org.arc.rutinabuilder.Controller;

import org.arc.rutinabuilder.Entity.Exercice;
import org.arc.rutinabuilder.Services.ExerciceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class ExerciceController {

    @Autowired
    ExerciceService exerciceService;

    /**
     * Saves an Exercice object received in the request body.
     *
     * @param exercice The Exercice object to be saved.
     * @return The saved Exercice object.
     */
    @PostMapping("/exercice")
    public Exercice saveExercice(@RequestBody Exercice exercice) {
        return exerciceService.saveExercice(exercice, exercice.getCollection());
    }
/**
 @GetMapping("/exercice/{id}") public Exercice findById(@PathVariable("id") String id) {

 }
 */
}
