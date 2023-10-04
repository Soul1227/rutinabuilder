package org.arc.rutinabuilder.Services;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.arc.rutinabuilder.Entity.Counter;
import org.arc.rutinabuilder.Entity.Exercise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ExerciceService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public ExerciceService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public ExerciceService() {
    }

    /**
     * Saves an Exercice object into the specified collection.
     *
     * @param exercise The Exercice object to be saved.
     * @return The saved Exercice object.
     */
    public Exercise saveExercice(Exercise exercise) {
        if (exercise.getId() == null) {
            exercise.setId(getNextIdForNewObject());
        }
        /*
          If the exercise come with the param "done" as true, "_done" is added to his collection name.
          so it will be saved in a different collection.
         */
        if (exercise.getDone()) {
            exercise.setCollection(exercise.getCollection() + "_done");
        } else {
            exercise.setCollection(exercise.getCollection() + "_guide");
        }
        try {
            return mongoTemplate.save(exercise, exercise.getCollection());
        } catch (Exception e) {
            //Añadir Log
            return null;
        }
    }

    /**
     * Gets the next ID for a new Exercice object.
     *
     * @return A new ID.
     */
    public long getNextIdForNewObject() {
        String collectionName = "counter";
        Counter counter = mongoTemplate.findOne(new Query(), Counter.class, collectionName);

        if (counter == null) {
            counter = new Counter();
            counter.setNextId(1); // Initial value for nextId
            mongoTemplate.save(counter, collectionName);
        } else {
            Update update = new Update().inc("nextId", 1);
            mongoTemplate.upsert(new Query(), update, collectionName);
        }
        return counter.getNextId();
    }

    /**
     * Update the object passed
     *
     * @param exercice the object to update
     * @return the object updated
     */
    public Exercise updateExercice(Exercise exercice) {
        try {
            Query query = new Query(Criteria.where("id").is(exercice.getId()));
            Update update = new Update()
                    .set("name", exercice.getName())
                    .set("rep", exercice.getRep())
                    .set("set", exercice.getSet())
                    .set("time", exercice.getTime())
                    .set("description", exercice.getDescription())
                    .set("date", exercice.getDate())
                    .set("done", exercice.getDone())
                    .set("weight", exercice.getWeight())
                    .set("collection", exercice.getCollection());
            UpdateResult result = mongoTemplate.updateFirst(query, update, exercice.getCollection());
            if (result.wasAcknowledged()) return findOneById(exercice.getId(), exercice.getCollection());
            return null;
        } catch (Exception e) {
            return null; // Error en la actualización
        }
    }

    /**
     * Find an Exercice from the DB, based on its id and collection.
     *
     * @param id             of the exercice object to find
     * @param collectionName where the object is
     * @return an exercice Object
     */
    public Exercise findOneById(Long id, String collectionName) {
        Query query = new Query(Criteria.where("id").is(id));
        try {
            return mongoTemplate.findOne(query, Exercise.class, collectionName);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    /**
     * Find the least performed Exercise form a collection.
     *
     * @param collectionName where to find the Exercise.
     * @return an Exercise.
     */
    public Exercise findLeastPerformedExercise(String collectionName) {
        Query query = new Query().with(Sort.by(Sort.Order.desc("date"))).limit(1);
        return mongoTemplate.findOne(query, Exercise.class, collectionName);
    }

    /**
     * Find an Exercice from the DB, based on its name and collection.
     *
     * @param name           of the Exercice to find.
     * @param collectionName where the exercice is.
     * @return an Exercice.
     */
    public Exercise findOneByName(String name, String collectionName) {
        Query query = new Query(Criteria.where("name").is(name));
        try {
            return mongoTemplate.findOne(query, Exercise.class, collectionName);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    /**
     * Removes an exercice from the database.
     *
     * @param id             of the Exercice to be deleted.
     * @param CollectionName of the Exercice to be deleted.
     * @return true if it was deleted, false if it was not.
     */
    public boolean deleteExercice(long id, String CollectionName) {
        Query query = new Query(Criteria.where("id").is(id));
        try {
            DeleteResult result = mongoTemplate.remove(query, Exercise.class, CollectionName);
            return result.wasAcknowledged();
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Deletes all the exercice done for the exercice name given.
     *
     * @param name           of the Exercice to be deleted.
     * @param CollectionName of the Exercice to be deleted.
     */
    public boolean deleteAllExerciceDone(String name, String CollectionName) {
        Query query = new Query(Criteria.where("name").is(name));
        try {
            DeleteResult result = mongoTemplate.remove(query, Exercise.class, CollectionName);
            return result.wasAcknowledged();
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * This method retrieves a Set of String objects representing the names of all the collections present in the MongoDB database.
     *
     * @return A Set of String containing the names of all the collections in the database.
     */
    public Set<String> getAllCollectionNames() {
        return mongoTemplate.getCollectionNames();
    }

    /**
     * Retrieves the Exercice from a specific collection.
     *
     * @param collectionName the name of the collection.
     * @return a list of Exercice.
     */
    public List<Exercise> getAllExerciceOfOneType(String collectionName) {
        return mongoTemplate.findAll(Exercise.class, collectionName);
    }

    /**
     * Retrieves all exercice done in one collection under a specific name.
     *
     * @param exerciceName   the name of the exercice.
     * @param collectionName the name of the collection where to look at.
     * @return a list of Exercice.
     */
    public List<Exercise> getAllExercicesDoneOfOneType(String exerciceName, String collectionName) {
        Query query = new Query(Criteria.where("name").is(exerciceName));
        query.with(Sort.by(Sort.Order.asc("date")));
        return mongoTemplate.find(query, Exercise.class, collectionName);
    }
}
