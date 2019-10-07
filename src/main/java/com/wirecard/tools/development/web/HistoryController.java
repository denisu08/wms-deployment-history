package com.wirecard.tools.development.web;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Sorts;
import com.wirecard.tools.development.model.HistoryLog;
import com.wirecard.tools.development.model.HistoryLogRepository;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
class HistoryController {

    private final Logger log = LoggerFactory.getLogger(HistoryController.class);

    @Autowired
    private HistoryLogRepository historyLogRepository;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    private SimpleDateFormat displayedFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");

    @GetMapping("/histories")
    Collection<HistoryLog> histories(@RequestParam String startTime, @RequestParam String endTime) throws ParseException {
        Date startTimeParsed = sdf.parse(startTime);
        Date endTimeParsed = sdf.parse(endTime);
        List historyLogList = new ArrayList<>();
        List<HistoryLog> res = (List<HistoryLog>) historyLogRepository.findAllByModifiedDateBetween(startTimeParsed, endTimeParsed);
        res.sort(new Comparator<HistoryLog>() {
            @Override
            public int compare(HistoryLog o1, HistoryLog o2) {
                return o1.getModifiedDate().compareTo(o2.getModifiedDate());
            }
        });

        for (HistoryLog item : res) {
            Map map = new HashMap();
            map.put("id", item.get_id());
            map.put("activityType", item.getActivityType());
            map.put("comment", item.getComment());
            map.put("description", item.getDescription());
            map.put("action", item.getAction());
            map.put("modifiedDate", displayedFormat.format(item.getModifiedDate()));
            map.put("modifiedBy", item.getModifiedBy());
            historyLogList.add(map);
        }

        return historyLogList;
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