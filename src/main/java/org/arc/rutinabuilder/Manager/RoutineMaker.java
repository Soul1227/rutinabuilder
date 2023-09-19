package org.arc.rutinabuilder.Manager;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class RoutineMaker {

    @Autowired
    MongoTemplate mongoTemplate;

    public RoutineMaker() {
    }

    /**
     *
     * @return
     */
    public HashMap<Date, String> filterMusclesByDate() {
        Set<String> collectionNames = mongoTemplate.getCollectionNames();
        List<String> guideCollections = collectionNames.stream().filter(collectionName -> collectionName.contains("_guide")).toList();
        HashMap<Date, String> datedCollection = new HashMap<>();
        Query query = new Query().with(Sort.by(Sort.Order.desc("date"))).limit(1);
        for (String collectionName : guideCollections) {
            Document exercice = mongoTemplate.findOne(query, Document.class, collectionName);
            if (exercice != null) {
                Date exerciceLastDate = exercice.getDate("date");
                datedCollection.put(exerciceLastDate, collectionName);
            }
        }
        return datedCollection;
    }
}
