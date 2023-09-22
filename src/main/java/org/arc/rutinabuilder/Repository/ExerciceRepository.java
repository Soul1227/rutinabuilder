package org.arc.rutinabuilder.Repository;

import org.arc.rutinabuilder.Entity.Exercise;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciceRepository extends MongoRepository<Exercise, ObjectId> {
}
