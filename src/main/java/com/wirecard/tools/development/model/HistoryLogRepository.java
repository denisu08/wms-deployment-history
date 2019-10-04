package com.wirecard.tools.development.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HistoryLogRepository extends MongoRepository<HistoryLog, String> {
    HistoryLog findBy_id(String _id);
}