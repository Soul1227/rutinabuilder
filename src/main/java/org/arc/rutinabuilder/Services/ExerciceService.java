package org.arc.rutinabuilder.Services;

import org.arc.rutinabuilder.Entity.Counter;
import org.arc.rutinabuilder.Entity.Exercice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ExerciceService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Saves a new exercice object
     * @param exercice to saved
     * @param colectionName the collection where to save it
     * @return and exercice object.
     */
    public Exercice saveExercice(Exercice exercice, String colectionName){
        if(exercice.getId()==null){
            exercice.setId(getNextIdForNewObject());
        }
        return mongoTemplate.save(exercice,colectionName);
    }

    /**
     * Takes the id for the next object.
     * @return a new id
     */
    public long getNextIdForNewObject() {
        String collectionName = "counter";
        Query query = new Query();
        Counter counter = mongoTemplate.findOne(query, Counter.class, collectionName);

        if (counter == null) {
            counter = new Counter();
            counter.setNextId(1); // Valor inicial para nextId
            mongoTemplate.save(counter, collectionName);
        } else {
            Update update = new Update().inc("nextId", 1);
            mongoTemplate.upsert(query, update, collectionName);
        }
        return counter.getNextId();
    }
}
