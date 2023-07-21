package org.arc.rutinabuilder.Services;

import org.arc.rutinabuilder.Entity.Counter;
import org.arc.rutinabuilder.Entity.Exercice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Objects;

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

    /**
     * Update the object passed
     *
     * @param exercice the object to update
     * @return the object updated
     */
    public Exercice updateExercice(Exercice exercice) {
        Exercice exerciceDB = findOneById(exercice.getId(), exercice.getCollection());
        try {
            exercicePooring(exerciceDB, exercice);
        }catch (IllegalAccessException ex){
            System.out.println(ex.getMessage());
        }
        return mongoTemplate.save(exerciceDB,exerciceDB.getCollection());
    }

    /**
     * Find an Exercice from the DB based on its id and collection.
     *
     * @param id             of the exercice object to find
     * @param collectionName where the object is
     * @return an exercice Object
     */
    public Exercice findOneById(Long id, String collectionName) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, Exercice.class, collectionName);
    }

    /**
     *
     * @param exerciceDB
     * @param exerciceUpdated
     * @return
     * @throws IllegalAccessException
     */
    public Exercice exercicePooring(Exercice exerciceDB, Exercice exerciceUpdated) throws IllegalAccessException {
        Class<?> exerciceClass = Exercice.class;
        java.lang.reflect.Field[] fields = exerciceClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object valueDB = field.get(exerciceDB);
            Object valueUpdated = field.get(exerciceUpdated);

            if (!Objects.equals(valueDB, valueUpdated)) {
                field.set(exerciceDB, valueUpdated);
            }
        }
        return exerciceDB;
    }
}
