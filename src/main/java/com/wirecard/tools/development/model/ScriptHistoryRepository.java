package com.wirecard.tools.development.model;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScriptHistoryRepository extends MongoRepository<ScriptHistory, String> {
    ScriptHistory findBy_id(String _id);
}