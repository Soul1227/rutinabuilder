package org.arc.rutinabuilder.Services;

import org.arc.rutinabuilder.Entity.Counter;
import org.arc.rutinabuilder.Entity.Exercice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ExerciceService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Saves an Exercice object into the specified collection.
     *
     * @param exercice      The Exercice object to be saved.
     * @param colectionName The name of the collection where to save it.
     * @return The saved Exercice object.
     */
    public Exercice saveExercice(Exercice exercice, String colectionName) {
        if (exercice.getId() == null) {
            exercice.setId(getNextIdForNewObject());
        }
        return mongoTemplate.save(exercice, colectionName);
    }

    /**
     * Gets the next ID for a new Exercice object.
     *
     * @return A new ID.
     */
    public long getNextIdForNewObject() {
        String collectionName = "counter";
        Query query = new Query();
        Counter counter = mongoTemplate.findOne(query, Counter.class, collectionName);

        if (counter == null) {
            counter = new Counter();
            counter.setNextId(1); // // Initial value for nextId
            mongoTemplate.save(counter, collectionName);
        } else {
            Update update = new Update().inc("nextId", 1);
            mongoTemplate.upsert(query, update, collectionName);
        }
        return counter.getNextId();
    }
}
