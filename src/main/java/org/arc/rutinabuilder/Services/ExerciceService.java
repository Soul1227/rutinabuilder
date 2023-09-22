package org.arc.rutinabuilder.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.DeleteResult;
import org.arc.rutinabuilder.Entity.Counter;
import org.arc.rutinabuilder.Entity.Exercise;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
     * @param exercice The Exercice object to be saved.
     * @return The saved Exercice object.
     */
    public Boolean saveExercice(Exercise exercice) {
        if (exercice.getId() == null) {
            exercice.setId(getNextIdForNewObject());
        }
        /*
          If the exercice come with the param "done" as true, "_done" is added to his collection name.
          so it will be saved in a different collection.
         */
        if (exercice.getDone()) {
            exercice.setCollection(exercice.getCollection() + "_done");
        } else {
            exercice.setCollection(exercice.getCollection() + "_guide");
        }
        try {
            mongoTemplate.save(exercice, exercice.getCollection());
            return true;
        } catch (Exception e) {
            //Añadir Log
            return false;
        }
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
    public boolean updateExercice(Exercise exercice) {
        try {
            saveExercice(exercice);
            return true; // Actualización exitosa
        } catch (Exception e) {
            return false; // Error en la actualización
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
     * @param collectionName where to find the Exercise.
     * @return an Exercise.
     */
    public Exercise findLeastPerformedExercise(String collectionName){
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

    /**
     * Converts a list of BSON documents to JSON objects.
     *
     * @param bsonDocuments a list of BSON documents
     * @return a list of documents in JSON format
     */
    public List<String> convertToJSON(List<Document> bsonDocuments) {
        List<String> jsonDocuments = new ArrayList<>();
        if (bsonDocuments == null) {
            return jsonDocuments;
        }
        // Convierte cada documento BSON a JSON utilizando Jackson ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        for (Document doc : bsonDocuments) {
            try {
                String jsonDocument = objectMapper.writeValueAsString(doc);
                jsonDocuments.add(jsonDocument);
            } catch (JsonProcessingException e) {
                // Manejo de errores en caso de que ocurra una excepción al convertir a JSON
                e.printStackTrace();
            }
        }
        return jsonDocuments;
    }
}
