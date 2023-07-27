package org.arc.rutinabuilder.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.arc.rutinabuilder.Entity.Counter;
import org.arc.rutinabuilder.Entity.Exercice;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * Saves an Exercice object into the specified collection.
     *
     * @param exercice The Exercice object to be saved.
     * @return The saved Exercice object.
     */
    public Exercice saveExercice(Exercice exercice) {
        if (exercice.getId() == null) {
            exercice.setId(getNextIdForNewObject());
        }
        return mongoTemplate.save(exercice, exercice.getCollection());
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
        return mongoTemplate.save(exercice, exercice.getCollection());
    }

    /**
     * Find an Exercice from the DB, based on its id and collection.
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
     * Removes an exercice from the database.
     *
     * @param exercice to be deleted.
     * @return true if it was deleted, false if it was not.
     */
    public boolean deleteExercice(Exercice exercice) {
        Query query = new Query(Criteria.where("id").is(exercice.getId()));
        mongoTemplate.remove(query, Exercice.class, exercice.getCollection());
        return findOneById(exercice.getId(), exercice.getCollection()) == null;
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
     * Retrive the BSON documents from a specific collection.
     *
     * @param collectionName the name of the collection.
     * @return a list of BSON documents.
     */
    public List<Document> getAllExerciceDocuments(String collectionName) {
        MongoCollection<Document> collection = mongoTemplate.getCollection(collectionName);
        List<Document> documents = new ArrayList<>();

        FindIterable<Document> findIterable = collection.find();
        findIterable.forEach(documents::add);

        return documents;
    }

    /**
     * Converts a list of BSON documents to JSON objects.
     *
     * @param bsonDocuments a list of BSON documents
     * @return a list of documents in JSON format
     */
    public List<String> convertToJSON(List<Document> bsonDocuments) {
        List<String> jsonDocuments = new ArrayList<>();

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
