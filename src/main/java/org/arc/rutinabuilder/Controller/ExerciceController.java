package org.arc.rutinabuilder.Controller;

import org.arc.rutinabuilder.Entity.Exercice;
import org.arc.rutinabuilder.Repository.ExerciceRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class ExerciceController {

    @Autowired
    private MongoTemplate mongoTemplate;
    private final ExerciceRepository exerciceRepository;
    @Autowired
    public ExerciceController(ExerciceRepository exerciceRepository) {
        this.exerciceRepository = exerciceRepository;
    }

    @PostMapping("/exercice")
    public Exercice saveExercice(@RequestBody Exercice exercice){
        return mongoTemplate.save(exercice,exercice.getCollection());
    }

    @GetMapping("/exercice/{id}")
    public Exercice findById(@PathVariable("id") String id) {
        ObjectId objectId = new ObjectId(id);
        Optional<Exercice> optionalExercice = exerciceRepository.findById(objectId);
        if (optionalExercice.isPresent()) {
            return optionalExercice.get();
        } else {
            // Manejar el caso en que no se encuentra el objeto Exercice con el ID especificado
            // Puede retornar un valor predeterminado o lanzar una excepción, según tus necesidades
            return null;
        }
    }
}
