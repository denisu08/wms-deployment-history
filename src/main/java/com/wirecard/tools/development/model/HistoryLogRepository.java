package com.wirecard.tools.development.model;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface HistoryLogRepository extends MongoRepository<HistoryLog, String> {
    HistoryLog findBy_id(String _id);
    List<HistoryLog> findAllByModifiedDateBetween(Date startTime, Date endTime);
}