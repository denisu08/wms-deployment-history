package com.wirecard.tools.development.web;

import com.wirecard.tools.development.model.HistoryLog;
import com.wirecard.tools.development.model.HistoryLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api")
class HistoryController {

    private final Logger log = LoggerFactory.getLogger(HistoryController.class);

    @Autowired
    private HistoryLogRepository historyLogRepository;

    @GetMapping("/histories")
    Collection<HistoryLog> histories() {
        return historyLogRepository.findAll();
    }

    @GetMapping("/history/{id}")
    ResponseEntity<?> getGroup(@PathVariable String id) {
        Optional<HistoryLog> group = historyLogRepository.findById(id);
        return group.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/history")
    ResponseEntity<HistoryLog> createGroup(@Valid @RequestBody HistoryLog group) throws URISyntaxException {
        log.info("Request to create group: {}", group);
        HistoryLog result = historyLogRepository.save(group);
        return ResponseEntity.created(new URI("/api/group/" + result.get_id()))
                .body(result);
    }

    @PutMapping("/history/{id}")
    ResponseEntity<HistoryLog> updateGroup(@Valid @RequestBody HistoryLog group) {
        log.info("Request to update group: {}", group);
        HistoryLog result = historyLogRepository.save(group);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/history/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable String id) {
        log.info("Request to delete group: {}", id);
        historyLogRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}